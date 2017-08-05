package com.bankerwala.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bankerwala.app.R;

import java.util.ArrayList;

/**
 * Created by mosenthi on 16-Oct-16.
 */
public class CustomTableLayoutMain extends BaseAdapter {
    public final String TAG = "TableMainLayout.java";
    ArrayList<SingleRow> list;
    Context context;
    static int i=0;
    Activity actvity;

    CustomTableLayoutMain(){

    }


    CustomTableLayoutMain(Context context, ArrayList<SingleRow> list){
        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
         return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder ViewHolder = null;
        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.tablerow,parent,false);
            ViewHolder = new ViewHolder(row);
            row.setTag(ViewHolder);
        }

        else{
            ViewHolder = (ViewHolder) row.getTag();
        }

        SingleRow temp = list.get(position);

        //TextView textView1 = (TextView) convertView.findViewById(R.id.MonthName);


        if(i%2==0){
            ViewHolder.Month.setText(temp.MonthName);
            ViewHolder.Opening.setText(temp.OpeningBalance);
            ViewHolder.Interest.setText(temp.InterestEarned);
            ViewHolder.Closing.setText(temp.ClosingBalance);
            ViewHolder.Month.setBackground(context.getResources().getDrawable(R.drawable.back));
            ViewHolder.Opening.setBackground(context.getResources().getDrawable(R.drawable.back));
            ViewHolder.Interest.setBackground(context.getResources().getDrawable(R.drawable.back));
            ViewHolder.Closing.setBackground(context.getResources().getDrawable(R.drawable.back));

        }
        else{
            ViewHolder.Month.setText(temp.MonthName);
            ViewHolder.Opening.setText(temp.OpeningBalance);
            ViewHolder.Interest.setText(temp.InterestEarned);
            ViewHolder.Closing.setText(temp.ClosingBalance);
            ViewHolder.Month.setBackground(context.getResources().getDrawable(R.drawable.border));
            ViewHolder.Opening.setBackground(context.getResources().getDrawable(R.drawable.border));
            ViewHolder.Interest.setBackground(context.getResources().getDrawable(R.drawable.border));
            ViewHolder.Closing.setBackground(context.getResources().getDrawable(R.drawable.border));
        }
        i++;


        return row;
    }

   public class SingleRow{
        String MonthName;
        String OpeningBalance;
        String InterestEarned;
        String ClosingBalance;

        SingleRow(String MonthName,String OpeningBalance,String ClosingBalance , String InterestEarned){
            this.MonthName = MonthName;
            this.OpeningBalance= OpeningBalance;
            this.InterestEarned = InterestEarned;
            this.ClosingBalance = ClosingBalance;
        }
    }

    class ViewHolder{
        TextView Month;
        TextView Opening;
        TextView Interest;
        TextView Closing;

        int i = 0;

        ViewHolder(View v){
            Month = (TextView) v.findViewById(R.id.MonthNameRow);
            Opening = (TextView) v.findViewById(R.id.OpeningRow);
            Interest = (TextView) v.findViewById(R.id.InterestRow);
            Closing = (TextView)v.findViewById(R.id.closingRow);
        }

    }



}