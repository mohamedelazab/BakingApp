package mohamed.com.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mohamed.com.bakingapp.R;
import mohamed.com.bakingapp.interfaces.BakeItemListener;
import mohamed.com.bakingapp.model.BakedResponse;

public class BakesAdapter extends RecyclerView.Adapter<BakesAdapter.BakesViewHolder> {

    private List<BakedResponse> bakes;
    private Context context;
    private BakeItemListener bakeItemListener;

    public BakesAdapter(Context context, List<BakedResponse> bakes, BakeItemListener bakeItemListener) {
        this.context = context;
        this.bakes = bakes;
        this.bakeItemListener = bakeItemListener;
    }

    @NonNull
    @Override
    public BakesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.rv_backs_item, parent, false);
        return new BakesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BakesViewHolder holder, int position) {
        holder.tvBakeName.setText(bakes.get(position).getName());
        if (!bakes.get(position).getImage().isEmpty() || !bakes.get(position).getImage().equals("")){
            Picasso.get().load(bakes.get(position).getImage()).into(holder.imgBake);
        }
    }

    @Override
    public int getItemCount() {
        return bakes.size();
    }

    public class BakesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgBake;
        TextView tvBakeName;

        public BakesViewHolder(View itemView) {
            super(itemView);
            imgBake =itemView.findViewById(R.id.img_bake_item);
            tvBakeName =itemView.findViewById(R.id.tv_baking_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            bakeItemListener.onBakeItemClicked(bakes.get(getAdapterPosition()),getAdapterPosition());
        }
    }
}
