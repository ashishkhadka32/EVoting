package com.example.evoting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.evoting.Url.UrlPath;
import com.example.evoting.adapter.CandidateRecyclerViewAdapter;
import com.example.evoting.adapter.ElectionRecyclerViewAdapter;
import com.example.evoting.model.Candidate;
import com.example.evoting.model.Election;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ElectionRecyclerView extends AppCompatActivity {
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<Election> lstElection;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election_recycler_view);

        lstElection = new ArrayList<>();

        recyclerView = findViewById(R.id.electionRecyclerviewid);
        jsonrequest();
    }

    private void jsonrequest() {
        request = new JsonArrayRequest(UrlPath.displayElection_Url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Election election = new Election();
                        election.setId(jsonObject.getInt("id"));
                        election.setElection(jsonObject.getString("election"));
                        election.setStartDate(jsonObject.getString("startDate"));
                        election.setEndDate(jsonObject.getString("endDate"));
                        lstElection.add(election);


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
                setuprecyclerview(lstElection);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(ElectionRecyclerView.this);
        requestQueue.add(request);
    }
    private void setuprecyclerview(List<Election> lstElection) {
        ElectionRecyclerViewAdapter adapter = new ElectionRecyclerViewAdapter(this,lstElection);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}