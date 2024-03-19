package com.app.medapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class CustomViewmedicine extends BaseAdapter {
    String[] id, name, phaarmacyname, stock, file, price, rating, type, latitude, longitude;
    Context context;

    public CustomViewmedicine(Context applicationContext, String[] id, String[] name, String[] phaarmacyname, String[] stock, String[] file, String[] price, String[] rating, String[] type, String[] latitude, String[] longitude) {
        this.context = applicationContext;
        this.name = name;
        this.phaarmacyname = phaarmacyname;
        this.stock = stock;
        this.file = file;
        this.price = price;
        this.rating = rating;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int getCount() {
        return name.length;
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
            gridView=inflator.inflate(R.layout.activity_custom_viewmedicine,null);

        }
        else
        {
            gridView=(View)view;

        }
        ImageView im = gridView.findViewById(R.id.imageView);
        TextView tname = gridView.findViewById(R.id.textView3);
        TextView tpharmacy = gridView.findViewById(R.id.textView4);
        TextView tstock = gridView.findViewById(R.id.textView5);
        TextView tprice = gridView.findViewById(R.id.textView6);
        RatingBar rtbr = gridView.findViewById(R.id.ratingBar2);
        TextView typeText = gridView.findViewById(R.id.textView12);
        ImageView loc = gridView.findViewById(R.id.imageView14);


        tname.setText(name[i]);
        tpharmacy.setText(phaarmacyname[i]);
        tstock.setText("Stock:"+stock[i]);
        tprice.setText("₹ "+price[i]);
        typeText.setText(type[i]);
        rtbr.setRating(Float.parseFloat(rating[i]));
        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
        String url=sh.getString("url", "")+file[i];
        Picasso.with(context).load(url).transform(new CircleTransform()). into(im);

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?q=loc:" + latitude[i] + "," + longitude[i] + " (" + "mTitle" + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        return gridView;
    }
}