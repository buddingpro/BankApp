package com.bankerwala.app;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bankerwala.app.R;

import java.text.DecimalFormat;

/**
 * Created by mosenthi on 13-Sep-16.
 */
public class Deposit_Cal_BaseFragment extends Fragment {


    EditText et, etInterest, etPeriod;
    String[] MonthsandYearSpinner, CompoundingSpinner, FDType, PeriodicalPayoutSpinnerData;
    Button reset, calculate, chart, details, chartpayout, sharepayout;
    View resultLayout, PeriodicalVIew, resultview, resultperiodical;
    DecimalFormat displayformat;
    Spinner spinner, compoundingSpinner;
    String principal, interest, period, periodType, compoundingType;
    ScrollView scrollViewMain;
    ImageView share;
    TextView maturityResult, interestEarned, interestPercentEarned, PeriodicalPayLabelTextView, PeriodicalPayValueText;
    public final String FINDLAYOUTVISIBILITY = "FINDLAYOUTVISIBILITY";
    public final String taglaunch = "TAGLAUNCH";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    public ArrayAdapter<String> customsAdapter(String[] data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_support_textview, data);
        adapter.setDropDownViewResource(R.layout.spinner_support);

        return adapter;
    }

    public void commonCreateView(View.OnClickListener buttonListener, DecimalFormat[] decimalFormats) {

        //hiding the layout
        resultLayout = (View) getActivity().findViewById(R.id.ResultView);
        resultLayout.setVisibility(View.GONE);

        //initializing edit texts to get the string

        et = (EditText) getActivity().findViewById(R.id.getPrincipal_editText);
        et.addTextChangedListener(new EditTextListnerCLass(et, decimalFormats[0], decimalFormats[1]));
        etInterest = (EditText) getActivity().findViewById(R.id.getInterest_editText);
        etInterest.addTextChangedListener(new EditTextListnerCLass(etInterest, decimalFormats[0], decimalFormats[1]));
        etPeriod = (EditText) getActivity().findViewById(R.id.getPeroid_editText);


        //initializing Spinners to get the string
        SpinnerClass spinnerlistner = new SpinnerClass(resultLayout);
        spinner = (Spinner) getActivity().findViewById(R.id.spinnerMonthsandYear);
        spinner.setAdapter(customsAdapter(MonthsandYearSpinner));
        spinner.setOnTouchListener(spinnerlistner);
        spinner.setOnItemSelectedListener(spinnerlistner);

        compoundingSpinner = (Spinner) getActivity().findViewById(R.id.compoundingspinner);
        compoundingSpinner.setAdapter(customsAdapter(CompoundingSpinner));
        compoundingSpinner.setOnTouchListener(spinnerlistner);
        compoundingSpinner.setOnItemSelectedListener(spinnerlistner);


        //initializing Buttons to Trigger the action
        reset = (Button) getActivity().findViewById(R.id.ClearButton);
        reset.setOnClickListener(buttonListener);
        calculate = (Button) getActivity().findViewById(R.id.CalculateButton);
        calculate.setOnClickListener(buttonListener);
        chart = (Button) getActivity().findViewById(R.id.GetGraph);
        chart.setOnClickListener(buttonListener);
        share = (ImageView) getActivity().findViewById(R.id.share);
        share.setOnClickListener(buttonListener);
        details = (Button) getActivity().findViewById(R.id.GetDetails);
        details.setOnClickListener(buttonListener);


    }

    @Override
    public void onPause() {
        super.onPause();
        ((Appdrawer) getActivity()).hideSoftKeyboard();
        ((Appdrawer) getActivity()).navigate();
        ((Appdrawer) getActivity())
                .setActionBarTitle(getString(R.string.app_name));
        Log.d(taglaunch, "onPause - Deposit_Cal_BaseFragment");
    }


}
