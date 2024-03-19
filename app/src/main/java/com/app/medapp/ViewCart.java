package com.app.medapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

public class ViewCart extends AppCompatActivity {

    String[] medname, quantity, image, id, price, isStock;
    ListView listView;
    SharedPreferences sh;
    TextView total;
    String total_amount;
    Button placeorder;
    ImageView emptycart;
    boolean stockCheck = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        listView = findViewById(R.id.list);
        total = findViewById(R.id.textView16);
        placeorder = findViewById(R.id.button8);
        emptycart = findViewById(R.id.imageView5);


        String url = sh.getString("url", "")+"/and_view_cart";
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
                                medname=new String[js.length()];
                                quantity=new String[js.length()];
                                price=new String[js.length()];
                                image = new String[js.length()];
                                isStock=new String[js.length()];

                                for(int i=0;i<js.length();i++)
                                {
                                    JSONObject u=js.getJSONObject(i);
                                    id[i]=u.getString("id");
                                    medname[i]=u.getString("name");
                                    quantity[i]=u.getString("quantity");
                                    price[i]=u.getString("tprice");
                                    image[i]=u.getString("image");
                                    isStock[i]=u.getString("isStock");

                                    if (u.getString("isStock").equalsIgnoreCase("True")){
                                        stockCheck = true;
                                    }


                                }
                                if (id.length!= 0){
                                    emptycart.setVisibility(View.GONE);
                                    total_amount = jsonObj.getString("total");
                                    total.setText("Total to be paid "+ total_amount);
                                }
                                else {
                                    emptycart.setVisibility(View.VISIBLE);
                                    total.setText("Hurry up, add to cart fast!");
                                    placeorder.setText("Search Products");
                                    placeorder.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                        }
                                    });
                                }

//                                for(int i=0;i<js1.length();i++)
//                                {
//                                    JSONObject u=js1.getJSONObject(i);
//                                    rating[i]=u.getString("rating");
//
//                                }

                                // ArrayAdapter<String> adpt=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,name);
                                listView.setAdapter(new CustomViewCart(getApplicationContext(),id, medname, quantity, price, image, isStock));
                                // l1.setAdapter(new Custom(getApplicationContext(),gamecode,name,type,discription,image,status));
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
//                params.put("mac",maclis);

                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS=100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recreate();
            }
        });

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stockCheck){
                    Snackbar.make(findViewById(android.R.id.content), "Remove Products which are Out of Stock first", Snackbar.LENGTH_LONG).show();

                }
                else {
                    startActivity(new Intent(getApplicationContext(), SelectAddress.class).putExtra("amount", total_amount));
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        recreate();
        super.onRestart();
    }
}