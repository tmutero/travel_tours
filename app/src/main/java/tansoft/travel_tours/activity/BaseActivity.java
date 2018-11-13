package tansoft.travel_tours.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import tansoft.travel_tours.R;
import tansoft.travel_tours.activity.preference.SettingsActivity;
import tansoft.travel_tours.fragment.BookingFragment;
import tansoft.travel_tours.fragment.DashboardFragment;
import tansoft.travel_tours.fragment.SearchFragment;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    // Renders the app toolbar
    void displayToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Renders the app navigation drawer and the toolbar
    void displayDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent= new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            finish();


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
            //logoutUser();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
