package com.app.medapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ViewMedicine extends AppCompatActivity {

    TextView name, price, stock, fname, size, type, review;
    ImageView img, loc;
    SharedPreferences sh;
    Spinner sp;
    FloatingActionButton btn;
    String latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medicine);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        img = findViewById(R.id.imageView2);
        name = findViewById(R.id.textView8);
        fname = findViewById(R.id.textView9);
        stock = findViewById(R.id.textView10);
        price = findViewById(R.id.textView11);
        size = findViewById(R.id.textView13);
        type = findViewById(R.id.textView14);
        review = findViewById(R.id.textView37);
        sp = findViewById(R.id.spinner2);
        loc = findViewById(R.id.imageView15);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        btn = findViewById(R.id.floatingActionButton3);

        Intent i = getIntent();

        Objects.requireNonNull(getSupportActionBar()).setTitle(i.getStringExtra("name")+" from "+i.getStringExtra("fname"));

        name.setText(i.getStringExtra("name"));
        fname.setText(i.getStringExtra("fname"));
        stock.setText(i.getStringExtra("stock"));
        price.setText(i.getStringExtra("price"));
        size.setText(i.getStringExtra("size"));
        type.setText(i.getStringExtra("type"));
        latitude = i.getStringExtra("latitude");
        longitude = i.getStringExtra("longitude");

        String full = review.getText().toString()+" "+i.getStringExtra("fname");

        review.setText(full);

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewReview.class).putExtra("medid", i.getStringExtra("id")));
            }
        });


        String url=sh.getString("url", "")+i.getStringExtra("file");
        Picasso.with(getApplicationContext()).load(url).transform(new CircleTransform()). into(img);
        i.getStringExtra("id");

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + "mTitle" + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = sp.getSelectedItem().toString();
                if (quantity.isEmpty()){
                    Snackbar.make(findViewById(android.R.id.content), "Add a minimum quantity", Snackbar.LENGTH_LONG).show();
                    return;
                }
                String url = sh.getString("url", "")+"/and_add_to_cart";

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
                                        Snackbar.make(findViewById(android.R.id.content), "Added to cart", Snackbar.LENGTH_LONG).show();

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
                                        Snackbar.make(findViewById(android.R.id.content), "Cant Add More than Stock", Snackbar.LENGTH_LONG).show();

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

                        params.put("id", i.getStringExtra("id"));
                        params.put("lid", sh.getString("lid", ""));
                        params.put("quantity", quantity);
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