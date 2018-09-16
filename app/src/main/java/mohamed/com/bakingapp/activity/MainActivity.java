package mohamed.com.bakingapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import mohamed.com.bakingapp.R;
import mohamed.com.bakingapp.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

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
            //Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();
        }
    }
}
