package com.bankerwala.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bankerwala.app.EMICalculator.EMI_Cal_Fragment;

/**
 * Created by mosenthi on 13-Sep-16.
 */
public class Calculator_List_Fragment extends Fragment implements SearchView.OnQueryTextListener {
    private final String taglaunch = "TAGLAUNCH";
    ListView listview;
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(taglaunch, "on Create - Calculator_List_Fragment");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.fd_list_menu, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(getActivity().SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Search Calculators...");


    }

    public void onResume() {
        super.onResume();
        Log.d(taglaunch, "onResume - Calculator_List_Fragment");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Log.d(taglaunch, "Search Icon Clicked from fragment");
            searchView.setOnQueryTextListener(this);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(taglaunch, "onCreateView - Calculator_List_Fragment");
        return inflater.inflate(R.layout.cal_list_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        listview = (ListView) getActivity().findViewById(R.id.CalculatorListView);
        listview.setOnItemClickListener(new SettingListeners());
        listview.setAdapter(new CustomAdapterCalculatorList(getActivity()));


        Log.d(taglaunch, "onViewCreated - Calculator_List_Fragment");
    }


    public class SettingListeners implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView textView = (TextView) view.findViewById(R.id.BankHeaderTitle);
            String text = textView.getText().toString().trim();
            Fragment fragment = null;
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            switch (text) {

                case "Fixed Deposit Calculator":
                    Log.d(taglaunch, "Switching to FD Calculator Fragment");
                    fragment = new FD_Cal_Fragment();
                    fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,
                            R.animator.slide_out_right, R.animator.exit_to_left, R.animator.exit_to_right);
                    fragmentTransaction.replace(R.id.app_bar, fragment).addToBackStack(null).commit();
                    break;

                case "Recurring Deposit Calculator":
                    Log.d(taglaunch, "Switching to RD Calculator Fragment");
                    fragment = new RD_Cal_Fragment();
                    fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,
                            R.animator.slide_out_right, R.animator.exit_to_left, R.animator.exit_to_right);
                    fragmentTransaction.replace(R.id.app_bar, fragment).addToBackStack(null).commit();
                    break;

                case "EMI Calculator":
                    Log.d(taglaunch, "Switching to RD Calculator Fragment");
                    fragment = new EMI_Cal_Fragment();
                    fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,
                            R.animator.slide_out_right, R.animator.exit_to_left, R.animator.exit_to_right);
                    fragmentTransaction.replace(R.id.app_bar, fragment).addToBackStack(null).commit();
                    break;


            }

        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        ((CustomAdapterCalculatorList) listview.getAdapter()).getFilter().filter(newText);

        return true;


    }


}
