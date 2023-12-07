package com.example.evoting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.evoting.Url.UrlPath;
import com.example.evoting.adapter.PresidentVoteCountRecyclerViewAdapter;
import com.example.evoting.adapter.VoteCountRecyclerViewAdapter;
import com.example.evoting.model.President;
import com.example.evoting.model.VicePresident;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PresidentVoteCountRecycler extends AppCompatActivity {
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<President> voteCounts;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_president_vote_count_recycler);
        voteCounts = new ArrayList<>();
        recyclerView = findViewById(R.id.presidentVoteRecyclerviewid);
        // Set a LinearLayoutManager for the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jsonRequest();
    }
    private void jsonRequest() {
        request = new JsonArrayRequest(UrlPath.presidentVoteCount_Url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        President voteCount = new President();
                        voteCount.setName(jsonObject.getString("name"));
                        voteCount.setVoteCount(jsonObject.getInt("count"));
                        voteCounts.add(voteCount);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setUpRecyclerView(voteCounts);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Verror", "onErrorResponse: "+error.getMessage());
                Toast.makeText(PresidentVoteCountRecycler.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(PresidentVoteCountRecycler.this);
        requestQueue.add(request);
    }

    private void setUpRecyclerView(List<President> voteCounts) {
        Collections.sort(voteCounts, new Comparator<President>() {
            @Override
            public int compare(President p1, President p2) {
                // Compare in descending order
                return Integer.compare(p2.getVoteCount(), p1.getVoteCount());
            }
        });
        PresidentVoteCountRecyclerViewAdapter adapter = new PresidentVoteCountRecyclerViewAdapter(this, voteCounts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}