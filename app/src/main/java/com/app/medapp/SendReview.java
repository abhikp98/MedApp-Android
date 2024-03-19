package com.app.medapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class SendReview extends AppCompatActivity {
    RatingBar rtbr;
    EditText edt;
    Button btn;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendreview);
        rtbr = findViewById(R.id.ratingBar);
        edt = findViewById(R.id.editTextTextMultiLine);
        btn = findViewById(R.id.button6);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = edt.getText().toString();
                if (review.isEmpty()){
                    edt.setError("*");
                    return;
                }
                if (rtbr.getRating() == 0){
                    Snackbar.make(findViewById(android.R.id.content), "Please Rate Us", Snackbar.LENGTH_LONG).show();
                    return;
                }

                String url = sh.getString("url", "")+"/and_send_review";

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

                                        Snackbar.make(findViewById(android.R.id.content), "Send Successfully", Snackbar.LENGTH_LONG).show();
                                        final Handler handler = new Handler(Looper.getMainLooper());
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        }, 2000);


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

                        params.put("rating", Float.toString(rtbr.getRating()));
                        params.put("review", review);
                        params.put("purchaseid", getIntent().getStringExtra("purchaseid"));
                        params.put("lid", sh.getString("lid", ""));

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
        });
    }
}