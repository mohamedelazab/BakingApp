package mohamed.com.bakingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import mohamed.com.bakingapp.interfaces.StepItemListener;
import mohamed.com.bakingapp.model.StepResponse;
import mohamed.com.bakingapp.utils.Helper;
import mohamed.com.bakingapp.utils.Preferences;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder>{

    private List<StepResponse> steps;
    private Context context;
    private StepItemListener stepItemListener;

    public StepsAdapter(Context context, List<StepResponse> steps, StepItemListener stepItemListener) {
        this.context = context;
        this.steps = steps;
        this.stepItemListener = stepItemListener;
    }

    public StepsAdapter(Context context, List<StepResponse> steps) {
        this.context = context;
        this.steps = steps;
    }

    @NonNull
    @Override
    public StepsAdapter.StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.rv_steps_item, parent, false);
        return new StepsAdapter.StepsViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.StepsViewHolder holder, int position) {
        holder.tvStepCount.setText("Step: "+(steps.get(position).getId()+1));
        holder.tvStepDesc.setText(steps.get(position).getShortDescription());
        String thumbnailImg =steps.get(position).getThumbnailURL();
        if (!thumbnailImg.equals("") && Helper.isImageFile(thumbnailImg)) {
            Picasso.get().load(thumbnailImg).placeholder(R.drawable.placeholder).into(holder.thumbnailStepImg);
        }
        else if (!thumbnailImg.equals("")&& Helper.isVideoFile(thumbnailImg)){
                new RetrieveThumbnail(thumbnailImg,holder).execute();
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvStepDesc, tvStepCount;
        ImageView thumbnailStepImg;

        public StepsViewHolder(View itemView) {
            super(itemView);
            tvStepDesc =itemView.findViewById(R.id.tv_step_desc);
            tvStepCount =itemView.findViewById(R.id.tv_step_count);
            thumbnailStepImg =itemView.findViewById(R.id.thumbnail_step_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //stepItemListener.onStepItemClicked();
            ((StepItemListener) context).onStepItemClicked(steps.get(getAdapterPosition()), steps);
        }
    }


    public class RetrieveThumbnail extends AsyncTask<Void, Void, Bitmap> {

        String videoPath;
        StepsAdapter.StepsViewHolder holder;

        public RetrieveThumbnail(String videoPath,StepsAdapter.StepsViewHolder holder){
            this.videoPath =videoPath;
            this.holder =holder;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = null;
            try {
                bitmap = Helper.retrieveVideoFrameFromVideo(videoPath);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap !=null) {
                holder.thumbnailStepImg.setImageBitmap(bitmap);
                new Preferences(context).setThumbnail(Helper.convertBitmapToBase64(bitmap));
            }
            else
                holder.thumbnailStepImg.setImageResource(R.drawable.placeholder);
        }
    }
}