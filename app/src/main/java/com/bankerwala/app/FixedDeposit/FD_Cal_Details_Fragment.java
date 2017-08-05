package com.bankerwala.app.FixedDeposit;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.bankerwala.app.Appdrawer;
import com.bankerwala.app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by MohanRaj_Senthilnath on 4/15/2017.
 */

public class FD_Cal_Details_Fragment extends Fragment {

    private final String taglaunch = "TAGLAUNCH";


    String investmentType, principal, period, periodType, compoundingType, interest, maturity, interestEarned, returnsPercent,
            PeriodicalPayout;
    int duration;

    TextView textViewPrincipalValue, textViewInterestRateValue, textViewFDTypeValue, textViewPeriodValue, textViewCompoundingValue,
            textViewMaturityValue, textViewPeriodicalPayout, textViewPeriodicalPayoutValue, textViewInterestEarnedValue, textviewreturnsvalue;


    EditText editTextStartDate,editTextEndDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_fd_calculator_details, container, false);


    }

    @Override
    public void onResume() {

        View resultLayout = (View) getActivity().findViewById(R.id.TopViewLinearLayout);
        resultLayout.setVisibility(View.GONE);
        resultLayout = (View) getActivity().findViewById(R.id.ResultView);
        resultLayout.setVisibility(View.GONE);


        Log.d(taglaunch, "Save Fragment onResume");
        super.onResume();
        ((Appdrawer) getActivity()).hideSoftKeyboard();

    }

    @Override
    public void onPause() {

        View resultLayout = (View) getActivity().findViewById(R.id.TopViewLinearLayout);
        resultLayout.setVisibility(View.VISIBLE);
        resultLayout = (View) getActivity().findViewById(R.id.ResultView);
        resultLayout.setVisibility(View.VISIBLE);

        Log.d(taglaunch, "Save Fragment onPause");
        super.onPause();
        ((Appdrawer) getActivity()).hideSoftKeyboard();

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        collectInfo();
        initializeWidgets();
        assignValue();
        updatePeriodicalPayoutLayout();
    }

    private void collectInfo() {
        Bundle bundle = getArguments();

        principal = bundle.getString("PrincipalValue");
        interest = bundle.getString("interest");
        investmentType = bundle.getString("investmentType");
        period = bundle.getString("period");
        periodType = bundle.getString("periodType");
        compoundingType = bundle.getString("compoundingType");
        maturity = bundle.getString("MaturityValue");
        PeriodicalPayout = bundle.getString("PeriodicalPayout");
        interestEarned = bundle.getString("InterestEarned");
        returnsPercent = bundle.getString("returnsPercent");

    }


    private void initializeWidgets() {

        textViewPrincipalValue = (TextView) getActivity().findViewById(R.id.textView_PrincipalValue);
        textViewInterestRateValue = (TextView) getActivity().findViewById(R.id.textView_InterestValue);
        textViewFDTypeValue = (TextView) getActivity().findViewById(R.id.textViewFDTypeValue);
        textViewPeriodValue = (TextView) getActivity().findViewById(R.id.textViewPeriodValue);
        textViewCompoundingValue = (TextView) getActivity().findViewById(R.id.textViewCompoundingValue);
        textViewMaturityValue = (TextView) getActivity().findViewById(R.id.textViewMaturityvalue);
        textViewPeriodicalPayout = (TextView) getActivity().findViewById(R.id.textViewPeriodicalPayout);
        textViewPeriodicalPayoutValue = (TextView) getActivity().findViewById(R.id.textViewPeriodicalPayoutValue);
        textViewInterestEarnedValue = (TextView) getActivity().findViewById(R.id.textViewInterestEarnedValue);
        textviewreturnsvalue = (TextView) getActivity().findViewById(R.id.textViewReturnsValue);

        editTextStartDate = (EditText) getActivity().findViewById(R.id.editTextStartDateValue);
        editTextStartDate.setOnClickListener(new DateListner());

        editTextEndDate = (EditText) getActivity().findViewById(R.id.editTextEndDateValue);

        updateLabel();
        updateMaturityDate(period,periodType);
    }


    private void assignValue() {

        textViewPrincipalValue.setText(": " + principal);
        textViewInterestRateValue.setText(": " + interest + "%");
        textViewFDTypeValue.setText(": " + investmentType);
        textViewPeriodValue.setText(": " + period + " " + periodType);
        textViewCompoundingValue.setText(": " + compoundingType);
        textViewMaturityValue.setText(": " + maturity);
        textViewInterestEarnedValue.setText(": " + interestEarned);
        textviewreturnsvalue.setText(": " + returnsPercent);

    }

    private void updatePeriodicalPayoutLayout() {

        if (investmentType.equals(EFixedDepositType.REINVESTMENT.getFdType()) || investmentType.equals(EFixedDepositType.SHORT_TERM.getFdType())) {
            View linearLayoutPeriodicalPayout = (View) getActivity().findViewById(R.id.linearLayoutPeriodicalPayout);
            linearLayoutPeriodicalPayout.setVisibility(View.GONE);
        } else {
            View linearLayoutPeriodicalPayout = (View) getActivity().findViewById(R.id.linearLayoutPeriodicalPayout);
            linearLayoutPeriodicalPayout.setVisibility(View.VISIBLE);

            textViewPeriodicalPayout.setText(investmentType);
            textViewPeriodicalPayoutValue.setText(": " + PeriodicalPayout);
        }
    }


    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendarEndDate = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
            updateMaturityDate(period,periodType);
        }

    };


    private void updateMaturityDate(String period, String PeriodType){

        int periodType = Calendar.YEAR;

        switch (PeriodType){

            case "Month(s)":
                periodType = Calendar.MONTH;
                break;

            case "Day(s)":
                periodType = Calendar.DATE;
                break;


        }

        myCalendarEndDate = (Calendar)myCalendar.clone();

        myCalendarEndDate.add(periodType, Integer.parseInt(period));
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTextEndDate.setText(": "+sdf.format(myCalendarEndDate.getTime()));

    }

    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTextStartDate.setText(": "+sdf.format(myCalendar.getTime()));
    }

    public class DateListner implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            new DatePickerDialog(getContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

}
