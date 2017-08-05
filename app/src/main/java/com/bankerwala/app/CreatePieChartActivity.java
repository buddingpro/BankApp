package com.bankerwala.app;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bankerwala.app.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mosenthi on 10-Oct-16.
 */
public class CreatePieChartActivity extends Fragment {
    private final String taglaunch = "TAGLAUNCH";
    PieChart pieChart;
    List<ChartData> data;
    String DisplayOne, DisplayTwo, calculationType, DisplayCenter, DisplayCenterString;
    Double MaturityValue, PrincipalValue;
    float Percentage;
    TextView displaytextone, displaytextonevalue, displaytexttwo, displaytexttwovalue;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.piechart, container, false);

    }

    @Override
    public void onResume() {

        Log.d(taglaunch, "Detail Fragment onResume");
        super.onResume();
        ((Appdrawer) getActivity()).hideSoftKeyboard();


        View resultLayout = (View) getActivity().findViewById(R.id.TopViewLinearLayout);
        resultLayout.setVisibility(View.GONE);
        resultLayout = (View) getActivity().findViewById(R.id.ResultView);
        resultLayout.setVisibility(View.GONE);

        Log.d(taglaunch, "Detail Fragment onPause");
        super.onPause();
        ((Appdrawer) getActivity()).hideSoftKeyboard();

    }

    @Override
    public void onPause() {

        View resultLayout = (View) getActivity().findViewById(R.id.TopViewLinearLayout);
        resultLayout.setVisibility(View.VISIBLE);
        resultLayout = (View) getActivity().findViewById(R.id.ResultView);
        resultLayout.setVisibility(View.VISIBLE);

        Log.d(taglaunch, "Detail Fragment onPause");
        super.onPause();
        ((Appdrawer) getActivity()).hideSoftKeyboard();

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            ((Appdrawer) getActivity()).setNavIcon();
        }
        Bundle bundle = getArguments();

        MaturityValue = Double.parseDouble(bundle.getString("MaturityValue").replace(",", ""));
        PrincipalValue = Double.parseDouble(bundle.getString("PrincipalValue").replace(",", ""));
        Percentage = (float) ((float) (PrincipalValue * 100) / MaturityValue);

        displaytextone = (TextView) getActivity().findViewById(R.id.textViewDisplaytextOne);
        displaytexttwo = (TextView) getActivity().findViewById(R.id.textViewDisplaytextTwo);

        displaytextonevalue = (TextView) getActivity().findViewById(R.id.textViewDisplayTextOneValue);
        displaytextonevalue.setText(bundle.getString("PrincipalValue"));

        displaytexttwovalue = (TextView) getActivity().findViewById(R.id.textViewDisplaytextTwoValue);
        displaytexttwovalue.setText(bundle.getString("InterestEarned"));

        DisplayCenter = bundle.getString("MaturityValue");

        calculationType = bundle.getString("TYPE");
        if (calculationType.equals("FD")) {
            DisplayOne = getString(R.string.principal_amount);
            DisplayTwo = getString(R.string.interest_earned);
            DisplayCenterString = getString(R.string.maturity_amount);

        } else if (calculationType.equals("EMI")) {
            DisplayOne = "Loan Amount";
            DisplayTwo = "Interest Payable";
            DisplayCenterString = "Total Amount Payable";
        }
        displaytextone.setText(DisplayOne);
        displaytexttwo.setText(DisplayTwo);

        pieChart = (PieChart) getActivity().findViewById(R.id.pie_chart);

        data = new ArrayList<>();

        data.add(new ChartData(String.format("%.2f", Percentage) + "%", Percentage, Color.BLACK, getResources().getColor(R.color.PieChartOrange)));
        data.add(new ChartData(String.format("%.2f", 100.00f - Percentage) + "%", 100.00f - Percentage, Color.BLACK, getResources().getColor(R.color.PieChartBlue)));
        pieChart.setChartData(data);
        pieChart.partitionWithPercent(true);
        pieChart.setAboutChart(DisplayCenterString);
        pieChart.setAboutChartValue(DisplayCenter);

        View resultLayout = (View) getActivity().findViewById(R.id.TopViewLinearLayout);
        resultLayout.setVisibility(View.GONE);
        resultLayout = (View) getActivity().findViewById(R.id.ResultView);
        resultLayout.setVisibility(View.GONE);

    }


}
