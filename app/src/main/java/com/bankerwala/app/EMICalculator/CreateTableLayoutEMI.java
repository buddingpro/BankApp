package com.bankerwala.app.EMICalculator;

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
public class CreateTableLayoutEMI extends Fragment implements Cloneable {
    private final String taglaunch = "TAGLAUNCH";
    private final String TableCreation = "TableCreation";
    private TextView recyclableTextView;
    TextView textView;
    String investmentType, principal, period, periodType, EmiValue, interest;
    int duration;
    ArrayList<CustomTableLayoutMain.SingleRow> list;
    ListView listview;
    String interestValuePerMonth, principalValuePerMonth, balanceLoan;
    private ProgressDialog progressBar;
    CustomTableLayout stk, BackupMonth, BackupYear;
    ScrollView scrollView;
    Boolean additionalCalulcation = false;
    String DurationTypeFromRadio;
    static Boolean Completed = true;
    RadioGroup radioGroup;
    TextView textView1, textView2, textView3, textView4;
    MathCalculations mathCalculations = new MathCalculations();

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.tablelayout, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stk = (CustomTableLayout) getActivity().findViewById(R.id.table_main);
        collectInfo();
        createTableForMonthView();
        radioGroup = (RadioGroup) getActivity().findViewById(R.id.MonthAndYear);
        radioGroup.setOnCheckedChangeListener(new RadioButtonListener());

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

    public void collectInfo() {
        Bundle bundle = getArguments();
        investmentType = bundle.getString("TYPE");
        if (investmentType.equals("EMI")) {
            principal = bundle.getString("LoanValue").replace(",", "");
            period = bundle.getString("period");
            interest = bundle.getString("interest").replace(",", "");
            periodType = bundle.getString("periodType");
            EmiValue = bundle.getString("EMIValue").replace(",", "");
        }
    }

    public void intializeTextViews() {
        textView1 = (TextView) getActivity().findViewById(R.id.MonthName);
        textView2 = (TextView) getActivity().findViewById(R.id.Opening);
        textView2.setText("Principal");
        textView3 = (TextView) getActivity().findViewById(R.id.InterestE);
        textView3.setText("Interest");
        textView4 = (TextView) getActivity().findViewById(R.id.closing);
        textView4.setText("Balance");
    }

    public void createTableForMonthView() {

        Log.d(TableCreation, "Table Creation in Progress");

        Log.d(taglaunch, "Memory Stk " + stk.toString());

        if (periodType.equals("Year(s)")) duration = Integer.parseInt(period) * 12;
        else duration = Integer.parseInt(period);

        stk.removeAllViews();

        TableRow tbrow = new TableRow(getActivity());

        intializeTextViews();

        textView1.setText("Month");

        String tempValue = principal;

        int split = duration;

        for (int i = 0; i < split; i++) {

            tbrow = new TableRow(getActivity());

            duration--;
            balanceLoan = balanceLoanEOY(duration);
            principalValuePerMonth = formatValue(Double.parseDouble(tempValue.replace(",", "")) - Double.parseDouble(balanceLoan.replace(",", "")));
            interestValuePerMonth = formatValue(Double.parseDouble(EmiValue) - Double.parseDouble(principalValuePerMonth.replace(",", "")));


            tbrow.addView(getTextView(getMonthName(i), textView1.getMeasuredWidthAndState(), 0.5f, i));

            tbrow.addView(getTextView(principalValuePerMonth, textView2.getMeasuredWidthAndState(), 0.90f, i));

            tbrow.addView(getTextView(interestValuePerMonth, textView3.getMeasuredWidthAndState(), 0.70f, i));

            if (i == split - 1)
                tbrow.addView(getTextView(formatValue(Double.parseDouble("0")), textView4.getMeasuredWidthAndState(), 0.90f, i));

            else
                tbrow.addView(getTextView(balanceLoan, textView4.getMeasuredWidthAndState(), 0.90f, i));


            tempValue = balanceLoan;
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

        stk.removeAllViews();
        TableRow tbrow = new TableRow(getActivity());
        intializeTextViews();
        textView1.setText("Year");


        tbrow.addView(getTextView(getYear(0), textView1.getMeasuredWidthAndState(), 0.5f, 0));

        int balancePaymentThisYear = 12 - Calendar.getInstance().get(Calendar.MONTH);

        duration = duration - balancePaymentThisYear;


        balanceLoan = balanceLoanEOY(duration);
        principalValuePerMonth = formatValue(Double.parseDouble(principal) - Double.parseDouble(balanceLoan.replace(",", "")));
        interestValuePerMonth = formatValue((Double.parseDouble(EmiValue) * balancePaymentThisYear) - Double.parseDouble(principalValuePerMonth.replace(",", "")));

        tbrow.addView(getTextView(principalValuePerMonth, textView2.getMeasuredWidthAndState(), 0.90f, 0));

        tbrow.addView(getTextView(interestValuePerMonth, textView3.getMeasuredWidthAndState(), 0.70f, 0));

        tbrow.addView(getTextView(balanceLoan, textView4.getMeasuredWidthAndState(), 0.90f, 0));


        double spin = duration / 12.0;

        stk.addView(tbrow);

        int i = 1;
        Log.d(TableCreation, "Duration Value" + duration + "and spinning " + spin);
        if (spin > 1) {
            for (i = 1; i <= spin; i++) {
                tbrow = new TableRow(getActivity());

                tbrow.addView(getTextView(getYear(i), textView1.getMeasuredWidthAndState(), 0.5f, i));

                String tempbalanceLoan = balanceLoan;

                duration = duration - 12;

                balanceLoan = balanceLoanEOY(duration);
                principalValuePerMonth = formatValue(Double.parseDouble(tempbalanceLoan.replace(",", "")) - Double.parseDouble(balanceLoan.replace(",", "")));
                interestValuePerMonth = formatValue((Double.parseDouble(EmiValue) * 12) - Double.parseDouble(principalValuePerMonth.replace(",", "")));


                tbrow.addView(getTextView(principalValuePerMonth, textView2.getMeasuredWidthAndState(), 0.90f, i));

                tbrow.addView(getTextView(interestValuePerMonth, textView3.getMeasuredWidthAndState(), 0.70f, i));

                tbrow.addView(getTextView(balanceLoan, textView4.getMeasuredWidthAndState(), 0.90f, i));


                stk.addView(tbrow);

            }
        }

        if (duration != 0) {

            Log.d(TableCreation, "Last Segment");

            tbrow = new TableRow(getActivity());
            tbrow.addView(getTextView(getYear(i), textView1.getMeasuredWidthAndState(), 0.5f, i));

            balanceLoan = balanceLoanEOY(duration);

            interestValuePerMonth = formatValue((Double.parseDouble(EmiValue) * duration) - Double.parseDouble(balanceLoan.replace(",", "")));


            tbrow.addView(getTextView(balanceLoan, textView2.getMeasuredWidthAndState(), 0.90f, i));

            tbrow.addView(getTextView(interestValuePerMonth, textView3.getMeasuredWidthAndState(), 0.70f, i));

            tbrow.addView(getTextView("0.00", textView4.getMeasuredWidthAndState(), 0.90f, i));

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


    private String InterestPaid(String principal) {

        Double interestValue = mathCalculations.calculateInterest(Double.parseDouble(principal.replace(",", "")), Double.parseDouble(interest));
        Log.d(taglaunch, "Creating Table Calculated InterestValue " + interest);
        return formatValue(interestValue);
    }

    private String balanceLoanEOY(Integer duration) {

        Double balanceEOY = mathCalculations.calculatePrincipal(Double.parseDouble(EmiValue.replace(",", "")), Double.parseDouble(interest), 1.0 * duration);
        Log.d(taglaunch, "Creating Table Calculated Balance payment by EOY " + balanceEOY);
        return formatValue(balanceEOY);
    }

    private String PrincipalPaid(String interestPaid) {

        Double principal = Double.parseDouble(EmiValue) - Double.parseDouble(interestPaid);

        return formatValue(principal);
    }


    private String balanceLoan(String balance, String Principal) {

        Double balanceValue = Double.parseDouble(balance) - Double.parseDouble(Principal);

        return formatValue(1.0 * (int) Math.round(balanceValue));
    }

    private String formatValue(Double Format) {

        DecimalFormat displayFormat = new DecimalFormat("#,##,###");

        String FormatedValue = displayFormat.format(Format).toString();
        Log.d(taglaunch, "Fomrated value for Table" + FormatedValue);

        return FormatedValue;
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

    private static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    private void closeDialog() {
        if (progressBar != null && progressBar.isShowing()) {
            progressBar.dismiss();
        }
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
