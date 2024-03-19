package com.app.medapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SelectAddress extends AppCompatActivity {

    Button btn;

    TextView address;
    boolean isAddress, first = false;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        btn = findViewById(R.id.button9);
        address = findViewById(R.id.textView24);

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor ed = sh.edit();
                ed.remove("housename");
                ed.remove("place");
                ed.remove("pin");
                ed.remove("post");
                ed.remove("addid");
                ed.commit();
                startActivity(new Intent(getApplicationContext(), ViewAddress.class));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddress){
                    new AlertDialog.Builder(v.getRootView().getContext())
                            .setTitle("Payment Method")
                            .setMessage("Online or Offline?")
                            .setPositiveButton("Online", (dialog, which) -> {
                                startActivity(new Intent(getApplicationContext(), PaymentActivity.class).putExtra("amount", getIntent().getStringExtra("amount")).putExtra("a", "a"));
                            })
                            .setNegativeButton("Offline", (dialog, which) ->{
                                paymentOffline();
                            })
                            .show();
                }
                else {
                    Snackbar.make(findViewById(android.R.id.content), "Click above and select Address", Snackbar.LENGTH_LONG).show();

                }

            }
        });

    }

    private void paymentOffline() {

        String url = sh.getString("url", "")+"/and_payment";

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
                                Snackbar.make(findViewById(android.R.id.content), "Placed Successfully", Snackbar.LENGTH_LONG).show();

                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 2000);

                            }

                            else {
                                Snackbar.make(findViewById(android.R.id.content), "Username or password is incorrect", Snackbar.LENGTH_LONG).show();

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
                params.put("lid", sh.getString("lid", ""));
                params.put("address", sh.getString("addid", ""));
                params.put("method", "offline");
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

    @Override
    protected void onResume() {
        if(first) {
            if (!sh.getString("housename", "").isEmpty()) {
                isAddress = true;
                String adrs = sh.getString("housename", "") + "\n" + sh.getString("place", "") + "\n" + sh.getString("post", "") + "\n" + sh.getString("pin", "");
                address.setText(adrs);
            }

        }
        first = true;
        super.onResume();
    }
}