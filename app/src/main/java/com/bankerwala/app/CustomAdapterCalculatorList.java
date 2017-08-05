package com.bankerwala.app;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankerwala.app.R;

import java.util.ArrayList;

/**
 * Created by mosenthi on 12-Sep-16.
 */
public class CustomAdapterCalculatorList extends BaseAdapter implements Filterable {


    private final String taglaunch = "TAGLAUNCH";
    ArrayList<SingleRow> list;
    Context context;
    String[] menuTitles;
    ArrayList<SingleRow>filteredData;
    Filter filter;



    CustomAdapterCalculatorList(Context context){
        this.context=context;
        list = new ArrayList<SingleRow>();
        Resources resources = context.getResources();
        menuTitles  = resources.getStringArray(R.array.Calculators);


        int[] menuIcons = {R.mipmap.ic_fdicon,R.mipmap.ic_rdcalculator,R.mipmap.ic_emicalculator};

        for(int i = 0; i<menuIcons.length; i++){
            list.add(new SingleRow(menuTitles[i],menuIcons[i]));
        }

        filteredData = new ArrayList<SingleRow>();
        filteredData.addAll(list);

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
            row = inflater.inflate(R.layout.singlerowcalculatorlist,parent,false);
            ViewHolder = new ViewHolder(row);
            row.setTag(ViewHolder);
        }

        else{
            ViewHolder = (ViewHolder) row.getTag();
        }

        SingleRow temp = list.get(position);

        ViewHolder.BankHeaderTitle.setText(temp.menu);
        ViewHolder.bankLogo.setImageResource(temp.menuIcons);

        return row;
    }
    @Override
    public Filter getFilter() {
        if(filter==null){
            filter = new MyFilter();
        }
        return filter;
    }


    class SingleRow{
        String menu;
        int menuIcons;

        SingleRow(String menu,int menuIcons){
            this.menu = menu;
            this.menuIcons= menuIcons;
        }
    }

    class ViewHolder{
        TextView BankHeaderTitle;
        ImageView bankLogo;

            ViewHolder(View v){
                BankHeaderTitle = (TextView) v.findViewById(R.id.BankHeaderTitle);
                bankLogo = (ImageView) v.findViewById(R.id.bankLogo);
            }

    }


    public class MyFilter extends Filter{

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //creating filter results object which needs to returned.
                FilterResults results  = new FilterResults();
                ArrayList<SingleRow> filterResults = new ArrayList<SingleRow>();
                String searchResult;
                if(constraint!=null&&constraint.length()>0)
                    searchResult = constraint.toString().toLowerCase();

                else{
                    results.count = filteredData.size();
                    results.values= filteredData;
                    return results;
                }

                int count = filteredData.size();
                String filterableString;

                for (int i = 0; i < count; i++) {
                    filterableString = filteredData.get(i).menu;
                    if (filterableString.toLowerCase().contains(searchResult)) {
                        SingleRow singleRow = new SingleRow(filteredData.get(i).menu,filteredData.get(i).menuIcons);
                        filterResults.add(singleRow);
                    }
                }

                results.count = filterResults.size();
                results.values= filterResults;
                return results;

            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results.count == 0)
                    notifyDataSetInvalidated();
                else {

                    list = (ArrayList<SingleRow>) results.values;
                   notifyDataSetChanged();
                }
            }



        }
    }


