package mohamed.com.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mohamed.com.bakingapp.R;
import mohamed.com.bakingapp.model.BakedResponse;
import mohamed.com.bakingapp.model.IngredientResponse;
import mohamed.com.bakingapp.retrofit.ApiClient;
import mohamed.com.bakingapp.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<BakedResponse> bakes;

    public WidgetFactory(Context applicationContext, Intent intent) {

        mContext = applicationContext;
        bakes =new ArrayList<>();

    }

    @Override
    public void onCreate() {
        getBakes();
        SystemClock.sleep(1000);
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {


        if (bakes != null) {

            return bakes.size();

        } else {

            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {

        BakedResponse bakedResponse = bakes.get(position);
        List<IngredientResponse> ingredients = bakedResponse.getIngredientResponses();
        String x = "";
        int i = 0;
        for (IngredientResponse ingredient : ingredients) {
            x = x + (++i) + ". " + ingredient.getIngredient() + " \n";
        }


        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_lv_item);
        rv.setTextViewText(R.id.tv1, bakedResponse.getName());
        rv.setTextViewText(R.id.tv2, x);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private void getBakes(){
        ApiInterface apiInterface = ApiClient.getApiClient(mContext).create(ApiInterface.class);

        Call<List<BakedResponse>> call = apiInterface.getBakingResponse();

        call.enqueue(new Callback<List<BakedResponse>>() {
            @Override
            public void onResponse(Call<List<BakedResponse>> call, Response<List<BakedResponse>> response) {
                bakes.clear();
                bakes.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<BakedResponse>> call, Throwable t) {
                Log.e("RESPONSE_ERROR", t.getMessage());
                Toast.makeText(mContext, "Something Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}