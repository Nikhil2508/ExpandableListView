package com.nikhil.expandablelistviewproject;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView view;
    ExpandableAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        view.setAdapter(listAdapter);

        view.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(final ExpandableListView expandableListView, View view, final int i, long l) {

                final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Getting child categories....");
                dialog.setCancelable(false);
                dialog.show();
// ...

// Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url ="https://reqres.in/api/users?page=2";

// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {


                            @Override
                            public void onResponse(String response) {

                                // Display the first 500 characters of the response string.

                                try {
                                    JSONObject res = new JSONObject(response);
                                    JSONArray data = res.getJSONArray("data");
                                    List<String> childDynamic = new ArrayList<>();
                                    for (int j = 0; j < data.length(); j++) {
                                        childDynamic.add(data.getJSONObject(j).getString("first_name"));
                                    }

                                    listDataChild.put(listDataHeader.get(i), childDynamic);
                                    listAdapter.notifyDataSetChanged();

                                    dialog.dismiss();



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.d(TAG, "onResponse: " + response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);

                return false;

            }
        });

    }
    /*
  * Preparing the list data
  */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Loading......");


        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Loading......");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("Loading......");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

}
