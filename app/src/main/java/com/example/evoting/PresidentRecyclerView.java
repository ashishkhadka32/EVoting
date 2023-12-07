package com.example.evoting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.evoting.Url.UrlPath;
import com.example.evoting.adapter.PresidentRecyclerViewAdapter;
import com.example.evoting.model.President;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PresidentRecyclerView extends AppCompatActivity {

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<President> lstPresident;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private PresidentRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_president_recycler_view);


        lstPresident = new ArrayList<>();

        recyclerView = findViewById(R.id.PresidentRecyclerviewid);
        searchEditText = findViewById(R.id.searchEditTextPresident);

        jsonrequest();
    }

    private void jsonrequest() {
        request = new JsonArrayRequest(UrlPath.displayPresident_Url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for(int i=0; i<response.length(); i++)
                {
                    try {
                        jsonObject = response.getJSONObject(i);
                        President president = new President();
                        president.setId(jsonObject.getInt("id"));
                        president.setName(jsonObject.getString("name"));
                        president.setParty(jsonObject.getString("party"));
                        president.setNominees(jsonObject.getString("nominees"));
                        president.setImage(jsonObject.getString("image"));
                        lstPresident.add(president);
                        // Log the president data here
                        Log.d("PresidentData", "Name: " + president.getName() + ", Party: " + president.getParty());

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                setuprecyclerview(lstPresident);
                setupSearchListener(lstPresident);

            }




        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PresidentRecyclerView.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(PresidentRecyclerView.this);
        requestQueue.add(request);
    }
    private void setuprecyclerview(List<President> lstPresident) {
            String uid = getIntent().getStringExtra("uid");
            adapter = new PresidentRecyclerViewAdapter(this, lstPresident, uid);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }


    private void setupSearchListener(List<President> lstPresident) {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterList(charSequence.toString(), lstPresident);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    private void filterList(String searchText, List<President> originalList) {
        List<President> filteredList = new ArrayList<>();
        for (President president : originalList) {
            if (president.getName().toLowerCase().contains(searchText.toLowerCase())
                    || president.getParty().toLowerCase().contains(searchText.toLowerCase())
                    || president.getNominees().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(president);
            }
        }
        adapter.setData(filteredList);
    }
}