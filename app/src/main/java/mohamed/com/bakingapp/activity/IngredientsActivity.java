package mohamed.com.bakingapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mohamed.com.bakingapp.R;
import mohamed.com.bakingapp.adapter.IngredientsAdapter;
import mohamed.com.bakingapp.model.IngredientResponse;
import mohamed.com.bakingapp.utils.Constants;

public class IngredientsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_ingredients)
    Toolbar toolbar;

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @BindView(R.id.btn_back)
    ImageButton btnBack;

    @BindView(R.id.rv_ingredients)
    RecyclerView rvIngredients;

    IngredientsAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        Bundle bundle =getIntent().getBundleExtra(Constants.INGREDIENTS_BUNDLE);
        if (bundle !=null){
            tvToolbarTitle.setText(getIntent().getExtras().getString(Constants.BAKE_NAME));
            List<IngredientResponse> ingredients = (ArrayList<IngredientResponse>) bundle.getSerializable(Constants.INGREDIENTS);
            Log.e("INGREDIENTS", ingredients.size()+"");
            layoutManager =new LinearLayoutManager(IngredientsActivity.this);
            adapter =new IngredientsAdapter(IngredientsActivity.this, ingredients);
            rvIngredients.setLayoutManager(layoutManager);
            rvIngredients.setAdapter(adapter);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
