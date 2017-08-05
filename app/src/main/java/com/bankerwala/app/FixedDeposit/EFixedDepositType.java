package com.bankerwala.app.FixedDeposit;

/**
 * Created by MohanRaj_Senthilnath on 4/15/2017.
 */

public enum EFixedDepositType {


    REINVESTMENT("Reinvestment"),
    QUARTERLY_PAYOUT("Quarterly Payout"),
    MONTHLY_PAYOUT("Monthly Payout"),
    SHORT_TERM("Short Term");


    private String fdType;

    EFixedDepositType(String fdtype) {
        this.fdType = fdtype;
    }


    public String getFdType() {
        return fdType;
    }

    @Override
    public String toString(){
        return fdType;
    }

}
