package com.bankerwala.app;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Created by mosenthi on 06-Oct-16.
 */
public class EditTextListnerCLass implements TextWatcher {

    private final String taglaunch = "TAGLAUNCH";
    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;
    EditText et;
    int trailingZeroCount;
    boolean interest =false;

    public EditTextListnerCLass(EditText et, DecimalFormat df, DecimalFormat dfnd){
        this.et = et;
        this.df = df;
        this.dfnd =dfnd;
    }

    public EditTextListnerCLass(EditText et, DecimalFormat df, DecimalFormat dfnd ,boolean interest){
        this.et = et;
        this.df = df;
        this.dfnd =dfnd;
        this.interest=interest;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


        if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()))) {
            int index = s.toString().indexOf(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
            trailingZeroCount = 0;
            if (index > -1) {
                for (index++; index < s.length(); index++) {
                    if (s.charAt(index) == '0')
                        trailingZeroCount++;
                    else {
                        trailingZeroCount = 0;
                    }
                }
            }
            hasFractionalPart = true;
        }

        else {
            hasFractionalPart = false;
        }

    }

    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";

    public void afterTextChanged(Editable s)
    {
        et.removeTextChangedListener(this);

        try {
            int inilen, endlen;
            inilen = et.getText().length();

            String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
            Number n = df.parse(v);
            int cp = et.getSelectionStart();
            if (hasFractionalPart) {
                StringBuilder trailingZeros = new StringBuilder();
                while (trailingZeroCount-- > 0)
                    trailingZeros.append('0');
                et.setText(df.format(n) + trailingZeros.toString());
            } else {
                et.setText(dfnd.format(n));
            }
            endlen = et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                // place cursor at the end?
                et.setSelection(et.getText().length() - 1);
            }
        } catch (NumberFormatException nfe) {
            // do nothing?
        } catch (ParseException e) {
            // do nothing?
        }

        et.addTextChangedListener(this);
    }

}