package com.bankerwala.app;

import android.util.Log;

/**
 * Created by mosenthi on 12-Oct-16.
 */
public class MathCalculations {

    private final static String taglaunch = "TAGLAUNCH";

    public static Double CalculateFixedDeposit(Double principal, Double rate, Double duration, String durationType, String CompoundingType) {

        if (durationType.equals("Day(s)")) {
            duration = duration / 365;

        } else if (durationType.equals("Month(s)")) {
            duration = duration / 12;
        }

        int compounding = 1;
        boolean simpleInterest = false;


        switch (CompoundingType) {
            case "Monthly":
                compounding = 12;
                break;

            case "Quarterly":
                compounding = 4;
                break;

            case "Half Yearly":
                compounding = 2;
                break;

            case "Simple Interest":
                simpleInterest = true;
                break;

            default:
                compounding = 1;
                break;
        }


        rate = rate / 100;


        if (!simpleInterest) {
            double time = duration * compounding;
            Log.d(taglaunch, "Calculations Received " + principal + " " + rate + " " + compounding + " " + time + " " + duration);

            double futureValue = principal * Math.pow((1 + rate / compounding), time);
            return futureValue;
        } else {

            double futureValue = principal * (1 + (rate * duration));
            return futureValue;
        }


    }


    public static Double CalculateRecurringDeposit(Double principal, Double rate, Double duration, String durationType, String CompoundingType) {

        if (durationType.equals("Year(s)")) {
            duration = duration * 12;
        }

        int compounding = 1;
        int divisor = 100;

        switch (CompoundingType) {
            case "Monthly":
                compounding = 1;
                divisor = 1200;
                break;

            case "Quarterly":
                compounding = 3;
                divisor = 400;
                break;

            case "Half Yearly":
                compounding = 6;
                divisor = 200;
                break;
            default:
                compounding = 12;
                divisor = 100;
                break;
        }

        double futureValue = (principal * ((Math.pow((1 + (rate / divisor)), duration / compounding) - 1))) / (1 - (Math.pow((1 + (rate / divisor)), -(1.0 / compounding))));
        return futureValue;


    }

    public static Double CalculateDeposit(Double principal, Double duration, String durationType) {

        if (durationType.equals("Year(s)")) {
            duration = duration * 12;
        }

        return principal * duration;
    }


    public Double calculateEMI(Double principal, Double rate, Double duration, String durationType) {

        if (durationType.equals("Year(s)")) {
            duration = duration * 12;
        }

        rate = rate / 1200;

        Log.d(taglaunch, "Calculations Received **** principal " + principal + "rate " + rate + "duration " + duration);

        return (principal * rate * Math.pow(1 + rate, duration)) / (Math.pow(1 + rate, duration) - 1);

    }

    public  Double calculatePrincipal(Double EMI, Double rate, Double duration) {

        rate = rate / 1200;

        Log.d(taglaunch, "Calculations Received **** EMI " + EMI + "rate " + rate + "duration " + duration);

        return (EMI * (Math.pow((1 + rate), duration) - 1)) / (rate * Math.pow((1 + rate), duration));

    }

    public  Double calculateInterest(Double LoanAmount, Double rate) {

        rate = rate / 1200;

        Log.d(taglaunch, "**** Calculations Received **** LoanAmount " + LoanAmount + "rate " + rate);

        return (LoanAmount * rate);

    }

}
