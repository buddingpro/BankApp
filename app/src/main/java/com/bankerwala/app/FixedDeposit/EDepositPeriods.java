package com.bankerwala.app.FixedDeposit;

/**
 * Created by MohanRaj_Senthilnath on 4/16/2017.
 */

public enum EDepositPeriods {

    YEAR("Year(s)"),
    MONTH("Month(s)"),
    DAY("Day(s)");

    private String depositPeriod;

    EDepositPeriods(String depositPeriod){
        this.depositPeriod = depositPeriod;
    }

    @Override
    public String toString(){
        return depositPeriod;
    }

}
