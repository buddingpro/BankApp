package com.bankerwala.app;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;

/**
 * Created by mosenthi on 24-Oct-16.
 */
public class CustomTableLayout extends TableLayout implements Cloneable {

    public CustomTableLayout(Context context) {
        super(context);
    }

    public CustomTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
