package mohamed.com.bakingapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import mohamed.com.bakingapp.R;
import mohamed.com.bakingapp.fragment.StepsFragment;
import mohamed.com.bakingapp.utils.BakingIdlingResource;

public class StepsActivity extends AppCompatActivity {

    IdlingResource idlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        String tag = "steps_fragment";
        if (savedInstanceState ==null) {
            Bundle data = getIntent().getExtras();
            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setArguments(data);
            FragmentManager manager = this.getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fl_steps, stepsFragment, tag);
            transaction.addToBackStack(tag);
            transaction.commit();
        }
        else {
            getSupportFragmentManager().findFragmentByTag(tag);
        }
    }

    @Override
    public void onBackPressed() {
        StepsActivity.this.finish();
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new BakingIdlingResource();
        }
        return idlingResource;
    }
}