package com.app.medapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewComplaints extends AppCompatActivity {
    String [] complaint, date, reply, replydate;
    SharedPreferences sh;
    ListView listView;
    FloatingActionButton btn;
    ImageView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaints);
        listView = findViewById(R.id.list);
        btn = findViewById(R.id.floatingActionButton6);
        empty = findViewById(R.id.imageView7);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        String url = sh.getString("url", "")+"/and_view_complaints";
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
                                complaint=new String[js.length()];
                                date=new String[js.length()];
                                reply=new String[js.length()];
                                replydate=new String[js.length()];
//                                image=new String[js.length()];
//                                status=new String[js.length()];
//
//                                JSONArray js1= jsonObj.getJSONArray("rating");
//                                rating=new String[js1.length()];

                                for(int i=0;i<js.length();i++)
                                {
                                    JSONObject u=js.getJSONObject(i);
                                    complaint[i]=u.getString("complaint");
                                    date[i]=u.getString("cdate");
                                    reply[i]=u.getString("reply");
                                    replydate[i]=u.getString("rdate");
//                                    type[i]=u.getString("type");
//                                    discription[i]=u.getString("description");
//                                    image[i]=u.getString("image");
//                                    status[i]=u.getString("status");


                                }
                                if (complaint.length !=0){
                                    empty.setVisibility(View.GONE);

                                }
                                else {
                                    empty.setVisibility(View.VISIBLE);
                                }


                                // ArrayAdapter<String> adpt=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,name);
                                listView.setAdapter(new CustomViewComplaints(getApplicationContext(),complaint,date, reply, replydate));
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SendComplaint.class));
            }
        });
    }

    @Override
    protected void onRestart() {
        recreate();
        super.onRestart();
    }
}