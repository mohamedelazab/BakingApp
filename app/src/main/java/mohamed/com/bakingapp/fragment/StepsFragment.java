package mohamed.com.bakingapp.fragment;


import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import mohamed.com.bakingapp.R;
import mohamed.com.bakingapp.model.StepResponse;
import mohamed.com.bakingapp.utils.Constants;
import mohamed.com.bakingapp.utils.Helper;
import mohamed.com.bakingapp.utils.Preferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment {

    @BindView(R.id.toolbar_steps_frag)
    Toolbar toolbar;

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @BindView(R.id.btn_back)
    ImageButton btnBack;

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView exoPlayerView;

    @BindView(R.id.thumbnail_step)
    ImageView thumbnailStep;

    @BindView(R.id.img_place_holder)
    TextView tvPlaceHolder;

    @BindView(R.id.btn_next)
    Button btnNext;

    @BindView(R.id.btn_previous)
    Button btnPrevious;

    @BindView(R.id.tv_step_desc)
    TextView tvDesc;

    @BindView(R.id.tv_step_short_desc)
    TextView tvShortDesc;


    SimpleExoPlayer simpleExoPlayer;
    ArrayList<StepResponse> steps;
    int currentStepIndex = 0;
    Long videoPositionState =C.TIME_UNSET;
    boolean playerState =true;

    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        steps = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, rootView);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        tvToolbarTitle.setText("Steps");
            Bundle bundle = getArguments();
            if (bundle != null) {
                bundle = getArguments();
                //StepResponse currentStep = (StepResponse) bundle.getSerializable(Constants.STEP_BUNDLE);
                currentStepIndex = bundle.getInt(Constants.STEP_INDEX);
                steps.addAll((Collection<? extends StepResponse>) bundle.getSerializable(Constants.ALL_STEPS_BUNDLE));

                    if (savedInstanceState !=null) {
                        videoPositionState = savedInstanceState.getLong("CURRENT_POSITION", C.TIME_UNSET);
                        Log.e("RESTORED_DATA", "video position: " + videoPositionState + "");
                        steps = (ArrayList<StepResponse>) savedInstanceState.getSerializable("STEPS");
                        Log.e("RESTORED_DATA", "Steps size: " + steps.size() + "");
                        currentStepIndex = savedInstanceState.getInt("CURRENT_STEP");
                        Log.e("RESTORED_DATA", "Current Step: " + currentStepIndex + "");
                        playerState =savedInstanceState.getBoolean("playerState");
                    }

                showVideo(steps.get(currentStepIndex));
                Log.e("SHOW_CURRENT", "CREATED OR ROTATED.! "+currentStepIndex);
            }


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishVideoService();
                currentStepIndex++;
                if (currentStepIndex <= steps.size()-1) {
                    playerState =true;
                    videoPositionState =C.TIME_UNSET;
                    showVideo(steps.get(currentStepIndex));
                }
                if (currentStepIndex >=steps.size()){
                    currentStepIndex =steps.size()-1;
                }
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishVideoService();
                currentStepIndex--;
                if (currentStepIndex >= 0) {
                    playerState =true;
                    videoPositionState =C.TIME_UNSET;
                    showVideo(steps.get(currentStepIndex));
                }

                if (currentStepIndex < 0){
                    currentStepIndex = 0;
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return rootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //First Hide other objects (listview or recyclerview), better hide them using Gone.
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) exoPlayerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            exoPlayerView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //unhide your objects here.
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) exoPlayerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=600;
            exoPlayerView.setLayoutParams(params);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        playerState = simpleExoPlayer.getPlayWhenReady();
        outState.putBoolean("playerState", playerState);
        outState.putLong("CURRENT_POSITION", simpleExoPlayer.getCurrentPosition());
        outState.putInt("CURRENT_STEP", currentStepIndex);
        outState.putSerializable("STEPS", steps);
    }



    private void showVideo(final StepResponse currentStep) {
        exoPlayerView.setVisibility(View.VISIBLE);
        tvPlaceHolder.setVisibility(View.GONE);
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            Uri videoUri = null;
            if (!currentStep.getVideoURL().equals("")) {
                videoUri = Uri.parse(currentStep.getVideoURL());
                Log.e("VIDEO", currentStep.getVideoURL());
            }
            else {
                exoPlayerView.setVisibility(View.GONE);
                tvPlaceHolder.setVisibility(View.VISIBLE);
            }

            //if thumbnail img exist
            if (!currentStep.getThumbnailURL().equals("") &&
                    Helper.isImageFile(currentStep.getThumbnailURL())) {
                Picasso.get().load(currentStep.getThumbnailURL()).into(thumbnailStep);
            }
            //extract thumbnail img if video exist
            else if (!currentStep.getThumbnailURL().equals("") &&
                    Helper.isVideoFile(currentStep.getThumbnailURL())){
                //TODO:
                String strImg =new Preferences(getContext()).getThumbnail();
                if( !strImg.equals("") ){
                    byte[] b = Base64.decode(strImg, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    thumbnailStep.setVisibility(View.VISIBLE);
                    thumbnailStep.setImageBitmap(bitmap);
                }
            }
            else{
                thumbnailStep.setVisibility(View.GONE);
            }

            if (videoUri != null) {
                int currentOrientation = getResources().getConfiguration().orientation;
                if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // Landscape
                    //Toast.makeText(getActivity(), "Landscape", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Portrait
                    //Toast.makeText(getActivity(), "Portrait", Toast.LENGTH_SHORT).show();
                }

                DefaultHttpDataSourceFactory sourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory factory = new DefaultExtractorsFactory();
                MediaSource videoSource = new ExtractorMediaSource(videoUri, sourceFactory, factory, null, null);
                exoPlayerView.setPlayer(simpleExoPlayer);
                if (videoPositionState != C.TIME_UNSET) simpleExoPlayer.seekTo(videoPositionState);
                simpleExoPlayer.prepare(videoSource);
                simpleExoPlayer.setPlayWhenReady(playerState);
            } else {
                //Toast.makeText(getContext(), "Null Uri.!", Toast.LENGTH_SHORT).show();
            }
            if (!currentStep.getDescription().equals(""))
                tvDesc.setText(currentStep.getDescription());
            if (!currentStep.getShortDescription().equals(""))
                tvShortDesc.setText(currentStep.getShortDescription());

        } catch (Exception e) {
            Log.e("EXOPLAYER_EXCEPTION", e.getMessage());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        videoPositionState =simpleExoPlayer.getCurrentPosition();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (simpleExoPlayer!=null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (simpleExoPlayer!=null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer=null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
    }

    private void finishVideoService(){
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
    }
}