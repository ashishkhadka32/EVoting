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
import com.example.evoting.adapter.RecyclerViewAdapter;
import com.example.evoting.model.Candidate;
import com.example.evoting.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CandidateRecyclerView extends AppCompatActivity {
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<Candidate> lstCandidate;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_recycler_view);
        lstCandidate = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerviewid);
        jsonrequest();
    }

    private void jsonrequest() {
        request = new JsonArrayRequest(UrlPath.displayCandidate_Url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i=0; i<response.length(); i++)
                {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Candidate candidate = new Candidate();
                        candidate.setId(jsonObject.getInt("id"));
                        candidate.setName(jsonObject.getString("name"));
                        candidate.setAddress(jsonObject.getString("address"));
                        candidate.setContact(jsonObject.getString("contact"));
                        candidate.setParty(jsonObject.getString("party"));
                        candidate.setNominees(jsonObject.getString("nominees"));
                        candidate.setImage(jsonObject.getString("image"));
                        lstCandidate.add(candidate);


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                setuprecyclerview(lstCandidate);

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(CandidateRecyclerView.this);
        requestQueue.add(request);
    }
    private void setuprecyclerview(List<Candidate> lstCandidate) {
        CandidateRecyclerViewAdapter adapter = new CandidateRecyclerViewAdapter(this,lstCandidate);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}