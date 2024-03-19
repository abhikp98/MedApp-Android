package com.app.medapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText username, password;
    CheckBox ch;
    Button btn;
    TextView signup;
    SharedPreferences sh;
    ImageView eye;
    boolean isClosed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        username = findViewById(R.id.editTextTextPersonName2);
        password = findViewById(R.id.editTextTextPassword);
        ch = findViewById(R.id.checkBox);
        btn = findViewById(R.id.button);
        signup = findViewById(R.id.textView2);
        eye = findViewById(R.id.imageView11);

        username.setText(sh.getString("username", ""));
        password.setText(sh.getString("password", ""));

        if (!sh.getString("username", "").isEmpty())
        {
            ch.setChecked(true);
        }

//        SignUp Page Intent
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClosed){
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isClosed = false;
                    eye.setImageResource(R.drawable.closed_eye);
                    password.setSelection(password.getText().length());
                }
                else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isClosed = true;
                    eye.setImageResource(R.drawable.closed_eye);
                    password.setSelection(password.getText().length());
                }
            }
        });



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textUsername = username.getText().toString();
                String textPassword = password.getText().toString();
                SharedPreferences.Editor editor = sh.edit();

                if (textUsername.isEmpty()){
                    username.setError("*");
                    return;
                }
                if (textPassword.isEmpty()){
                    password.setError("*");
                    return;
                }

                if (ch.isChecked()){

                    editor.putString("username", textUsername);
                    editor.putString("password", textPassword);
                    editor.commit();
                }
                else {
                    editor.remove("username");
                    editor.remove("password");
                    editor.commit();

                }
                String url = sh.getString("url", "")+"/and_login";

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

                                        editor.putString("lid", jsonObj.getString("lid"));
                                        editor.putString("name", jsonObj.getString("name"));
                                        editor.commit();

                                        startActivity(new Intent(getApplicationContext(), Home.class));
                                        finish();
                                    }


                                    // }
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

                        params.put("username", textUsername);
                        params.put("password", textPassword);
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