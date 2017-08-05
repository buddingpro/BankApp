package com.bankerwala.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
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
public class CustomAdapterBanksList extends BaseAdapter implements Filterable {


    private final String taglaunch = "TAGLAUNCH";
    ArrayList<SingleRow> list;
    Context context;
    String[] menuTitles;
    ArrayList<SingleRow>filteredData;
    Filter filter;



    CustomAdapterBanksList(Context context){
        this.context=context;
        list = new ArrayList<SingleRow>();
        Resources resources = context.getResources();
        menuTitles  = resources.getStringArray(R.array.Banks);


        int[] menuIcons = {R.mipmap.ic_allahabadbank_logo,R.mipmap.ic_andhrabank_logo,R.mipmap.ic_axisbank_logo,R.mipmap.ic_bankofbaroda_logo,
                R.mipmap.ic_bankofindia_logo,R.mipmap.ic_bankofmaharashtra_logo, R.mipmap.ic_bharatiyamahilabank_logo,R.mipmap.ic_canarabank_logo
        ,R.mipmap.ic_capitallocalareabank_logo,R.mipmap.ic_cathaloicsyrianbank,R.mipmap.ic_centralbankofindia_logo,R.mipmap.ic_cityunionbank,
        R.mipmap.ic_corporationbnk,R.mipmap.ic_dcbbnk,R.mipmap.ic_denabank,R.mipmap.ic_dhanabnk,R.mipmap.ic_fedebnk,R.mipmap.ic_hdfcbnk,R.mipmap.ic_icicibnk,
        R.mipmap.ic_idbibnk,R.mipmap.ic_indnbnk,R.mipmap.ic_iobbank,R.mipmap.ic_indusindbnk,R.mipmap.ic_jkbank,R.mipmap.ic_krntabnk,
        R.mipmap.ic_kotakbnk,R.mipmap.ic_laksvlsbnk,R.mipmap.ic_nanibnk,R.mipmap.ic_obcbnk,R.mipmap.ic_pnbbank,R.mipmap.ic_rblbank,R.mipmap.ic_southindnbnk,
        R.mipmap.ic_sbbj,R.mipmap.ic_sbhh,R.mipmap.ic_sbi,R.mipmap.ic_sbm,R.mipmap.ic_sbip,R.mipmap.ic_sbit,R.mipmap.ic_synbank
        ,R.mipmap.ic_tmb,R.mipmap.ic_uco,R.mipmap.ic_unionbnk,R.mipmap.ic_ubibnk,R.mipmap.ic_vjybnk,R.mipmap.ic_yesbank     };

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
            row = inflater.inflate(R.layout.singlerowbanklist,parent,false);
            ViewHolder = new ViewHolder(row);
            row.setTag(ViewHolder);
        }

        else{
            ViewHolder = (ViewHolder) row.getTag();
        }

        SingleRow temp = list.get(position);

        if(temp.menu.length()>29){
            ViewHolder.BankHeaderTitle.setTextAppearance(context, android.R.style.TextAppearance_Small);
        }
        else if(temp.menu.length()>=22) {
            ViewHolder.BankHeaderTitle.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        }
        else{
            ViewHolder.BankHeaderTitle.setTextAppearance(context, android.R.style.TextAppearance_Large);
        }
        ViewHolder.BankHeaderTitle.setText(temp.menu);
        ViewHolder.BankHeaderTitle.setTextColor(Color.WHITE);
        ViewHolder.linkNotification.setText( context.getResources().getString(R.string.Click_here));
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
       TextView linkNotification;
        ImageView bankLogo;

            ViewHolder(View v){
                BankHeaderTitle = (TextView) v.findViewById(R.id.BankHeaderTitle);
                linkNotification = (TextView) v.findViewById(R.id.linkNotification);
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


