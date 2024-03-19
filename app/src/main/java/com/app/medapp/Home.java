package com.app.medapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    String[] id, name, phaarmacyname, stock, file, price, size, type, rating, latitude, longitude;
    SharedPreferences sh;
    EditText ed;
    FloatingActionButton btn, complaintbtn;
    ListView listView;
    Snackbar snackbar;
    ImageView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        ed = findViewById(R.id.editTextTextPersonName3);
        btn = findViewById(R.id.floatingActionButton2);

        empty = findViewById(R.id.imageView10);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        listView = findViewById(R.id.list);
        getSupportActionBar().setTitle("Hai "+sh.getString("name", ""));
        complaintbtn = findViewById(R.id.floatingActionButton4);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().setTitle("MedApp");
            }
        }, 5000);

        snackbar = Snackbar.make(findViewById(android.R.id.content), "Press back Again to Exit", Snackbar.LENGTH_SHORT);


        if (sh.getString("count","").equalsIgnoreCase("")){
            Snackbar.make(findViewById(android.R.id.content), "Welcome", Snackbar.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sh.edit();
            editor.putString("count", "1");
            editor.commit();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = ed.getText().toString();
                if (search.isEmpty()){
                    Snackbar.make(view, "Type something first!", Snackbar.LENGTH_LONG)
                            .setAction("Ok", null).setActionTextColor(getResources().getColor(R.color.white))
                            .show();
                }

                String url = sh.getString("url", "")+"/and_search_medicine";

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
                                        name=new String[js.length()];
                                        phaarmacyname=new String[js.length()];
                                        stock=new String[js.length()];
                                        file=new String[js.length()];
                                        price=new String[js.length()];
                                        size=new String[js.length()];
                                        type=new String[js.length()];
                                        rating=new String[js.length()];
                                        latitude=new String[js.length()];
                                        longitude=new String[js.length()];
//

                                        for(int i=0;i<js.length();i++)
                                        {
                                            JSONObject u=js.getJSONObject(i);
                                            id[i]=u.getString("id");
                                            name[i]=u.getString("name");
                                            phaarmacyname[i]=u.getString("pharmacyname");
                                            stock[i]=u.getString("stock");
                                            file[i]=u.getString("file");
                                            price[i]=u.getString("price");
                                            size[i]=u.getString("size");
                                            type[i]=u.getString("type");
                                            rating[i]=u.getString("rating");
                                            latitude[i]=u.getString("latitude");
                                            longitude[i]=u.getString("longitude");


                                        }
                                        if(id.length !=0){
                                            empty.setVisibility(View.GONE);

                                        }
                                        else {
                                            empty.setVisibility(View.VISIBLE);
                                        }

                                        // ArrayAdapter<String> adpt=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,name);
                                        listView.setAdapter(new CustomViewmedicine(getApplicationContext(),id, name,phaarmacyname, stock, file, price, rating, type, latitude, longitude));
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
                        params.put("search",search);
//

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long idd) {
                Intent i = new Intent(getApplicationContext(), ViewMedicine.class);
                i.putExtra("name", name[position]);
                i.putExtra("file", file[position]);
                i.putExtra("fname", phaarmacyname[position]);
                i.putExtra("stock", stock[position]);
                i.putExtra("price", price[position]);
                i.putExtra("id", id[position]);
                i.putExtra("size", size[position]);
                i.putExtra("type", type[position]);
                i.putExtra("latitude", latitude[position]);
                i.putExtra("longitude", longitude[position]);

                startActivity(i);
            }
        });
        complaintbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewComplaints.class));
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_cart){
            startActivity(new Intent(getApplicationContext(), ViewCart.class));
        }
        if (id == R.id.action_logout){
            new AlertDialog.Builder(Home.this)
                    .setTitle("Have a nice day!")
                    .setMessage("Are you sure you want to logout?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor ed = sh.edit();
                            ed.remove("name");
                            ed.remove("lid");
                            ed.commit();
                            finish();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }
                    }).show();
        }

        if (id == R.id.action_notifications){
            startActivity(new Intent(getApplicationContext(), PaymentHistory.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        checkCart(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    private void checkCart(Menu menu) {

        String url = sh.getString("url", "")+"/and_cart_check";

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
                                menu.findItem(R.id.action_cart).setIcon(R.drawable.baseline_shopping_basket_24);

                            }
                            else if(jsonObj.getString("status").equalsIgnoreCase("no")) {
                                menu.findItem(R.id.action_cart).setIcon(R.drawable.ic_baseline_shopping_cart_24);
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
    public void onBackPressed() {
        if (snackbar.isShown()){
            super.onBackPressed();
        }
        else {
            snackbar.show();
        }

    }
}