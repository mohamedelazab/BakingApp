package mohamed.com.bakingapp.fragment;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import mohamed.com.bakingapp.R;
import mohamed.com.bakingapp.model.StepResponse;
import mohamed.com.bakingapp.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment {

    @BindView(R.id.toolbar_steps_frag)
    Toolbar toolbar;

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView exoPlayerView;

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
                    currentStepIndex = savedInstanceState.getInt("CURRENT_STEP");
                    videoPositionState =savedInstanceState.getLong("CURRENT_POSITION");
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
                    showVideo(steps.get(currentStepIndex));
                }

                if (currentStepIndex < 0){
                    currentStepIndex = 0;
                }
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
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) exoPlayerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=600;
            exoPlayerView.setLayoutParams(params);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: save th state of video, list of steps, the position of step, and the index of buttons..!
        outState.putLong("CURRENT_POSITION", simpleExoPlayer.getCurrentPosition());
        outState.putInt("CURRENT_STEP", currentStepIndex);
        outState.putSerializable("STEPS", steps);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //TODO: RESTORE ALL DATA AND STATES..
        if (savedInstanceState !=null) {
            videoPositionState = savedInstanceState.getLong("CURRENT_POSITION", C.TIME_UNSET);
            Log.e("RESTORED_DATA", "video position: " + videoPositionState + "");
            steps = (ArrayList<StepResponse>) savedInstanceState.getSerializable("STEPS");
            Log.e("RESTORED_DATA", "Steps size: " + steps.size() + "");
            currentStepIndex = savedInstanceState.getInt("CURRENT_STEP");
            Log.e("RESTORED_DATA", "Current Step: " + currentStepIndex + "");
        }
    }

    private void showVideo(StepResponse currentStep) {
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
            } else if (!currentStep.getThumbnailURL().equals("")) {
                videoUri = Uri.parse(currentStep.getThumbnailURL());
                Log.e("Thumbnil", currentStep.getThumbnailURL());
            } else {
                exoPlayerView.setVisibility(View.GONE);
                tvPlaceHolder.setVisibility(View.VISIBLE);
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
                simpleExoPlayer.setPlayWhenReady(true);

            } else {
                //Toast.makeText(getContext(), "Null Uri.!", Toast.LENGTH_SHORT).show();
            }
            if (!currentStep.getDescription().equals(""))
                tvDesc.setText(currentStep.getDescription());
            if (!currentStep.getShortDescription().equals(""))
                tvShortDesc.setText(currentStep.getShortDescription());

        } catch (Exception e) {
            Log.e("EXOPLAYER_EXCEPTION", e.getMessage());
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