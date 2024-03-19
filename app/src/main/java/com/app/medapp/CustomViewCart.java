package com.app.medapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomViewCart extends BaseAdapter {
    String[] medname, quantity, image, id, price, isStock;
    Context context;
    public int a = 0;

    public CustomViewCart(Context applicationContext, String[] id, String[] medname, String[] quantity, String[] price, String[] image, String[] isStock) {
        this.context = applicationContext;
        this.medname = medname;
        this.quantity = quantity;
        this.image = image;
        this.id=id;
        this.price = price;
        this.isStock = isStock;
    }

    @Override
    public int getCount() {
        return medname.length;
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
            gridView=inflator.inflate(R.layout.activity_custom_view_cart,null);

        }
        else
        {
            gridView=(View)view;

        }
        ImageView im = gridView.findViewById(R.id.imageView3);
        TextView tname = gridView.findViewById(R.id.textView15);
        Spinner sp = gridView.findViewById(R.id.spinner);
        Button button = gridView.findViewById(R.id.button7);
        TextView toutstock = gridView.findViewById(R.id.textView18);
        TextView total = gridView.findViewById(R.id.textView46);

        total.setText("Total amount ₹ "+price[i]);

        if (isStock[i].equalsIgnoreCase("False")){
            toutstock.setVisibility(View.INVISIBLE);
        }

        sp.setSelection(Integer.parseInt(quantity[i])-1);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(v.getRootView().getContext())
                        .setTitle("Are you sure?")
                        .setMessage("Are you sure you want to remove "+medname[i]+" from cart?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            deletecart(viewGroup, view, i, id[i]);
                            dialog.dismiss();
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();


            }
        });

        tname.setText(medname[i]);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idd) {
                if (a != 0){
                    String spinnervalue = sp.getSelectedItem().toString();
                    if (updateStock(parent, view, position, id[i], spinnervalue)){
                        ((AdapterView) viewGroup).performItemClick(view, i, getItemId(i));
                    }
                }
                a ++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
        String url=sh.getString("url", "")+image[i];
        Picasso.with(context).load(url).transform(new CircleTransform()). into(im);
        return gridView;
    }

    private void deletecart(ViewGroup viewGroup, View view, int i, String s) {
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);

        String url = sh.getString("url", "")+"/and_delete_cart";

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
                                ((AdapterView) viewGroup).performItemClick(view, i, getItemId(i));


                            }


                            // }
                            else {
                                Snackbar.make(viewGroup.findViewById(android.R.id.content), "Username or password is incorrect", Snackbar.LENGTH_LONG).show();

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

                params.put("id", s);
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

    private boolean updateStock(AdapterView<?> parent, View view, int position, String s, String spinnervalue) {

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);

        String url = sh.getString("url", "")+"/and_update_cart";

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
                            }


                            // }
                            else {
                                Snackbar.make(parent.findViewById(android.R.id.content), "Username or password is incorrect", Snackbar.LENGTH_LONG).show();

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

                params.put("id", s);
                params.put("quantity", spinnervalue);
                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS=100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);

        return true;
    }
}