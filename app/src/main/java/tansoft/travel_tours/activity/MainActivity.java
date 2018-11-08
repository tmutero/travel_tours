package tansoft.travel_tours.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.HashMap;

import tansoft.travel_tours.R;
import tansoft.travel_tours.fragment.DashboardFragment;
import tansoft.travel_tours.services.SQLiteHandler;
import tansoft.travel_tours.services.SessionManager;


public class MainActivity extends BaseActivity {

    private SQLiteHandler db;
    private SessionManager session;
    private TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_container, new DashboardFragment());
        ft.addToBackStack("DashboardFragment");
        ft.commit();

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");

        displayDrawer(); // Displays the toolbar and the drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



}
