package mohamed.com.bakingapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mohamed.com.bakingapp.R;
import mohamed.com.bakingapp.activity.IngredientsActivity;
import mohamed.com.bakingapp.adapter.StepsAdapter;
import mohamed.com.bakingapp.model.BakedResponse;
import mohamed.com.bakingapp.model.IngredientResponse;
import mohamed.com.bakingapp.model.StepResponse;
import mohamed.com.bakingapp.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    @BindView(R.id.toolbar_details_frag)
    Toolbar toolbar;

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    BakedResponse bakeObject;
//    @BindView(R.id.collapse_imageView)
//    ImageView imgCollapseBar;
//    //
//    @BindView(R.id.toolbar_details)
//    Toolbar toolbarDetails;

//    @BindView(R.id.collapseToolbar_layout)
//    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.tv_ingredients)
    TextView tvIngredients;

    @BindView(R.id.rv_steps)
    RecyclerView rvSteps;

    StepsAdapter stepsAdapter;
    LinearLayoutManager layoutManager;
    List<StepResponse> steps;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            bakeObject = (BakedResponse) bundle.getSerializable(Constants.BAKE_OBJ_BUNDLE);
            steps = new ArrayList<>();
            steps.addAll(bakeObject.getStepResponses());
            Log.e("STEPS", bakeObject.getStepResponses().get(0).getShortDescription()+"");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, rootView);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        tvToolbarTitle.setText("Details");
        layoutManager = new LinearLayoutManager(getContext());
        stepsAdapter = new StepsAdapter(getContext(), steps);
        rvSteps.setLayoutManager(layoutManager);
        rvSteps.setAdapter(stepsAdapter);

        tvIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<IngredientResponse> ingredients = (ArrayList<IngredientResponse>) bakeObject.getIngredientResponses();
                Intent intent =new Intent(getContext(), IngredientsActivity.class);
                Bundle bundle =new Bundle();
                bundle.putSerializable(Constants.INGREDIENTS, ingredients);
                intent.putExtra(Constants.INGREDIENTS_BUNDLE,bundle);
                startActivity(intent);
            }
        });
        return rootView;
    }
}