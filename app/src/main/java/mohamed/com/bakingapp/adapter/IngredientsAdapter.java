package mohamed.com.bakingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mohamed.com.bakingapp.R;
import mohamed.com.bakingapp.model.IngredientResponse;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>{
    private List<IngredientResponse> ingredients;
    private Context context;

    public IngredientsAdapter(Context context, List<IngredientResponse> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsAdapter.IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.rv_ingredient_item, parent, false);
        return new IngredientsAdapter.IngredientsViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.IngredientsViewHolder holder, int position) {
        holder.tvTitle.setText(ingredients.get(position).getIngredient());
        holder.tvQuantityMeasure.setText(" "+ingredients.get(position).getQuantity()
        +" "+ingredients.get(position).getMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvQuantityMeasure;

        IngredientsViewHolder(View itemView) {
            super(itemView);
            tvTitle =itemView.findViewById(R.id.tv_ingredient_title);
            tvQuantityMeasure =itemView.findViewById(R.id.tv_quantity_measure);
        }
    }
}
