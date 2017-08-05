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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bankerwala.app.FixedDeposit.CreateTableLayoutFD;
import com.bankerwala.app.FixedDeposit.EDepositCompounding;
import com.bankerwala.app.FixedDeposit.EDepositPeriods;
import com.bankerwala.app.FixedDeposit.EFixedDepositType;
import com.bankerwala.app.FixedDeposit.FD_Cal_Details_Fragment;

import java.text.DecimalFormat;

/**
 * Created by mosenthi on 13-Sep-16.
 */
public class FD_Cal_Fragment extends Deposit_Cal_BaseFragment {
    double interestEarnedPercents;
    Spinner FDTypeSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(taglaunch, "onCreateView - FD_Cal_Fragment");

        return inflater.inflate(R.layout.fragment_fd_calculator_homepage, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Log.d(FINDLAYOUTVISIBILITY, "CalCulator Called after coming back from chart");

        super.onViewCreated(view, savedInstanceState);

        //Decimal Format for Principal Edit Text
        DecimalFormat df = new DecimalFormat("#,##,###.##");
        df.setDecimalSeparatorAlwaysShown(true);
        DecimalFormat dfnd = new DecimalFormat("#,##,###");


        ButtonListener buttonListener = new ButtonListener();
        FDType = new String[]{EFixedDepositType.REINVESTMENT.toString(), EFixedDepositType.QUARTERLY_PAYOUT.toString(), EFixedDepositType.MONTHLY_PAYOUT.toString(), EFixedDepositType.SHORT_TERM.toString()};
        MonthsandYearSpinner = new String[]{EDepositPeriods.YEAR.toString(), EDepositPeriods.MONTH.toString()};
        CompoundingSpinner = new String[]{EDepositCompounding.QUARTERLY.toString(), EDepositCompounding.MONTHLY.toString(), EDepositCompounding.HALF_YEARLY.toString(), EDepositCompounding.YEARLY.toString(), EDepositCompounding.SIMPLE_INTEREST.toString()};
        PeriodicalPayoutSpinnerData = new String[]{EDepositPeriods.DAY.toString()};
        DecimalFormat[] decimalFormats = {df, dfnd};

        commonCreateView(buttonListener, decimalFormats);

        SpinnerClass spinnerlistner = new SpinnerClass(getActivity(), resultLayout, spinner, MonthsandYearSpinner, PeriodicalPayoutSpinnerData);
        FDTypeSpinner = (Spinner) getActivity().findViewById(R.id.FDTypespinner);
        FDTypeSpinner.setAdapter(customsAdapter(FDType));
        FDTypeSpinner.setOnTouchListener(spinnerlistner);
        FDTypeSpinner.setOnItemSelectedListener(spinnerlistner);

        chartpayout = (Button) getActivity().findViewById(R.id.GetGraphForPeriodical);
        chartpayout.setOnClickListener(buttonListener);
        sharepayout = (Button) getActivity().findViewById(R.id.shareForPeriodical);
        sharepayout.setOnClickListener(buttonListener);


        Button buttonSave =(Button) getActivity().findViewById(R.id.buttonSaveDetail);
        buttonSave.setOnClickListener(buttonListener);

        Button buttonView = (Button) getActivity().findViewById(R.id.buttonViewDetail);
        buttonView.setOnClickListener(buttonListener);


        Log.d(FINDLAYOUTVISIBILITY, "Obtained " + et.getText().toString() + " " + etInterest.getText().toString());

        Log.d(taglaunch, "onViewCreated - FD_Cal_Fragment");
    }


    @Override
    public void onResume() {
        super.onResume();
        String temp = getString(R.string.fixed_deposit_calculator);

        ((Appdrawer) getActivity())
                .setActionBarTitle(temp);
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

        Log.d(taglaunch, "onResume - FD_Cal_Fragment");

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

            Double maturity = MathCalculations.CalculateFixedDeposit(Double.parseDouble(principal), Double.parseDouble(interest), Double.parseDouble(period), periodType, compoundingType);
            Log.d(taglaunch, "Maturity Value " + maturity);
            maturityResult = (TextView) getActivity().findViewById(R.id.MaturityAmountResult);
            interestEarned = (TextView) getActivity().findViewById(R.id.InterestEarnedResult);
            interestPercentEarned = (TextView) getActivity().findViewById(R.id.InterestEarnedPercentResult);

            displayformat = new DecimalFormat("#,##,###.##");


            displayformat.setMinimumFractionDigits(2);
            maturityResult.setText(displayformat.format(maturity).toString());
            Double interestEarnedvalue = maturity - Double.parseDouble(principal);
            interestEarned.setText(displayformat.format(interestEarnedvalue).toString());

            interestEarnedPercents = (interestEarnedvalue / Double.parseDouble(principal)) * 100;
            interestPercentEarned.setText(displayformat.format(interestEarnedPercents).toString() + " %");

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


    public void calculateIntervalPayout(String payoutType) {

        principal = et.getText().toString().replace(",", "");
        interest = etInterest.getText().toString().replace("%", "").replace(",", "");
        period = etPeriod.getText().toString().replace("%", "").replace(",", "");
        periodType = spinner.getSelectedItem().toString().trim();
        compoundingType = compoundingSpinner.getSelectedItem().toString().trim();

        Log.d(taglaunch, "Content" + principal + " " + interest + " " + period + periodType + " " + compoundingType);

        if (principal.length() >= 1 && interest.length() >= 1 && period.length() >= 1) {

            PeriodicalPayLabelTextView = (TextView) getActivity().findViewById(R.id.PeriodicalPay);
            PeriodicalPayValueText = (TextView) getActivity().findViewById(R.id.PeriodicalPayEditText);


            ((Appdrawer) getActivity()).hideSoftKeyboard();

            Double divisor = 0d;
            Double mutltiplier = 0d;

            if (payoutType.equals(EFixedDepositType.QUARTERLY_PAYOUT.toString())) {

                PeriodicalPayLabelTextView.setText(EFixedDepositType.QUARTERLY_PAYOUT.toString());

                divisor = 3d;

                if (periodType.equals(EDepositPeriods.YEAR.toString()))
                    mutltiplier = (Double.parseDouble(period) * 12d) / 3d;
                else
                    mutltiplier = Double.parseDouble(period) / 3d;
            }
            if (payoutType.equals(EFixedDepositType.MONTHLY_PAYOUT.getFdType())) {

                PeriodicalPayLabelTextView.setText(EFixedDepositType.MONTHLY_PAYOUT.getFdType());

                divisor = 1d;

                if (periodType.equals(EDepositPeriods.YEAR.toString()))
                    mutltiplier = Double.parseDouble(period) * 12d;

                else
                    mutltiplier = Double.parseDouble(period);
            }

            Double periodicallyEarning = MathCalculations.CalculateFixedDeposit(Double.parseDouble(principal), Double.parseDouble(interest), divisor, EDepositPeriods.MONTH.toString(), compoundingType);
            periodicallyEarning = periodicallyEarning - Double.parseDouble(principal);
            Log.d(taglaunch, "periodicallyEarning Value " + periodicallyEarning);
            maturityResult = (TextView) getActivity().findViewById(R.id.MaturityAmountResult);
            interestEarned = (TextView) getActivity().findViewById(R.id.InterestEarnedResult);
            interestPercentEarned = (TextView) getActivity().findViewById(R.id.InterestEarnedPercentResult);

            displayformat = new DecimalFormat("#,##,###.##");
            displayformat.setMinimumFractionDigits(2);

            PeriodicalPayValueText.setText(displayformat.format(periodicallyEarning).toString());
            maturityResult.setText(displayformat.format(Double.parseDouble(principal)).toString());
            Double interestEarnedvalue = periodicallyEarning * mutltiplier;
            interestEarned.setText(displayformat.format(interestEarnedvalue).toString());

            interestEarnedPercents = (interestEarnedvalue / Double.parseDouble(principal)) * 100;
            interestPercentEarned.setText(displayformat.format(interestEarnedPercents).toString() + " %");

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


    public void DisplayChart() {
        Log.d(taglaunch, "Switching to Chart Fragment");
        Fragment fragments = new CreatePieChartActivity();
        Bundle bundle = new Bundle();
        bundle.putString("TYPE", "FD");
        bundle.putString("MaturityValue", maturityResult.getText().toString());
        bundle.putString("PrincipalValue", displayformat.format(Double.parseDouble(principal)).toString());
        bundle.putString("InterestEarned", interestEarned.getText().toString());

        fragments.setArguments(bundle);
        FragmentTransaction fragmentTransactions = getFragmentManager().beginTransaction();
        fragmentTransactions.setCustomAnimations(R.animator.slide_in_left,
                R.animator.slide_out_right, R.animator.exit_to_left, R.animator.exit_to_right);
        fragmentTransactions.add(R.id.app_bar, fragments, "My Fragment").addToBackStack("My Fragment").commit();


    }

    public void DisplayChartForperiodicalPayout() {
        Log.d(taglaunch, "Switching to Chart Fragment");
        Fragment fragments = new CreatePieChartActivity();
        Bundle bundle = new Bundle();
        bundle.putString("TYPE", "FD");
        bundle.putString("MaturityValue", displayformat.format(Double.parseDouble(principal.replace(",", "")) + Double.parseDouble(interestEarned.getText().toString().replace(",", ""))));
        bundle.putString("PrincipalValue", displayformat.format(Double.parseDouble(principal)).toString());
        bundle.putString("InterestEarned", interestEarned.getText().toString());

        fragments.setArguments(bundle);
        FragmentTransaction fragmentTransactions = getFragmentManager().beginTransaction();
        fragmentTransactions.setCustomAnimations(R.animator.slide_in_left,
                R.animator.slide_out_right, R.animator.exit_to_left, R.animator.exit_to_right);
        fragmentTransactions.add(R.id.app_bar, fragments, "My Fragment").addToBackStack("My Fragment").commit();


    }


    private void diplayTable() {
        Log.d(taglaunch, "Switching to Detail Fragment");
        Fragment fragments = new CreateTableLayoutFD();

        Bundle bundle = new Bundle();
        bundle.putString("TYPE", "FD");
        bundle.putString("PrincipalValue", displayformat.format(Double.parseDouble(principal)).toString());
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

    private void saveDetails(){

        Log.d(taglaunch, "Launching Save");
        Fragment fragments = new FD_Cal_Details_Fragment();

        Bundle bundle = new Bundle();

        bundle.putString("PrincipalValue", displayformat.format(Double.parseDouble(principal)).toString());
        bundle.putString("interest", interest.toString());
        bundle.putString("investmentType", FDTypeSpinner.getSelectedItem().toString().trim());
        bundle.putString("period", period.toString());
        bundle.putString("periodType", periodType.toString());
        bundle.putString("compoundingType", compoundingType.toString());
        bundle.putString("MaturityValue", maturityResult.getText().toString());

        if((FDTypeSpinner.getSelectedItem().toString().trim().equals(EFixedDepositType.QUARTERLY_PAYOUT.getFdType()) || FDTypeSpinner.getSelectedItem().toString().trim().equals(EFixedDepositType.MONTHLY_PAYOUT.getFdType())))
             bundle.putString("PeriodicalPayout",PeriodicalPayValueText.getText().toString() );


        bundle.putString("InterestEarned", interestEarned.getText().toString());
        bundle.putString("returnsPercent", displayformat.format(interestEarnedPercents).toString() + " %");
        fragments.setArguments(bundle);

        FragmentTransaction fragmentTransactions = getFragmentManager().beginTransaction();
        fragmentTransactions.setCustomAnimations(R.animator.slide_in_left,
                R.animator.slide_out_right, R.animator.exit_to_left, R.animator.exit_to_right);
        fragmentTransactions.add(R.id.app_bar,fragments ).addToBackStack(null).commit();
    }

    public String shareString() {

        String share = "Fixed Deposit Details\n----------------------------------" + "\nPrincipal : " + displayformat.format(Double.parseDouble(principal)) + " \n" + "Interest Rate : "
                + etInterest.getText().toString() + "% \nFD Type : " + FDTypeSpinner.getSelectedItem().toString().trim() + "\nDuration : " + etPeriod.getText().toString() + " " + periodType + "\nCompounding : " + compoundingType + "\n\nReturns would be,\n\nMaturity Value : " +
                maturityResult.getText().toString() + "\nInterest Earned : " + interestEarned.getText().toString() + "\nReturns Percentage : " + displayformat.format(interestEarnedPercents).toString() + "%";

        return share;
    }

    public String shareStringPeriodicalPayout() {

        String share = "Fixed Deposit Details\n----------------------------------" + "\nPrincipal : " + displayformat.format(Double.parseDouble(principal)) + " \n" + "Interest Rate : "
                + etInterest.getText().toString() + "%\nFD Type : " + FDTypeSpinner.getSelectedItem().toString().trim() + "\nDuration : " + etPeriod.getText().toString() + " " + periodType + "\nCompounding : " + compoundingType + "\n\nReturns would be,\n\nMaturity Value : " +
                maturityResult.getText().toString() + "\n" + PeriodicalPayLabelTextView.getText().toString() + " : " + PeriodicalPayValueText.getText().toString() + "\nInterest Earned : " + interestEarned.getText().toString() + "\nReturns Percentage : " + displayformat.format(interestEarnedPercents).toString() + "%";

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
                    if (FDTypeSpinner.getSelectedItem().toString().trim().equals(EFixedDepositType.REINVESTMENT.toString()) || FDTypeSpinner.getSelectedItem().toString().trim().equals(EFixedDepositType.SHORT_TERM.toString())) {

                        PeriodicalVIew = (View) getActivity().findViewById(R.id.PerodicalPayLayout);
                        PeriodicalVIew.setVisibility(View.GONE);

                        if (FDTypeSpinner.getSelectedItem().toString().trim().equals(EFixedDepositType.REINVESTMENT.toString())) {
                            resultperiodical = (View) getActivity().findViewById(R.id.PostButtonsResutlsForPeriodical);
                            resultperiodical.setVisibility(View.GONE);

                            resultview = (View) getActivity().findViewById(R.id.PostButtonsResutls);
                            resultview.setVisibility(View.VISIBLE);
                        }
                        if (FDTypeSpinner.getSelectedItem().toString().trim().equals(EFixedDepositType.SHORT_TERM.toString())) {
                            resultperiodical = (View) getActivity().findViewById(R.id.PostButtonsResutlsForPeriodical);
                            resultperiodical.setVisibility(View.VISIBLE);

                            resultview = (View) getActivity().findViewById(R.id.PostButtonsResutls);
                            resultview.setVisibility(View.GONE);
                        }

                        calculate();


                    } else {
                        calculateIntervalPayout(FDTypeSpinner.getSelectedItem().toString().trim());
                        resultview = (View) getActivity().findViewById(R.id.PostButtonsResutls);
                        resultview.setVisibility(View.GONE);
                        resultperiodical = (View) getActivity().findViewById(R.id.PostButtonsResutlsForPeriodical);
                        resultperiodical.setVisibility(View.VISIBLE);
                        PeriodicalVIew = (View) getActivity().findViewById(R.id.PerodicalPayLayout);
                        PeriodicalVIew.setVisibility(View.VISIBLE);
                    }
                    break;

                case R.id.GetGraph:
                    Log.d(taglaunch, "Chart Button Clicked");
                    DisplayChart();
                    break;

                case R.id.share:
                    Log.d(taglaunch, "Launching share");
                    new ShareContent(shareString(), getActivity()).shareData("Fixed Deposit Calculations by Banker Wala");
                    break;

                case R.id.GetDetails:
                    Log.d(taglaunch, "Launching Details");
                    details.setText("Creating...");
                    diplayTable();
                    break;

                case R.id.GetGraphForPeriodical:
                    Log.d(taglaunch, "Chart Button Clicked");
                    if (FDTypeSpinner.getSelectedItem().toString().trim().equals(EFixedDepositType.SHORT_TERM.toString()))
                        DisplayChart();
                    else
                        DisplayChartForperiodicalPayout();
                    break;

                case R.id.shareForPeriodical:
                    Log.d(taglaunch, "Launching share");
                    if (FDTypeSpinner.getSelectedItem().toString().trim().equals(EFixedDepositType.SHORT_TERM.toString()))
                        new ShareContent(shareString(), getActivity()).shareData("Fixed Deposit Calculations by Banker Wala");
                    else
                        new ShareContent(shareStringPeriodicalPayout(), getActivity()).shareData("Fixed Deposit Calculations by Banker Wala");
                    break;

                case R.id.buttonSaveDetail:
                    saveDetails();
                    break;


                case R.id.buttonViewDetail:
                    Log.d(taglaunch, "Launching View");
                    Toast.makeText(getContext(),"Clicked view",Toast.LENGTH_SHORT).show();
                    break;


            }
        }
    }

}
