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
import com.example.evoting.adapter.VoteCountRecyclerViewAdapter;
import com.example.evoting.model.VicePresident;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VicepresidentVoteCountRecycler extends AppCompatActivity {

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<VicePresident> voteCounts;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vicepresident_vote_count_recycler);
        voteCounts = new ArrayList<>();
        recyclerView = findViewById(R.id.voteCountRecyclerView);
        // Set a LinearLayoutManager for the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jsonRequest();
    }

    private void jsonRequest() {
        request = new JsonArrayRequest(UrlPath.voteCount_Url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        VicePresident voteCount = new VicePresident();
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
                Toast.makeText(VicepresidentVoteCountRecycler.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(VicepresidentVoteCountRecycler.this);
        requestQueue.add(request);
    }

    private void setUpRecyclerView(List<VicePresident> voteCounts) {
        Collections.sort(voteCounts, new Comparator<VicePresident>() {
            @Override
            public int compare(VicePresident vp1, VicePresident vp2) {
                // Compare in descending order
                return Integer.compare(vp2.getVoteCount(), vp1.getVoteCount());
            }
        });

        VoteCountRecyclerViewAdapter adapter = new VoteCountRecyclerViewAdapter(this, voteCounts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
