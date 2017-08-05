package com.bankerwala.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mosenthi on 24-Dec-16.
 */
public class AboutUs_Fragment extends Fragment {

    private final String taglaunch = "TAGLAUNCH";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(taglaunch, "onCreateView - AboutUs_Fragment");

        return inflater.inflate(R.layout.fragment_aboutus, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Log.d(taglaunch, "onViewCreated - AboutUs_Fragment");
    }


    @Override
    public void onResume() {
        super.onResume();
        String temp = getString(R.string.about);

        ((Appdrawer) getActivity())
                .setActionBarTitle(temp);
        ((Appdrawer) getActivity())
                .setNavIcon();
        Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Appdrawer) getActivity()).onSupportNavigateUp();
            }
        });

        Log.d(taglaunch, "onResume - AboutUs_Fragment");

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((Appdrawer) getActivity()).hideSoftKeyboard();
        ((Appdrawer) getActivity()).navigate();
        ((Appdrawer) getActivity())
                .setActionBarTitle(getString(R.string.app_name));
        Log.d(taglaunch, "onPause - AboutUs_Fragment");
    }

}
