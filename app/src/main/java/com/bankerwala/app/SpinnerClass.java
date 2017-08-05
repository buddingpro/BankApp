package com.bankerwala.app;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bankerwala.app.R;

/**
 * Created by mosenthi on 10-Oct-16.
 */
public class SpinnerClass implements AdapterView.OnItemSelectedListener ,View.OnTouchListener{
    private final String taglaunch = "TAGLAUNCH";
    private final String FINDLAYOUTVISIBILITY = "FINDLAYOUTVISIBILITY";
    private final String FDTYPETriggered = "FDTYPETriggered";
    View resultLayout;
    EditText editText;
    TextView textView;
    Spinner spinner;
    String[] adapter,childadapter;
    Context context;

    boolean userSelect = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        userSelect = true;

        Log.d(FINDLAYOUTVISIBILITY, "Touch Event");
        return false;
    }

    public SpinnerClass(View resultLayout) {

        this.resultLayout = resultLayout;
    }

    public SpinnerClass(Context context, View resultLayout, Spinner spinner, String[]... adapter) {
        this.context = context;
        this.resultLayout = resultLayout;
        this.spinner = spinner;
        this.adapter =adapter[0];
        this.childadapter= adapter[1];
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (userSelect) {
            resultLayout.setVisibility(View.GONE);
            Log.d(FINDLAYOUTVISIBILITY, "Layout Gone I am from Spinner");
            userSelect =true;
        }

        Log.d(FDTYPETriggered, "Parent ID "+parent.getId());
        Log.d(FDTYPETriggered, "Spinner ID "+R.id.FDType);

        if(parent.getId()==R.id.FDTypespinner){

            Log.d(FDTYPETriggered, "FD Type Triggered");

            if(position==3)
            spinner.setAdapter(customsAdapter(childadapter));

            else
                spinner.setAdapter(customsAdapter(adapter));
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public ArrayAdapter<String> customsAdapter(String[] data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.spinner_support_textview, data);
        adapter.setDropDownViewResource(R.layout.spinner_support);

        return adapter;
    }
}
