package com.buah.farmconnect.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.buah.farmconnect.R;
import com.buah.farmconnect.api.Api;
import com.buah.farmconnect.api.ApiCall;
import com.buah.farmconnect.api.Result;
import com.buah.farmconnect.fragment.FragmentCategory;
import com.buah.farmconnect.fragment.FragmentHome;
import com.buah.farmconnect.fragment.FragmentMyProduct;
import com.buah.farmconnect.object.ObjectUser;
import com.buah.farmconnect.session.SharedPrefManager;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHome extends AppCompatActivity {

    Fragment mCurrentFragment;
    View navHeader;
    ObjectUser user;
    Toolbar mToolbar;
    TextView fullName;
    TextView farmName;
    Menu mNavMenu;
    FloatingActionButton fab;
    DrawerLayout mDrawerLayout;
    CircleImageView userPicture;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mDrawerToggle;
    boolean clicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialize widgets and components
        initialize();

        // Setting up the Home Fragment
        setHomeFragment(new FragmentHome());

        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
            mNavigationView.inflateMenu(R.menu.logged_in_nav_drawer_menu);
        } else {
            mNavigationView.inflateMenu(R.menu.logged_out_nav_drawer_menu);
        }

        String fullname = SharedPrefManager.getInstance(getApplicationContext()).getobjectUser().getFullname();
        String email = SharedPrefManager.getInstance(getApplicationContext()).getobjectUser().getEmail();

        fullName.setText(fullname);
        farmName.setText(email);

        // Replace action bar and set custom mToolbar as actionbar
        setSupportActionBar(mToolbar);

        // Setting up the Up button
        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // Setting up the mDrawerLayout's app bar listener
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        // Setting up the itemSelectedListener
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                displaySelectedScreen(item.getItemId());
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                    startActivity(new Intent(getBaseContext(), ActivityAddProduct.class));
                } else {
                    Snackbar.make(findViewById(R.id.homeAppBar_rootLayout), "Login to complete this action.", Snackbar.LENGTH_LONG)
                            .setAction(R.string.title_activity_login, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getBaseContext(), ActivityLogin.class));
                                }
                            }).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    @Override
    public void onBackPressed(){
        if (mCurrentFragment == getSupportFragmentManager().findFragmentById(R.id.fragmentHome_rootLayout)){
            super.onBackPressed();
        }
    }

    public void setHomeFragment(Fragment fragment) {
        mCurrentFragment = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeLayout, mCurrentFragment)
                .commit();
    }

    public Fragment getHomeFragment(){
        return mCurrentFragment;
    }

    private void displaySelectedScreen(@IdRes int itemId) {
        Fragment fragment = null;
        Activity activity = null;
        String message = "Hi!";

        // Set destination fragment
        switch (itemId) {
            case R.id.nav_item_home:
                fragment = new FragmentHome();
                break;
            case R.id.nav_item_account:
                activity = new ActivityMyAccount();
                break;
            case R.id.nav_item_products:
                fragment = new FragmentMyProduct();
                break;
            case R.id.nav_item_addProduct:
                activity = new ActivityAddProduct();
                break;
            case R.id.nav_item_category:
                fragment = new FragmentCategory();
                break;
            case R.id.nav_item_filter:
                fragment = new FragmentCategory();
                break;
            case R.id.nav_item_logout:
                logout();
                message = "Logged out!";
                clicked = true;
                break;
            case R.id.nav_item_login:
                activity = new ActivityLogin();
                message = "Logged in!";
                clicked = false;
                break;
            //Actionbar overflow menu items
            case R.id.action_logout:
                activity = new ActivitySignUp();
        }

        // Replace the fragment/activity
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.homeLayout, fragment)
                    .addToBackStack("")
                    .commit();
        } else if (activity != null) {
            Intent intent = new Intent(this, activity.getClass());
            startActivity(intent);
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        // Finally, close the drawer
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void logout() {
        SharedPrefManager.getInstance(this).logout();
        finish();
        startActivity(new Intent(this, ActivityHome.class));
    }

    private void initialize() {

        user = new ObjectUser();
        mToolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.homeAppBar_fab);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mNavigationView = findViewById(R.id.navigationView);
        navHeader = mNavigationView.getHeaderView(0);
        mNavMenu = mNavigationView.getMenu();
        fullName = navHeader.findViewById(R.id.navDrawerHeader_UserFullName);
        farmName = navHeader.findViewById(R.id.navDrawerHeader_UserFarmName);

    }
}
