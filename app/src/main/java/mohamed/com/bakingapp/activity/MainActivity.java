package mohamed.com.bakingapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import mohamed.com.bakingapp.R;
import mohamed.com.bakingapp.fragment.MainFragment;
import mohamed.com.bakingapp.utils.BakingIdlingResource;

public class MainActivity extends AppCompatActivity {

    IdlingResource idlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String tag = "my_fragment";
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentByTag(tag) == null) {
            MainFragment mainFragment = new MainFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fl_main, mainFragment, tag);
            transaction.commit();
        }
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
