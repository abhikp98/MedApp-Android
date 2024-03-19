package com.app.medapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CustomViewItems extends BaseAdapter {
    String[] name, quantity, image, price;
    Context context;

    public CustomViewItems(Context applicationContext, String[] name, String[] quantity, String[] image, String[] price) {
        this.name = name;
        this.context = applicationContext;
        this.quantity = quantity;
        this.image = image;
        this.price = price;

    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(convertView==null)
        {
            gridView=new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView=inflator.inflate(R.layout.activity_custom_view_items,null);

        }
        else
        {
            gridView=(View)convertView;

        }
        TextView nametext = gridView.findViewById(R.id.textView29);
        TextView quantitytext = gridView.findViewById(R.id.textView30);
        TextView pricetext = gridView.findViewById(R.id.textView31);
        TextView totalprice = gridView.findViewById(R.id.textView32);
        ImageView img = gridView.findViewById(R.id.imageView4);


        nametext.setText(name[position]);
        quantitytext.setText("quantity: "+quantity[position]);
        pricetext.setText("₹ "+price[position]);
        totalprice.setText("Grand total ₹ "+String.valueOf(Float.parseFloat(price[position])*Float.parseFloat(quantity[position])));
        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
        String url=sh.getString("url", "")+image[position];
        Picasso.with(context).load(url).transform(new CircleTransform()). into(img);
        return gridView;
    }
}