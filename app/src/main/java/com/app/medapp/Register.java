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

public class Register extends AppCompatActivity {
    EditText name, email, phone, password, house, place, post, pin;
    Button btn;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        btn = findViewById(R.id.button4);
        name = findViewById(R.id.editTextTextPersonName4);
        email = findViewById(R.id.editTextTextPersonName5);
        phone = findViewById(R.id.editTextTextPersonName6);
        password = findViewById(R.id.editTextTextPersonName7);
        house = findViewById(R.id.editTextTextPersonName8);
        place = findViewById(R.id.editTextTextPersonName9);
        post = findViewById(R.id.editTextTextPersonName10);
        pin = findViewById(R.id.editTextTextPersonName11);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textName = name.getText().toString();
                String textEmail = email.getText().toString();
                String textPhone = phone.getText().toString();
                String textPassword = password.getText().toString();
                String textHouse = house.getText().toString();
                String textPlace = place.getText().toString();
                String textPost = post.getText().toString();
                String textPin = pin.getText().toString();

                if (textName.isEmpty()){
                    name.setError("*");
                    return;
                }
                if (textEmail.isEmpty()){
                    email.setError("*");
                    return;
                }
                if (textPhone.isEmpty()){
                    phone.setError("*");
                    return;
                }
                if (textPassword.isEmpty()){
                    password.setError("*");
                    return;
                }
                if (textHouse.isEmpty()){
                    house.setError("*");
                    return;
                }
                if (textPlace.isEmpty()){
                    place.setError("*");
                    return;
                }
                if (textPost.isEmpty()){
                    post.setError("*");
                    return;
                }
                if (textPin.isEmpty()){
                    pin.setError("*");
                    return;
                }

                String url = sh.getString("url", "")+"/and_register";

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

                                        Snackbar.make(findViewById(android.R.id.content), "Registered Successfully", Snackbar.LENGTH_LONG).show();
                                        final Handler handler = new Handler(Looper.getMainLooper());
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        }, 2000);


                                    }
                                    else if(jsonObj.getString("status").equalsIgnoreCase("exists")){
                                        Snackbar.make(findViewById(android.R.id.content), "This Email Id is Already Used!", Snackbar.LENGTH_LONG).show();
                                        email.setError("use diffrent email");

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

                        params.put("name", textName);
                        params.put("email", textEmail);
                        params.put("phone", textPhone);
                        params.put("password", textPassword);
                        params.put("house", textHouse);
                        params.put("place", textPlace);
                        params.put("post", textPost);
                        params.put("pin", textPin);
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