package com.bankerwala.app.EMICalculator;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bankerwala.app.Appdrawer;
import com.bankerwala.app.CreatePieChartActivity;
import com.bankerwala.app.EditTextListnerCLass;
import com.bankerwala.app.MathCalculations;
import com.bankerwala.app.R;
import com.bankerwala.app.ShareContent;
import com.bankerwala.app.SpinnerClass;

import java.text.DecimalFormat;

/**
 * Created by mosenthi on 31-Oct-16.
 */
public class EMI_Cal_Fragment extends Fragment {
    double monthlyEMI, interestPayable, totalAmountPayable;
    private final String taglaunch = "TAGLAUNCH";
    Spinner spinner, compoundingSpinner;
    View resultLayout;
    EditText et, etInterest, etPeriod;
    String[] MonthsandYearSpinner;
    DecimalFormat df, dfnd;
    Button reset, calculate, chart, details;
    ImageView share;
    String principal, interest, period, periodType;
    TextView emiView, interestSpent, totalPayment;
    ScrollView scrollViewMain;
    MathCalculations mathCalculations = new MathCalculations();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_emi_calculator_homepage, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        df = new DecimalFormat("#,##,###.##");
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat("#,##,###");


        resultLayout = (View) getActivity().findViewById(R.id.ResultView);
        resultLayout.setVisibility(View.GONE);

        MonthsandYearSpinner = new String[]{"Year(s)", "Month(s)"};
        SpinnerClass spinnerlistner = new SpinnerClass(resultLayout);

        spinner = (Spinner) getActivity().findViewById(R.id.spinnerMonthsandYear);
        spinner.setAdapter(customsAdapter(MonthsandYearSpinner));
        spinner.setOnTouchListener(spinnerlistner);
        spinner.setOnItemSelectedListener(spinnerlistner);

        et = (EditText) getActivity().findViewById(R.id.getPrincipal_editText);
        et.addTextChangedListener(new EditTextListnerCLass(et, df, dfnd));
        etInterest = (EditText) getActivity().findViewById(R.id.getInterest_editText);
        etInterest.addTextChangedListener(new EditTextListnerCLass(etInterest, df, dfnd));
        etPeriod = (EditText) getActivity().findViewById(R.id.getPeroid_editText);

        ButtonListener buttonListener = new ButtonListener();

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

        scrollViewMain = (ScrollView) getActivity().findViewById(R.id.scrollViewMain);

        emiView = (TextView) getActivity().findViewById(R.id.MaturityAmountResult);
        interestSpent = (TextView) getActivity().findViewById(R.id.InterestEarnedResult);
        totalPayment = (TextView) getActivity().findViewById(R.id.InterestEarnedPercentResult);


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


    @Override
    public void onResume() {
        super.onResume();
        String temp = getString(R.string.emi_calculator);

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


    }

    @Override
    public void onPause() {


        super.onPause();
        ((Appdrawer) getActivity()).hideSoftKeyboard();
        ((Appdrawer) getActivity()).navigate();
        ((Appdrawer) getActivity())
                .setActionBarTitle(getString(R.string.app_name));
        Log.d(taglaunch, "onPause - EMI_Cal_Fragment");
    }


    public void calculate() {
        principal = et.getText().toString().replace(",", "");
        interest = etInterest.getText().toString().replace("%", "").replace(",", "");
        period = etPeriod.getText().toString().replace("%", "").replace(",", "");
        periodType = spinner.getSelectedItem().toString().trim();

        Log.d(taglaunch, "principal " + principal + "interest " + interest + "period " + period + "periodType " + periodType);

        if (principal.length() >= 1 && interest.length() >= 1 && period.length() >= 1) {

            ((Appdrawer) getActivity()).hideSoftKeyboard(); //hiding the keyboard.

            monthlyEMI = mathCalculations.calculateEMI(Double.parseDouble(principal), Double.parseDouble(interest), Double.parseDouble(period), periodType);

            emiView.setText(df.format(monthlyEMI).toString());

            interestSpent.setText(df.format(totalPayment() - Double.parseDouble(principal)).toString());

            totalPayment.setText(df.format(totalPayment()).toString());


            resultLayout.setVisibility(View.VISIBLE);

            if (resultLayout.getVisibility() == View.VISIBLE)
                scrollViewMain.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollViewMain.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 100);


        } else {
            Toast.makeText(getActivity(), "Please Enter Values", Toast.LENGTH_SHORT).show();
        }

    }


    public Double totalPayment() {

        if (periodType.equals("Year(s)"))
            return Double.parseDouble(period) * 12 * monthlyEMI;

        else
            return Double.parseDouble(period) * monthlyEMI;
    }

    public void DisplayChart() {
        df.setMinimumFractionDigits(2);
        Log.d(taglaunch, "Switching to Chart Fragment");
        Fragment fragments = new CreatePieChartActivity();
        Bundle bundle = new Bundle();
        bundle.putString("TYPE", "EMI");
        bundle.putString("MaturityValue", totalPayment.getText().toString());
        bundle.putString("PrincipalValue", df.format(Double.parseDouble(principal)).toString());
        bundle.putString("InterestEarned", interestSpent.getText().toString());

        fragments.setArguments(bundle);
        FragmentTransaction fragmentTransactions = getFragmentManager().beginTransaction();
        fragmentTransactions.setCustomAnimations(R.animator.slide_in_left,
                R.animator.slide_out_right, R.animator.exit_to_left, R.animator.exit_to_right);
        fragmentTransactions.add(R.id.app_bar, fragments, "My Fragment").addToBackStack("My Fragment").commit();


    }

    public String shareString() {

        String share = "EMI Details\n----------------------------------" + "\nLoan Amount : " + et.getText().toString() + " \n" + "Interest Rate : "
                + etInterest.getText().toString() + "%\nLoan Duration : " + etPeriod.getText().toString() + " " + periodType + "\n\nEMI (Monthly Payment) : "
                + emiView.getText().toString() + "\nTotal Interest Payable : " + interestSpent.getText().toString() + "\nTotal Payment (Principal + Interest) : "
                + totalPayment.getText().toString();
        return share;
    }

    public void DisplayDetails() {

        Log.d(taglaunch, "Switching to Detail Fragment");
        Fragment fragments = new CreateTableLayoutEMI();

        Bundle bundle = new Bundle();
        bundle.putString("TYPE", "EMI");
        bundle.putString("EMIValue", emiView.getText().toString());
        bundle.putString("LoanValue", principal);
        bundle.putString("period", period.toString());
        bundle.putString("interest", interest.toString());
        bundle.putString("periodType", periodType.toString());
        fragments.setArguments(bundle);


        FragmentTransaction fragmentTransactions = getFragmentManager().beginTransaction();
        fragmentTransactions.setCustomAnimations(R.animator.slide_in_left,
                R.animator.slide_out_right, R.animator.exit_to_left, R.animator.exit_to_right);
        fragmentTransactions.add(R.id.app_bar, fragments).addToBackStack(null).commit();


    }


    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.ClearButton:
                    Log.d(taglaunch, "Clear Button Clicked");
                    et.setText("");
                    etInterest.setText("");
                    etPeriod.setText("");
                    resultLayout.setVisibility(View.GONE);
                    break;


                case R.id.CalculateButton:
                    calculate();
                    break;

                case R.id.share:
                    Log.d(taglaunch, "Launching share");
                    new ShareContent(shareString(), getActivity()).shareData("EMI Calculations by Banker Wala");
                    break;

                case R.id.GetGraph:
                    Log.d(taglaunch, "Chart Button Clicked");
                    DisplayChart();
                    break;

                case R.id.GetDetails:
                    Log.d(taglaunch, "Launching Details");
                    details.setText("Creating...");
                    DisplayDetails();
                    break;
            }


        }
    }

}
