package com.app.medapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentHistory extends AppCompatActivity {

    String[] pharmacyname, date, id, status, purchase, amount;
    ListView listView;
    SharedPreferences sh;
    Button today, previous;
    ImageView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        listView = findViewById(R.id.list);

        today = findViewById(R.id.button2);
        previous = findViewById(R.id.button3);

        empty = findViewById(R.id.imageView9);

        view_payment_history("today");

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_payment_history("today");
                getSupportActionBar().setTitle("Today's History");
                today.setTypeface(null, Typeface.BOLD);
                previous.setTypeface(null, Typeface.NORMAL);

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_payment_history("previous");
                getSupportActionBar().setTitle("Previous History");
                previous.setTypeface(null, Typeface.BOLD);
                today.setTypeface(null, Typeface.NORMAL);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long idd) {

                new AlertDialog.Builder(view.getRootView().getContext())
                        .setTitle("Hey!")
                        .setMessage("what do you want?")
                        .setPositiveButton("View Products", (dialog, which) -> {
                            startActivity(new Intent(getApplicationContext(), ViewItems.class).putExtra("id", id[position]));                        })
                        .setNeutralButton("Rate us", (dialog, which) ->{
                            startActivity(new Intent(getApplicationContext(), SendReview.class).putExtra("purchaseid", id[position]));
                        })
                        .show();


            }
        });
    }

    private void view_payment_history(String type) {

        String url = sh.getString("url", "")+"/and_view_payment_history";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        // response
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js= jsonObj.getJSONArray("data");
                                id=new String[js.length()];
                                pharmacyname=new String[js.length()];
                                date=new String[js.length()];
                                purchase=new String[js.length()];
                                status=new String[js.length()];
                                amount=new String[js.length()];


                                for(int i=0;i<js.length();i++)
                                {
                                    JSONObject u=js.getJSONObject(i);
                                    id[i]=u.getString("id");
                                    pharmacyname[i]=u.getString("phname");
                                    date[i]=u.getString("date");
                                    purchase[i]=u.getString("purchase");
                                    status[i]=u.getString("status");
                                    amount[i]=u.getString("total");


                                }
                                if (id.length!= 0){
                                    empty.setVisibility(View.GONE);
                                }
                                else {
                                    empty.setVisibility(View.VISIBLE);
                                }

                                listView.setAdapter(new CustomPaymentHistory(getApplicationContext(),pharmacyname,date, purchase, status, amount));
                            }


                            // }
                            else {
                                Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                            }

                        }    catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();

                String id=sh.getString("lid","");
                params.put("lid",id);
                params.put("type",type);

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