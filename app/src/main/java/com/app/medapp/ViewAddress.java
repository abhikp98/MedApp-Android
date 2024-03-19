package com.app.medapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
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

public class ViewAddress extends AppCompatActivity {
    String[] housename, place, post, pin, id;
    SharedPreferences sh;
    ListView listView;
    FloatingActionButton addbtn;
    ImageView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_address);
        listView = findViewById(R.id.list);
        addbtn = findViewById(R.id.floatingActionButton5);
        empty = findViewById(R.id.imageView17);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = sh.getString("url", "")+"/and_view_address";
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
                                housename=new String[js.length()];
                                place=new String[js.length()];
                                post=new String[js.length()];
                                pin=new String[js.length()];
                                id=new String[js.length()];


                                for(int i=0;i<js.length();i++)
                                {
                                    JSONObject u=js.getJSONObject(i);
                                    housename[i]=u.getString("house");
                                    place[i]=u.getString("place");
                                    post[i]=u.getString("post");
                                    pin[i]=u.getString("pin");
                                    id[i]=u.getString("id");
//                                    image[i]=u.getString("image");
//                                    status[i]=u.getString("status");


                                }

                                // ArrayAdapter<String> adpt=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,name);
                                listView.setAdapter(new CustomViewAddress(ViewAddress.this, housename,place, post, pin, id));
                                // l1.setAdapter(new Custom(getApplicationContext(),gamecode,name,type,discription,image,status));

                                if (id.length!= 0){
                                    empty.setVisibility(View.GONE);
                                }
                                else {
                                    empty.setVisibility(View.VISIBLE);
                                }
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long idd) {
                new AlertDialog.Builder(view.getRootView().getContext())
                        .setTitle("Select Address")
                        .setMessage("Are you sure to select this as your Delivery Address?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            SharedPreferences.Editor editor = sh.edit();
                            editor.putString("housename", housename[position]);
                            editor.putString("place", place[position]);
                            editor.putString("post", post[position]);
                            editor.putString("pin", pin[position]);
                            editor.putString("addid", id[position]);
                            editor.commit();
                            finish();
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddAddress.class));
            }
        });
    }

    @Override
    protected void onRestart() {
        recreate();
        super.onRestart();
    }
}