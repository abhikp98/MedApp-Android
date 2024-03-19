package com.app.medapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomViewAddress extends BaseAdapter {
    String[] housename, place, post, pin, id;
    Context context;


    public CustomViewAddress(Context applicationContext, String[] housename, String[] place, String[] post, String[] pin, String[] id) {
        this.housename = housename;
        this.place = place;
        this.post = post;
        this.pin = pin;
        this.context = applicationContext;
        this.id = id;
    }

    @Override
    public int getCount() {
        return housename.length;
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
            gridView=inflator.inflate(R.layout.activity_custom_view_address,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView h = gridView.findViewById(R.id.textView25);
        TextView pl = gridView.findViewById(R.id.textView26);
        TextView po = gridView.findViewById(R.id.textView27);
        TextView pi = gridView.findViewById(R.id.textView28);
        ImageView btn = gridView.findViewById(R.id.imageView16);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(v.getRootView().getContext())
                        .setTitle("Delete Address")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            deleteAddress(context, id[i]);
                        })
                        .setNeutralButton("No", (dialog, which) ->{
                            dialog.dismiss();
                        })
                        .show();
            }
        });


        h.setText(housename[i]);
        pl.setText(place[i]);
        po.setText(post[i]);
        pi.setText(pin[i]);
        return gridView;
    }

    private void deleteAddress(Context context, String id) {
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
        String url = sh.getString("url", "")+"/and_delete_address";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        // response
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                ((Activity) context).recreate();
                            }
                            else if(jsonObj.getString("status").equalsIgnoreCase("no")) {
                                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                            }

                        }    catch (Exception e) {
                            Toast.makeText(context, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(context, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", id);

                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS=100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }
}