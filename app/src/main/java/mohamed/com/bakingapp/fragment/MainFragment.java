package mohamed.com.bakingapp.fragment;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mohamed.com.bakingapp.R;
import mohamed.com.bakingapp.activity.DetailsActivity;
import mohamed.com.bakingapp.adapter.BakesAdapter;
import mohamed.com.bakingapp.dialog.LoadingDialog;
import mohamed.com.bakingapp.interfaces.BakeItemListener;
import mohamed.com.bakingapp.model.BakedResponse;
import mohamed.com.bakingapp.retrofit.ApiClient;
import mohamed.com.bakingapp.retrofit.ApiInterface;
import mohamed.com.bakingapp.utils.Constants;
import mohamed.com.bakingapp.widget.IngredientsWidget;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements BakeItemListener {

    @BindView(R.id.toolbar_main_frag)
    Toolbar toolbar;

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @BindView(R.id.rv_backs)
    RecyclerView rvBakes;

    List<BakedResponse> bakes;
    BakesAdapter bakesAdapter;
    GridLayoutManager layoutManager;

    ApiInterface apiInterface;
    LoadingDialog loadingDialog;
    int scrollPosition =-1;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        rootView.findViewById(R.id.btn_back).setVisibility(View.GONE);
        bakes =new ArrayList<>();
        loadingDialog =new LoadingDialog(getContext());
        tvToolbarTitle.setText("Baking App");
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            layoutManager =new GridLayoutManager(getContext(),2);
        } else {
            // In portrait
            layoutManager =new GridLayoutManager(getContext(),1);
        }

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            // do something
            layoutManager =new GridLayoutManager(getContext(),3);
        } else {
            // do something else
            layoutManager =new GridLayoutManager(getContext(),1);
        }

        bakesAdapter =new BakesAdapter(getContext(), bakes, this);
        rvBakes.setLayoutManager(layoutManager);
        rvBakes.setAdapter(bakesAdapter);
        loadBakes();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //super.onSaveInstanceState(outState);
        int x =layoutManager.findLastCompletelyVisibleItemPosition();

        if (x ==-1){
            x =layoutManager.findLastCompletelyVisibleItemPosition();
        }
        outState.putInt(Constants.BAKES_POSITION,x);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState !=null) {
            if (savedInstanceState.containsKey(Constants.BAKES_POSITION)) {
                scrollPosition = savedInstanceState.getInt(Constants.BAKES_POSITION);
            }
        }
    }

    private void loadBakes() {
        loadingDialog.show();
        apiInterface = ApiClient.getApiClient(getContext()).create(ApiInterface.class);

        Call<List<BakedResponse>> call = apiInterface.getBakingResponse();

        call.enqueue(new Callback<List<BakedResponse>>() {
            @Override
            public void onResponse(Call<List<BakedResponse>> call, Response<List<BakedResponse>> response) {
                bakes.clear();
                bakes.addAll(response.body());
                bakesAdapter.notifyDataSetChanged();
                if (scrollPosition >=0){
                    rvBakes.scrollToPosition(scrollPosition);
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<BakedResponse>> call, Throwable t) {
                loadingDialog.dismiss();
                Log.e("RESPONSE_ERROR", t.getMessage());
                Toast.makeText(getContext(), "Something Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBakeItemClicked(BakedResponse bakedResponse, int position) {
        if (bakedResponse != null) {
            Toast.makeText(getContext(), "HH", Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(getContext(), DetailsActivity.class);
            intent.putExtra(Constants.BAKE_OBJ_INTENT,bakedResponse);
            Log.e("STEPS",bakedResponse.getStepResponses().get(0).getShortDescription());
            startActivity(intent);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getContext(), IngredientsWidget.class));
            IngredientsWidget.updateFromActivity(getContext(), appWidgetManager, appWidgetIds, position, (ArrayList<BakedResponse>) bakes);
        }
    }

}