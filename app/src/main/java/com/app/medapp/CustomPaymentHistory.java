package com.app.medapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomPaymentHistory extends BaseAdapter {
    String[] pharmacyname, date, purchase, status, amount;
    Context context;


    public CustomPaymentHistory(Context applicationContext, String[] pharmacyname, String[] date, String[] purchase, String[] status, String[] amount) {
        this.context = applicationContext;
        this.pharmacyname = pharmacyname;
        this.date = date;
        this.purchase=purchase;
        this.status=status;
        this.amount = amount;
    }

    @Override
    public int getCount() {
        return pharmacyname.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(view==null)
        {
            gridView=new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView=inflator.inflate(R.layout.activity_custom_payment_history,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView nametext = gridView.findViewById(R.id.textView33);
        TextView datetext = gridView.findViewById(R.id.textView35);
        TextView totalprice = gridView.findViewById(R.id.textView36);
        TextView statusText = gridView.findViewById(R.id.textView34);


        nametext.setText(pharmacyname[i]);
        datetext.setText(date[i]);
        totalprice.setText("₹ "+amount[i]);

        if (status[i].equalsIgnoreCase("pending")){
            statusText.setText("Wait for Pharmacy to confirm");
        }
        else if(status[i].equalsIgnoreCase("approved")){
            statusText.setText("Amount of "+amount[i]+" should pay on hand");
        }
        else if (status[i].equalsIgnoreCase("paid")){
            statusText.setText("Product will be shipped soon");
            statusText.setTextColor(Color.parseColor("#FF8C00"));
        }
        else if (status[i].equalsIgnoreCase("completed")){
            statusText.setText("Delivered");
            statusText.setTextColor(Color.parseColor("#008000"));
        }
        else{
            if(purchase[i].equalsIgnoreCase("offline")){
                statusText.setText("Rejected due to "+status[i]);
                statusText.setTextColor(Color.RED);
            }
            else {
                statusText.setText("Rejected due to "+status[i]+"\n Amount will be returned within 7 working days");
                statusText.setTextColor(Color.RED);
            }
        }


        return gridView;

    }
}