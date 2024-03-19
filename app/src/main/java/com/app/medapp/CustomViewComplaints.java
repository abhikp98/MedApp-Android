package com.app.medapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomViewComplaints extends BaseAdapter {
    String [] complaint, date, reply, replydate;
    Context context;

    public CustomViewComplaints(Context applicationContext, String[] complaint, String[] date, String[] reply, String[] replydate) {
        this.context = applicationContext;
        this.complaint = complaint;
        this.date = date;
        this.reply = reply;
        this.replydate = replydate;

    }

    @Override
    public int getCount() {
        return complaint.length;
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
            gridView=inflator.inflate(R.layout.activity_custom_view_complaints,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView c = gridView.findViewById(R.id.textView19);
        TextView cd = gridView.findViewById(R.id.textView20);
        TextView r = gridView.findViewById(R.id.textView23);
        TextView rd = gridView.findViewById(R.id.textView22);
        c.setText(complaint[i]);
        cd.setText(date[i]);
        r.setText(reply[i]);
        rd.setText(replydate[i]);

        if (replydate[i].equalsIgnoreCase("pending")){
            rd.setVisibility(View.INVISIBLE);
            r.setText("Wait for Administrator to Reply");
            r.setTextColor(Color.RED);
        }


        return gridView;
    }
}