package com.app.medapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class CustomViewReview extends BaseAdapter {
    String[] rating, review, name, date;
    Context context;

    public CustomViewReview(Context applicationContext, String[] name, String[] rating, String[] review, String[] date) {
        this.context = applicationContext;
        this.rating = rating;
        this.review = review;
        this.name = name;
        this.date = date;
    }

    @Override
    public int getCount() {
        return rating.length;
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
            gridView=inflator.inflate(R.layout.activity_custom_view_review,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView n = gridView.findViewById(R.id.textView38);

        TextView d = gridView.findViewById(R.id.textView40);
        TextView r = gridView.findViewById(R.id.textView39);
        RatingBar rtbr = gridView.findViewById(R.id.ratingBar3);


        n.setText(name[i]);
        d.setText(date[i]);
        r.setText(review[i]);
        rtbr.setRating(Float.parseFloat(rating[i]));
        return gridView;
    }
}