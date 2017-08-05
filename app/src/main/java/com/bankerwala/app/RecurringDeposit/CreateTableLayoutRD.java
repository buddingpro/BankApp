package com.bankerwala.app.RecurringDeposit;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import com.bankerwala.app.Appdrawer;
import com.bankerwala.app.CustomTableLayout;
import com.bankerwala.app.CustomTableLayoutMain;
import com.bankerwala.app.MathCalculations;
import com.bankerwala.app.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by mosenthi on 10-Oct-16.
 */
public class CreateTableLayoutRD extends Fragment implements Cloneable {


    private final String taglaunch = "TAGLAUNCH";
    private final String TableCreation = "TableCreation";
    private TextView recyclableTextView;
    TextView textView;
    String investmentType, principal, period, periodType, CompoundingType, interest;
    int duration;
    ArrayList<CustomTableLayoutMain.SingleRow> list;
    ListView listview;
    String initialMaturityValue, initialInterestEarned;
    private ProgressDialog progressBar;
    CustomTableLayout stk, BackupMonth, BackupYear;
    ScrollView scrollView;
    Boolean additionalCalulcation = false;
    String DurationTypeFromRadio;
    static Boolean Completed = true;
    RadioGroup radioGroup;
    TextView textView1, textView2, textView3, textView4;

    @Override
    public void onResume() {

        View resultLayout = (View) getActivity().findViewById(R.id.TopViewLinearLayout);
        resultLayout.setVisibility(View.GONE);
        resultLayout = (View) getActivity().findViewById(R.id.ResultView);
        resultLayout.setVisibility(View.GONE);


        Log.d(taglaunch, "Detail Fragment onResume");
        super.onResume();
        ((Appdrawer) getActivity()).hideSoftKeyboard();

    }

    @Override
    public void onPause() {

        View resultLayout = (View) getActivity().findViewById(R.id.TopViewLinearLayout);
        resultLayout.setVisibility(View.VISIBLE);
        resultLayout = (View) getActivity().findViewById(R.id.ResultView);
        resultLayout.setVisibility(View.VISIBLE);

        Button details = (Button) getActivity().findViewById(R.id.GetDetails);
        details.setText(getText(R.string.details));

        Log.d(taglaunch, "Detail Fragment onPause");
        super.onPause();
        ((Appdrawer) getActivity()).hideSoftKeyboard();

    }


    public void CreateDialog(final String DurationType) {
        progressBar = new ProgressDialog(getActivity());
        Log.d(TableCreation, "Dialog Created");
        progressBar.setIndeterminate(true);
        progressBar.setIndeterminateDrawable(getResources()
                .getDrawable(R.drawable.progressbar_handler));
        progressBar.setMessage("Loading...");
        progressBar.show();

        radioGroup.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (DurationType.equals("Month")) createTableForMonthView();
                else if (DurationType.equals("Year")) createTableForYearView();
            }
        }, 200);

        Log.d(TableCreation, "Dialog Showing");
    }

    public void closeDialog() {
        if (progressBar != null && progressBar.isShowing()) {
            progressBar.dismiss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.tablelayout, container, false);


    }

    public String calculateMaturity(String principal, String DurationType) {

        Double maturity = MathCalculations.CalculateRecurringDeposit(Double.parseDouble(principal.replace(",", "")), Double.parseDouble(interest), 1d, DurationType, CompoundingType);
        Log.d(taglaunch, "Creating Table Calculated MaturityValue " + maturity);
        return formatValue(maturity);
    }

    public String calculateMaturity(String principal, String DurationType, String Duration) {

        Double maturity = MathCalculations.CalculateRecurringDeposit(Double.parseDouble(principal.replace(",", "")), Double.parseDouble(interest), Double.parseDouble(Duration), DurationType, CompoundingType);
        Log.d(taglaunch, "Creating Table Calculated MaturityValue " + maturity);
        return formatValue(maturity);
    }


    public String interestEarned(String maturity, String principal) {

        Double interest = Double.parseDouble(maturity.replace(",", "")) - Double.parseDouble(principal.replace(",", ""));

        return formatValue(interest);
    }

    public String formatValue(Double Format) {

        DecimalFormat displayFormat = new DecimalFormat("#,##,###.##");
        displayFormat.setMinimumFractionDigits(2);

        String FormatedValue = displayFormat.format(Format).toString();
        Log.d(taglaunch, "Fomrated value for Table" + FormatedValue);

        return FormatedValue;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stk = (CustomTableLayout) getActivity().findViewById(R.id.table_main);
        collectInfo();
        intializeTextViews();
        createTableForMonthView();
        radioGroup = (RadioGroup) getActivity().findViewById(R.id.MonthAndYear);
        radioGroup.setOnCheckedChangeListener(new RadioButtonListener());

    }

    private String getMonthName(int number) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, number);
        SimpleDateFormat month_date = new SimpleDateFormat("MMM-yy");
        return month_date.format(cal.getTime());

    }

    private String getYear(int number) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, number);
        SimpleDateFormat month_date = new SimpleDateFormat("yyyy");
        return month_date.format(cal.getTime());

    }


    public void collectInfo() {
        Bundle bundle = getArguments();
        investmentType = bundle.getString("TYPE");
        if (investmentType.equals("RD")) {
            principal = bundle.getString("PrincipalValue").replace(",", "");
            period = bundle.getString("period");
            interest = bundle.getString("interest");
            periodType = bundle.getString("periodType");
            CompoundingType = bundle.getString("compoundingType");
        }
    }


    public void createTableForMonthView() {

        Log.d(TableCreation, "Table Creation in Progress");

        Log.d(taglaunch, "Memory Stk " + stk.toString());

        if (periodType.equals("Year(s)")) duration = Integer.parseInt(period) * 12;
        else duration = Integer.parseInt(period);

        stk.removeAllViews();

        TableRow tbrow = new TableRow(getActivity());

        tbrow.addView(getTextView(getMonthName(0), textView1.getMeasuredWidthAndState(), 0.5f, 0));


        tbrow.addView(getTextView(formatValue(Double.parseDouble(principal)), textView2.getMeasuredWidthAndState(), 0.90f, 0));

        initialMaturityValue = calculateMaturity(principal, "Month(s)");
        initialInterestEarned = interestEarned(initialMaturityValue, principal);


        tbrow.addView(getTextView(initialInterestEarned, textView3.getMeasuredWidthAndState(), 0.70f, 0));


        tbrow.addView(getTextView(initialMaturityValue, textView4.getMeasuredWidthAndState(), 0.90f, 0));

        stk.addView(tbrow);

        String LoopMaturityValue = "";
        int i;
        for (i = 1; i < duration; i++) {
            tbrow = new TableRow(getActivity());

            initialMaturityValue = formatValue(Double.parseDouble(initialMaturityValue.replace(",", "")) + Double.parseDouble(principal));
            LoopMaturityValue = calculateMaturity(initialMaturityValue, "Month(s)");


            tbrow.addView(getTextView(getMonthName(i), textView1.getMeasuredWidthAndState(), 0.5f, i));

            tbrow.addView(getTextView(initialMaturityValue, textView2.getMeasuredWidthAndState(), 0.90f, i));

            tbrow.addView(getTextView(interestEarned(LoopMaturityValue, initialMaturityValue), textView3.getMeasuredWidthAndState(), 0.70f, i));

            tbrow.addView(getTextView(LoopMaturityValue, textView4.getMeasuredWidthAndState(), 0.90f, i));

            initialMaturityValue = LoopMaturityValue;

            stk.addView(tbrow);

        }
        stk.postDelayed(new Runnable() {
            @Override
            public void run() {
                closeDialog();
            }
        }, 400);

        Log.d(TableCreation, "Table Creation Completed");
    }

    public void createTableForYearView() {

        Log.d(TableCreation, "Table Creation in Progress");

        Log.d(taglaunch, "Memory Stk " + stk.toString());

        if (periodType.equals("Year(s)")) duration = Integer.parseInt(period) * 12;
        else duration = Integer.parseInt(period);

        int finalduration = duration;

        stk.removeAllViews();
        TableRow tbrow = new TableRow(getActivity());


        tbrow.addView(getTextView(getYear(0), textView1.getMeasuredWidthAndState(), 0.5f, 0));

        int balancePaymentThisYear = 12 - Calendar.getInstance().get(Calendar.MONTH);


        tbrow.addView(getTextView(formatValue(Double.parseDouble(principal) * balancePaymentThisYear), textView2.getMeasuredWidthAndState(), 0.90f, 0));

        initialMaturityValue = calculateMaturity(formatValue(Double.parseDouble(principal)), "Month(s)", "" + balancePaymentThisYear);
        initialInterestEarned = interestEarned(initialMaturityValue, formatValue(Double.parseDouble(principal) * balancePaymentThisYear));


        tbrow.addView(getTextView(initialInterestEarned, textView3.getMeasuredWidthAndState(), 0.70f, 0));


        tbrow.addView(getTextView(initialMaturityValue, textView4.getMeasuredWidthAndState(), 0.90f, 0));

        stk.addView(tbrow);

        duration = duration - balancePaymentThisYear;

        double spin = duration / 12.0;

        String LoopMaturityValue = initialMaturityValue;

        int i = 1;

        int value = balancePaymentThisYear + 12;

        if (spin > 1)
            for (i = 1; i <= spin; i++) {
                tbrow = new TableRow(getActivity());

                String displayValue = formatValue(Double.parseDouble(initialMaturityValue.replace(",", "")) + (Double.parseDouble(principal) * 12));

                LoopMaturityValue = calculateMaturity(formatValue(Double.parseDouble(principal)), "Month(s)", "" + value);

                tbrow.addView(getTextView(getYear(i), textView1.getMeasuredWidthAndState(), 0.5f, i));

                tbrow.addView(getTextView(displayValue, textView2.getMeasuredWidthAndState(), 0.90f, i));

                tbrow.addView(getTextView(interestEarned(LoopMaturityValue, displayValue), textView3.getMeasuredWidthAndState(), 0.70f, i));

                tbrow.addView(getTextView(LoopMaturityValue, textView4.getMeasuredWidthAndState(), 0.90f, i));

                initialMaturityValue = LoopMaturityValue;

                stk.addView(tbrow);
                value = value + 12;
                duration = duration - 12;
            }


        if (duration != 0) {

            Log.d(TableCreation, "Final Segment " + period + " " + duration);

            tbrow = new TableRow(getActivity());
            tbrow.addView(getTextView(getYear(i), textView1.getMeasuredWidthAndState(), 0.5f, i));

            initialMaturityValue = formatValue(Double.parseDouble(LoopMaturityValue.replace(",", "")) + (Double.parseDouble(principal) * duration));

            LoopMaturityValue = calculateMaturity(formatValue(Double.parseDouble(principal)), "Month(s)", "" + finalduration);

            tbrow.addView(getTextView(initialMaturityValue, textView2.getMeasuredWidthAndState(), 0.90f, i));
            tbrow.addView(getTextView(interestEarned(LoopMaturityValue, initialMaturityValue), textView3.getMeasuredWidthAndState(), 0.70f, i));
            tbrow.addView(getTextView(LoopMaturityValue, textView4.getMeasuredWidthAndState(), 0.90f, i));
            stk.addView(tbrow);


        }

        stk.postDelayed(new Runnable() {
            @Override
            public void run() {
                closeDialog();
            }
        }, 400);

        Log.d(TableCreation, "Table Creation Completed");
    }


    private TextView getTextView(String value, int width, float weight, int i) {

        textView = new TextView(getActivity());
        textView.setText(value);
        TableRow.LayoutParams params = new TableRow.LayoutParams(width, (int) dipToPixels(getActivity(), 40), weight);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setPadding(2, 0, 2, 0);

        if (i % 2 == 0)
            textView.setBackground(getActivity().getResources().getDrawable(R.drawable.back));

        else
            textView.setBackground(getActivity().getResources().getDrawable(R.drawable.border));


        return textView;

    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    private void intializeTextViews() {
        textView1 = (TextView) getActivity().findViewById(R.id.MonthName);
        textView2 = (TextView) getActivity().findViewById(R.id.Opening);
        textView3 = (TextView) getActivity().findViewById(R.id.InterestE);
        textView4 = (TextView) getActivity().findViewById(R.id.closing);

    }

    protected class RadioButtonListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {


            switch (checkedId) {

                case R.id.MonthRadio:
                    CreateDialog("Month");

                    break;


                case R.id.YearRadio:
                    CreateDialog("Year");
                    break;


            }
        }

    }

}
