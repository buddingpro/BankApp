package com.bankerwala.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bankerwala.app.R;

/**
 * Created by mosenthi on 13-Sep-16.
 */
public class FragmentMenu extends Fragment implements View.OnClickListener {
    private final String taglaunch = "TAGLAUNCH";
    Button fdbutton, clbutton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(taglaunch, "Menu Fragment Created");
        return inflater.inflate(R.layout.fragment_menu_layout, container, false);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ((Appdrawer) getActivity())
                .setActionBarTitle(getString(R.string.app_name));


        fdbutton = (Button) getActivity().findViewById(R.id.fdButton);
        fdbutton.setOnClickListener(this);

        clbutton = (Button) getActivity().findViewById(R.id.CalButton);
        clbutton.setOnClickListener(this);

        Log.d(taglaunch, "activity created");
    }


    public void onResume() {
        super.onResume();

        Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Appdrawer) getActivity()).setDefault();
            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fdButton:
                Log.d(taglaunch, "Switching to FD Fragment");
                Fragment fragment = new BanksList_FD_InterestRates_Fragment();
                Bundle args = new Bundle();
                args.putString(((Appdrawer) getActivity()).interestType, ((Appdrawer) getActivity()).fd);
                fragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,
                        R.animator.slide_out_right,R.animator.exit_to_left, R.animator.exit_to_right);
                fragmentTransaction.replace(R.id.app_bar, fragment).addToBackStack(null).commit();
                break;
            case R.id.CalButton:
                Log.d(taglaunch, "Switching to Calculator Fragment");
                Fragment fragments = new Calculator_List_Fragment();
                FragmentTransaction fragmentTransactions = getFragmentManager().beginTransaction();
                fragmentTransactions.setCustomAnimations(R.animator.slide_in_left,
                        R.animator.slide_out_right,R.animator.exit_to_left, R.animator.exit_to_right);
                fragmentTransactions.replace(R.id.app_bar, fragments).addToBackStack(null).commit();
                break;
        }

    }
}
