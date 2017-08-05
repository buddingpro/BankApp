package com.bankerwala.app.FixedDeposit;

/**
 * Created by MohanRaj_Senthilnath on 4/16/2017.
 */

public enum EDepositCompounding {

    QUARTERLY("Quarterly"),
    MONTHLY("Monthly"),
    HALF_YEARLY("Half Yearly"),
    YEARLY("Yearly"),
    SIMPLE_INTEREST("Simple Interest");

    private String compounding;

    EDepositCompounding(String compouding) {
        this.compounding = compouding;
    }

    @Override
    public String toString() {
        return compounding;
    }
}
