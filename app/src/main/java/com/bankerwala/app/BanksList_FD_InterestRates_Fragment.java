package com.bankerwala.app;

import android.app.Fragment;
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
import android.widget.Toast;

import com.bankerwala.app.R;

/**
 * Created by mosenthi on 13-Sep-16.
 */
public class BanksList_FD_InterestRates_Fragment extends Fragment implements SearchView.OnQueryTextListener {
    private final String taglaunch = "TAGLAUNCH";
    ListView listview;
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(taglaunch, "on Create - BanksList_FD_InterestRates_Fragment");

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.fd_list_menu, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(getActivity().SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Search Banks...");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            searchView.setOnQueryTextListener(this);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(taglaunch, "on Create View - BanksList_FD_InterestRates_Fragment");

        return inflater.inflate(R.layout.fd_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Log.d(taglaunch, "onViewCreated - BanksList_FD_InterestRates_Fragment");

        super.onViewCreated(view, savedInstanceState);

        listview = (ListView) getActivity().findViewById(R.id.banksListView);
        listview.setOnItemClickListener(new SettingListeners());
        listview.setAdapter(new CustomAdapterBanksList(getActivity()));


    }



    public class SettingListeners implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Fragment fragment = new WebViewClass();
            String selected = ((TextView) view.findViewById(R.id.BankHeaderTitle)).getText().toString();
            Log.d(taglaunch, "List item clicked");
            Bundle args = new Bundle();
            args.putString(((Appdrawer) getActivity()).BankName, selected);
            args.putString(((Appdrawer) getActivity()).interestType, "fd");
            fragment.setArguments(args);
            if(((Appdrawer) getActivity()).isNetworkAvailable())
            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left,
                    R.animator.slide_out_right,R.animator.exit_to_left, R.animator.exit_to_right).replace(R.id.app_bar, fragment).addToBackStack(null).commit();
            else
                Toast.makeText(getActivity(), R.string.checkconnection, Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        ((CustomAdapterBanksList) listview.getAdapter()).getFilter().filter(newText);

        return true;


    }


}
