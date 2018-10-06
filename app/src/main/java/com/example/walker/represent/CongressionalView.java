package com.example.walker.represent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CongressionalView extends AppCompatActivity {
    String ZIP_CODE;
    String API_KEY = "53b7c6a7abaa6bb6fcbb8bcbb2a7653b3866ca5";
    List<Representative> representatives = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional_view);

        TextView location = (TextView) findViewById(R.id.location);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        ZIP_CODE = getIntent().getExtras().getString("zip_code");
        location.setText("Zip: " + ZIP_CODE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.geocod.io/v1.3/geocode?postal_code=" + ZIP_CODE + "&fields=cd&api_key=" + API_KEY;

        // Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response_str) {
                        JSONArray current_legislators = parseReps(response_str);
                        if (current_legislators != null) {
                            for(int i = 0; i < current_legislators.length(); i++) {
                                try {
                                    JSONObject rep = current_legislators.getJSONObject(i);
                                    representatives.add(new Representative(getRepInfo(rep)));
                                    RVAdapter adapter = new RVAdapter(representatives);
                                    rv.setAdapter(adapter);
                                } catch (JSONException e) {
                                    System.out.println("JSON PARSE FAILED");
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("No response received");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static JSONArray parseReps(String response_str) {
        JSONArray current_legislators = null;
        try {
            JSONObject response = new JSONObject(response_str);
            JSONArray results = (JSONArray) response.get("results");
            JSONObject fields = (JSONObject) results.getJSONObject(0).get("fields");
            JSONArray congressional_districts = (JSONArray) fields.get("congressional_districts");
            current_legislators = (JSONArray) congressional_districts.getJSONObject(0).get("current_legislators");

        } catch (JSONException e) {
            System.out.println("JSON PARSE FAILED");
        }
        return current_legislators;
    }

    public static Map<String, String> getRepInfo(JSONObject rep) {
        Map<String, String> repInfo = new HashMap<>();
        try {
            JSONObject bio = rep.getJSONObject("bio");
            JSONObject contact = rep.getJSONObject("contact");
            JSONObject references = rep.getJSONObject("references");

            repInfo.put("type", rep.getString("type"));
            repInfo.put("first_name", bio.getString("first_name"));
            repInfo.put("last_name", bio.getString("last_name"));
            repInfo.put("party", bio.getString("party"));
            repInfo.put("url", contact.getString("url"));
            repInfo.put("contact_form", contact.getString("contact_form"));
            repInfo.put("bioguide_id", references.getString("bioguide_id"));
        } catch (JSONException e) {
            System.out.println("GET REP INFO FAILED");
        }
        return repInfo;
    }
}
