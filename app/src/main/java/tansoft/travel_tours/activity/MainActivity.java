package tansoft.travel_tours.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;

import tansoft.travel_tours.R;

import tansoft.travel_tours.fragment.BookingFragment;
import tansoft.travel_tours.fragment.DashboardFragment;
import tansoft.travel_tours.fragment.SearchFragment;
import tansoft.travel_tours.services.SQLiteHandler;
import tansoft.travel_tours.services.SessionManager;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            FragmentManager settingsFm = getSupportFragmentManager();
//            FragmentTransaction ft = settingsFm.beginTransaction();
////            ft.replace(R.id.frag_container, new SettingsFragment());
//            ft.addToBackStack("SettingsFragment");
//            ft.commit();

          
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_booking) {
            FragmentManager settingsFm = getSupportFragmentManager();
            FragmentTransaction ft = settingsFm.beginTransaction();
            ft.replace(R.id.frag_container, new BookingFragment());
            ft.addToBackStack("BookingFragment");
            ft.commit();

        } else if (id == R.id.nav_share) {


        } else if (id == R.id.nav_search) {
            FragmentManager settingsFm = getSupportFragmentManager();
            FragmentTransaction ft = settingsFm.beginTransaction();
            ft.replace(R.id.frag_container, new SearchFragment());
            ft.addToBackStack("SearchFragment");
            ft.commit();


        } else if (id == R.id.nav_home) {
            FragmentManager settingsFm = getSupportFragmentManager();
            FragmentTransaction ft = settingsFm.beginTransaction();
            ft.replace(R.id.frag_container, new DashboardFragment());
            ft.addToBackStack("DashbordFragment");
            ft.commit();
        } else if (id == R.id.nav_logout) {
            //logout Function
            logoutUser();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
