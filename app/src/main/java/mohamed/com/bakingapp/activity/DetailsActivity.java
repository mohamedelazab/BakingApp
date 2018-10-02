package mohamed.com.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.List;

import mohamed.com.bakingapp.R;
import mohamed.com.bakingapp.fragment.DetailsFragment;
import mohamed.com.bakingapp.fragment.StepsFragment;
import mohamed.com.bakingapp.interfaces.StepItemListener;
import mohamed.com.bakingapp.model.StepResponse;
import mohamed.com.bakingapp.utils.Constants;

public class DetailsActivity extends AppCompatActivity implements StepItemListener{

    public static boolean isTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (findViewById(R.id.fl_steps) !=null){
            isTwoPane =true;
        }

        Bundle data = new Bundle();
        data.putSerializable(Constants.BAKE_OBJ_BUNDLE, getIntent().getExtras().getSerializable(Constants.BAKE_OBJ_INTENT));
        String tag = "details_fragment";
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentByTag(tag) == null) {
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setArguments(data);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fl_details, detailsFragment, tag);
            //transaction.addToBackStack(tag);
            transaction.commit();
            //Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onBackPressed() {

//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            for (int i = 0; i <getSupportFragmentManager().getBackStackEntryCount() ; i++) {
//                Log.e("BACK", getSupportFragmentManager().getFragments().get(i).getTag()+"");
//            }
//            getSupportFragmentManager().popBackStack();
//
//            if (getSupportFragmentManager().getBackStackEntryCount() == 0){
//                DetailsActivity.this.finish();
//            }
//        } else {
//            super.onBackPressed();
//        }
        DetailsActivity.this.finish();
    }

    @Override
    public void onStepItemClicked(StepResponse stepResponse, List<StepResponse> steps) {
        if (isTwoPane){
            Bundle data =new Bundle();
            data.putSerializable(Constants.STEP_BUNDLE, stepResponse);
            for (int i = 0; i < steps.size(); i++) {
                if (steps.get(i).getId() ==stepResponse.getId()){
                    data.putInt(Constants.STEP_INDEX,i);
                }
            }
            data.putSerializable(Constants.ALL_STEPS_BUNDLE, (Serializable) steps);
            StepsFragment stepsFragment =new StepsFragment();
            stepsFragment.setArguments(data);
            FragmentManager manager = this.getSupportFragmentManager();
            FragmentTransaction transaction =manager.beginTransaction();
            String tag ="steps_fragment";
            transaction.add(R.id.fl_steps,stepsFragment, tag);
            transaction.addToBackStack(tag);
            transaction.commit();
        }
        else {
            Bundle data = new Bundle();
            data.putSerializable(Constants.STEP_BUNDLE, stepResponse);
            for (int i = 0; i < steps.size(); i++) {
                if (steps.get(i).getId() ==stepResponse.getId()){
                    data.putInt(Constants.STEP_INDEX,i);
                }
            }
            data.putSerializable(Constants.ALL_STEPS_BUNDLE, (Serializable) steps);
            Intent intent =new Intent(DetailsActivity.this, StepsActivity.class);
            intent.putExtras(data);
            startActivity(intent);
        }
    }
}
