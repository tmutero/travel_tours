package tansoft.travel_tours.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import tansoft.travel_tours.R;

public class ResortViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resort_view);
        final String resortName = getIntent().getStringExtra("resortName");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        System.out.println("----------------------" + resortName);


    }

}
