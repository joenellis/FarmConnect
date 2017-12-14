package com.buah.farmconnect.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.buah.farmconnect.R;
import com.buah.farmconnect.fragment.FragmentCategory;
import com.buah.farmconnect.fragment.FragmentHome;
import com.buah.farmconnect.fragment.FragmentMore;
import com.buah.farmconnect.fragment.FragmentMyProduct;
import com.buah.farmconnect.fragment.FragmentMyWishList;
import com.buah.farmconnect.object.ObjectUser;
import com.buah.farmconnect.session.SharedPrefManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityHome extends AppCompatActivity {

    Menu mNavMenu;
    View navHeader;
    boolean clicked;
    ObjectUser user;
    Toolbar mToolbar;
    TextView fullName;
    TextView farmName;
    FloatingActionButton fab;
    DrawerLayout mDrawerLayout;
    CircleImageView userPicture;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialize widgets and components
        initialize();

        Intent intent = getIntent();
        String screen = intent.getStringExtra("Screen");

        if (screen != null) {

            setHomeFragment(screen);

        } else {

            setHomeFragment("Home");

        }

        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {

            mNavigationView.inflateMenu(R.menu.logged_in_nav_drawer_menu);

        } else {

            mNavigationView.inflateMenu(R.menu.logged_out_nav_drawer_menu);

        }


        String fullName = SharedPrefManager.getInstance(getApplicationContext()).getobjectUser().getFullname();
        String email = SharedPrefManager.getInstance(getApplicationContext()).getobjectUser().getEmail();


        this.fullName.setText(fullName);
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


    private void gotoActivity(AppCompatActivity activity) {

        startActivity(new Intent(this, activity.getClass()));

    }


    public void setHomeFragment(String TAG) {

        switch (TAG) {
            case "Home":
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.homeLayout, new FragmentHome(), TAG)
                        .addToBackStack(null)
                        .commit();
                break;

            case "Category":
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.homeLayout, new FragmentCategory(), TAG)
                        .addToBackStack(null)
                        .commit();
                break;

            case "More":
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.homeLayout, new FragmentMore(), TAG)
                        .addToBackStack(null)
                        .commit();
                break;

            case "MyProduct":
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.homeLayout, new FragmentMyProduct(), TAG)
                        .addToBackStack(null)
                        .commit();
                break;

            case "MyWishList":
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.homeLayout, new FragmentMyWishList(), TAG)
                        .addToBackStack(null)
                        .commit();
                break;

            default:
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.homeLayout, new FragmentHome())
                        .commit();
                break;

        }

    }


    private void displaySelectedScreen(@IdRes int itemId) {

        switch (itemId) {

            case R.id.nav_item_home:
                setHomeFragment("Home");
                break;

            case R.id.nav_item_products:
                setHomeFragment("MyProduct");
                break;

            case R.id.nav_item_category:
                setHomeFragment("Category");
                break;

            case R.id.nav_item_wishList:
                setHomeFragment("MyWishList");
                break;

            case R.id.nav_item_account:
                gotoActivity(new ActivityMyAccount());
                break;

            case R.id.nav_item_addProduct:
                gotoActivity(new ActivityAddProduct());
                break;

            case R.id.nav_item_login:
                gotoActivity(new ActivityLogin());
                clicked = false;
                break;

            case R.id.nav_item_logout:
                logout();
                clicked = true;
                break;

        }


        // Finally, close the drawer
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.closeDrawer(GravityCompat.START);

    }


    public void onAddProductClick(View view) {

        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {

            gotoActivity(new ActivityAddProduct());

        } else {

            Snackbar.make(
                    findViewById(R.id.homeAppBar_rootLayout),
                    "Sign In To Complete This Action.",
                    Snackbar.LENGTH_LONG

            ).setAction(
                    R.string.title_activity_login,
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            gotoActivity(new ActivityLogin());

                        }

                    }

            ).show();

        }
    }


    private void logout() {

        SharedPrefManager.getInstance(this).logout();
        finish();
        gotoActivity(new ActivityHome());

    }


    private void initialize() {

        user = new ObjectUser();
        mToolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.homeAppBar_fab);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        fullName = navHeader.findViewById(R.id.navDrawerHeader_UserFullName);
        farmName = navHeader.findViewById(R.id.navDrawerHeader_UserFarmName);

        mFragmentManager = getSupportFragmentManager();

        mNavigationView = findViewById(R.id.navigationView);
        navHeader = mNavigationView.getHeaderView(0);
        mNavMenu = mNavigationView.getMenu();

    }
}
