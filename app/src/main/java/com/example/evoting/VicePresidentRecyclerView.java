package com.example.evoting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.example.evoting.Url.UrlPath;
import com.example.evoting.adapter.PresidentRecyclerViewAdapter;
import com.example.evoting.adapter.VicePresidentRecyclerViewAdapter;
import com.example.evoting.model.President;
import com.example.evoting.model.VicePresident;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VicePresidentRecyclerView extends AppCompatActivity {

   private JsonArrayRequest request;
   private RequestQueue requestQueue;
   private List<VicePresident> lstVicePresident;
   private RecyclerView recyclerView;
    private EditText searchEditText;
    private VicePresidentRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vice_president_recycler_view);

        lstVicePresident = new ArrayList<>();

        recyclerView = findViewById(R.id.vicePresidentRecyclerviewid);
        searchEditText = findViewById(R.id.searchEditText);
        jsonRequest();

    }

    private void jsonRequest() {
        request = new JsonArrayRequest(UrlPath.displayVicePresident_Url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i<response.length(); i++)
                {
                    try {
                        jsonObject = response.getJSONObject(i);
                        VicePresident vicePresident = new VicePresident();
                        vicePresident.setId(jsonObject.getInt("id"));
                        vicePresident.setName(jsonObject.getString("name"));
                        vicePresident.setParty(jsonObject.getString("party"));
                        vicePresident.setNominees(jsonObject.getString("nominees"));
                        vicePresident.setImage(jsonObject.getString("image"));
                        lstVicePresident.add(vicePresident);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                setUpRecyclerView(lstVicePresident);
                setupSearchListener(lstVicePresident);

                }




        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VicePresidentRecyclerView.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(VicePresidentRecyclerView.this);
        requestQueue.add(request);
    }
    private void setUpRecyclerView(List<VicePresident> lstVicePresident) {
        String uid = getIntent().getStringExtra("uid");
        adapter = new VicePresidentRecyclerViewAdapter(this, lstVicePresident, uid); // Use the existing 'adapter' variable
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    private void setupSearchListener(List<VicePresident> lstVicePresident) {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterList(charSequence.toString(), lstVicePresident);
            }



            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    private void filterList(String searchText, List<VicePresident> originalList) { // Change parameter name here
        List<VicePresident> filteredList = new ArrayList<>();
        for (VicePresident vicePresident : originalList) {
            if (vicePresident.getName().toLowerCase().contains(searchText.toLowerCase())
                    || vicePresident.getParty().toLowerCase().contains(searchText.toLowerCase())
                    || vicePresident.getNominees().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(vicePresident);
            }
        }
        adapter.setData(filteredList);
    }
}