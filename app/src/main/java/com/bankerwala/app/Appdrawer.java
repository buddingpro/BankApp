package com.bankerwala.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

public class Appdrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener {

    public final String interestType = "interestType";
    public final String fd = "fd";
    public final String rd = "rd";
    public final String BankName = "BankName";
    private final String taglaunch = "TAGLAUNCH";
    private final String FINDLAYOUTVISIBILITY = "FINDLAYOUTVISIBILITY";
    protected DrawerLayout drawer;
    ListView listview;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    String display;
    ViewPager viewPager;
    TabLayout tabLayout;
    MainActivityAdapter mainActivityAdapter;

    private FragmentManager.OnBackStackChangedListener backStackListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            setNavIcon();
        }

        ;
    };

    public String getText() {
        return display;
    }

    public void setText(String display) {
        this.display = display;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(taglaunch, "on Create - AppDrawer");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appdrawer);
        mainActivityAdapter = new MainActivityAdapter(getFragmentManager());
        navigate();
        switchtofragment();
        getFragmentManager().addOnBackStackChangedListener(backStackListener);

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(taglaunch, "onResume - AppDrawer");
    }


    public void switchtofragment() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("TAB1"));
        tabLayout.addTab(tabLayout.newTab().setText("TAB2"));
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(mainActivityAdapter);
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);

    }


    public void setActionBarTitle(String title) {

        getSupportActionBar().setTitle(title);
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void navigate() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void dialogBox() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Disclaimer");
        alertDialogBuilder.setMessage(getString(R.string.disclaimer));
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }

    public void setDefault() {
        drawer.openDrawer(GravityCompat.START);
    }


    public void setNavIcon() {
        int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
        toggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (toggle.isDrawerIndicatorEnabled() && toggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        if (id == R.id.action_search) {
            return false;
        } else if (id == R.id.action_popOut) {
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {
            new ShareContent(this).shareApp();
        } else if (id == R.id.nav_rate) {
            launchMarket();
        } else if (id == R.id.nav_aboutus) {
            createAboutUs();
        } else if (id == R.id.Disclaimer) {
            dialogBox();
        } else if (id == R.id.nav_help) {
            createHelp();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setFragmentNavUp() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
    }

    private void createAboutUs() {

        Fragment fragment = new AboutUs_Fragment();
        if (getVisibleFragment("AboutUS_Fragment")) {

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Log.d(taglaunch, "Switching to AboutUS Fragment");

            fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,
                    R.animator.slide_out_right, R.animator.exit_to_left, R.animator.exit_to_right);
            fragmentTransaction.replace(R.id.app_bar, fragment, "AboutUS_Fragment").addToBackStack(null).commit();
        }


    }

    private void createHelp() {

        Fragment fragment = new Help_Fragment();
        if (getVisibleFragment("Help_Fragment")) {

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Log.d(taglaunch, "Switching to Help Fragment");

            fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,
                    R.animator.slide_out_right, R.animator.exit_to_left, R.animator.exit_to_right);
            fragmentTransaction.replace(R.id.app_bar, fragment, "Help_Fragment").addToBackStack(null).commit();
        }
    }


    public boolean getVisibleFragment(String fragmentTagName) {
        Fragment currentFragment = getFragmentManager().findFragmentByTag(fragmentTagName);

        if (currentFragment != null && currentFragment.isVisible()) {
            return false;
        }
        return true;
    }


    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    class MainActivityAdapter extends FragmentStatePagerAdapter {

        public ArrayList<Fragment> fragments = new ArrayList<Fragment>();

        public MainActivityAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = new String();

            switch (position) {
                case 0:
                    title = "Calculators";
                    break;

                case 1:
                    title = "FD Rates";
                    break;

            }

            return title;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public Fragment getItem(int position) {

            Fragment fragments = null;

            switch (position) {
                case 0:
                    fragments = new Calculator_List_Fragment();
                    break;


                case 1:

                    fragments = new BanksList_FD_InterestRates_Fragment();
                    break;

                default:
                    return null;
            }

            return fragments;
        }

    }

}
