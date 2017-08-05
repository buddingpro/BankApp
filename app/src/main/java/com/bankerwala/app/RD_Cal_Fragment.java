package com.bankerwala.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bankerwala.app.RecurringDeposit.CreateTableLayoutRD;

import java.text.DecimalFormat;

/**
 * Created by mosenthi on 13-Sep-16.
 */
public class RD_Cal_Fragment extends Deposit_Cal_BaseFragment {

    double DepositAmount,maturity,interestEarnedvalue;
    TextView DepositAmountView;
    DecimalFormat df;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        df = new DecimalFormat("#,##,###.##");
        df.setMinimumFractionDigits(2);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_rd_calculator_homepage, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        //Decimal Format for Principal Edit Text
        DecimalFormat df = new DecimalFormat("#,##,###.##");
        df.setDecimalSeparatorAlwaysShown(true);
        DecimalFormat dfnd = new DecimalFormat("#,##,###");


        ButtonListener listener = new ButtonListener();

        DecimalFormat[] decimalFormats = {df, dfnd};

        MonthsandYearSpinner = new String[]{"Year(s)", "Month(s)"};
        CompoundingSpinner = new String[]{"Quarterly", "Monthly", "Half Yearly", "Yearly"};

        commonCreateView(listener, decimalFormats);


    }


    public void onResume() {
        super.onResume();
        String temp = getString(R.string.recurring_deposit_calculator);

        ((Appdrawer) getActivity())
                .setActionBarTitle(temp);
        ((Appdrawer) getActivity())
                .setNavIcon();
        ((Appdrawer) getActivity())
                .setFragmentNavUp();
    }


    @Override
    public void onPause() {


        super.onPause();
        ((Appdrawer) getActivity()).hideSoftKeyboard();



    }


    public void calculate() {
        principal = et.getText().toString().replace(",", "");
        interest = etInterest.getText().toString().replace("%", "").replace(",", "");
        period = etPeriod.getText().toString().replace("%", "").replace(",", "");
        periodType = spinner.getSelectedItem().toString().trim();
        compoundingType = compoundingSpinner.getSelectedItem().toString().trim();

        Log.d(taglaunch, "Content" + principal + " " + interest + " " + period + periodType + " " + compoundingType);

        if (principal.length() >= 1 && interest.length() >= 1 && period.length() >= 1) {

            ((Appdrawer) getActivity()).hideSoftKeyboard();
            maturity = MathCalculations.CalculateRecurringDeposit(Double.parseDouble(principal), Double.parseDouble(interest), Double.parseDouble(period), periodType, compoundingType);
            Log.d(taglaunch, "Maturity Value " + maturity);
            interestPercentEarned = (TextView) getActivity().findViewById(R.id.InterestEarnedPercentResult);
            maturityResult = (TextView) getActivity().findViewById(R.id.MaturityAmountResult);
            interestEarned = (TextView) getActivity().findViewById(R.id.InterestEarnedResult);
            DepositAmountView = (TextView) getActivity().findViewById(R.id.DepositResult);

            DepositAmount = MathCalculations.CalculateDeposit(Double.parseDouble(principal), Double.parseDouble(period), periodType);

            DepositAmountView.setText(df.format(DepositAmount));
            maturityResult.setText(df.format(maturity).toString());
            interestEarnedvalue = maturity - DepositAmount;
            interestEarned.setText(df.format(interestEarnedvalue).toString());

            double interestEarnedPercents = (interestEarnedvalue / DepositAmount) * 100;
            interestPercentEarned.setText(df.format(interestEarnedPercents).toString() + " %");


            resultLayout.setVisibility(View.VISIBLE);
            scrollViewMain = (ScrollView) getActivity().findViewById(R.id.scrollViewMain);

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

    public void DisplayDetails() {
        Log.d(taglaunch, "Switching to Detail Fragment");
        Fragment fragments = new CreateTableLayoutRD();

        Bundle bundle = new Bundle();
        bundle.putString("TYPE", "RD");
        bundle.putString("PrincipalValue", principal);
        bundle.putString("period", period.toString());
        bundle.putString("interest", interest.toString());
        bundle.putString("periodType", periodType.toString());
        bundle.putString("compoundingType", compoundingType.toString());
        fragments.setArguments(bundle);


        FragmentTransaction fragmentTransactions = getFragmentManager().beginTransaction();
        fragmentTransactions.setCustomAnimations(R.animator.slide_in_left,
                R.animator.slide_out_right, R.animator.exit_to_left, R.animator.exit_to_right);
        fragmentTransactions.add(R.id.app_bar, fragments).addToBackStack(null).commit();


    }
    public void DisplayChart() {
        Log.d(taglaunch, "Switching to Chart Fragment");
        Fragment fragments = new CreatePieChartActivity();
        Bundle bundle = new Bundle();
        bundle.putString("TYPE", "FD");
        bundle.putString("MaturityValue", maturityResult.getText().toString());
        bundle.putString("PrincipalValue", DepositAmountView.getText().toString());
        bundle.putString("InterestEarned", interestEarned.getText().toString());

        fragments.setArguments(bundle);
        FragmentTransaction fragmentTransactions = getFragmentManager().beginTransaction();
        fragmentTransactions.setCustomAnimations(R.animator.slide_in_left,
                R.animator.slide_out_right, R.animator.exit_to_left, R.animator.exit_to_right);
        fragmentTransactions.add(R.id.app_bar, fragments, "My Fragment").addToBackStack("My Fragment").commit();

    }

    public String shareString(){

        String  share = "Recurring Deposit Details\n-------------------------------------------" + "\nMonthly Deposit : "+df.format(Double.parseDouble(principal))+"\nDuration : "+etPeriod.getText().toString()+" "+periodType+"\nTotal Deposit : "+DepositAmountView.getText().toString()+ " \n"+ "Interest Rate : "
                +etInterest.getText().toString()+"%\nCompounding : "+compoundingType+"\n\nReturns would be,\n\nMaturity Value : " +
                maturityResult.getText().toString()+"\nInterest Earned : "+interestEarned.getText().toString()+"\nReturns Percentage : "+interestPercentEarned.getText().toString();

        return share;
    }


    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.ClearButton:
                    et.setText("");
                    Log.d(taglaunch, "Clear Button Clicked");
                    etInterest.setText("");
                    etPeriod.setText("");
                    resultLayout.setVisibility(View.GONE);
                    break;

                case R.id.CalculateButton:
                    Log.d(taglaunch, "Calculate Button Clicked");
                    calculate();
                    break;

                case R.id.GetGraph:
                    Log.d(taglaunch, "Chart Button Clicked");
                    DisplayChart();
                    break;

                case R.id.share:
                    Log.d(taglaunch,"Launching share");
                    new ShareContent(shareString(),getActivity()).shareData("Recurring Deposit Calculations by Banker Wala");
                    break;

                case R.id.GetDetails:
                    Log.d(taglaunch,"Launching Details");
                    details.setText("Creating...");
                    DisplayDetails();
                    break;


            }


        }
    }

}
