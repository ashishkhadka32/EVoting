package com.example.evoting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.evoting.Url.UrlPath;
import com.example.evoting.adapter.RecyclerViewAdapter;
import com.example.evoting.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserRecyclerView extends AppCompatActivity {
     private JsonArrayRequest request;
     private RequestQueue requestQueue;
     private List<User> lstUser;
     private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recycler_view);

        lstUser = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerviewid);
        jsonrequest();
    }

    private void jsonrequest() {
        request = new JsonArrayRequest(UrlPath.displayUser_Url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i=0; i<response.length(); i++)
                {
                    try {
                        jsonObject = response.getJSONObject(i);
                        User user = new User();
                        user.setId(jsonObject.getInt("id"));
                        user.setUid(jsonObject.getString("uid"));
                        user.setUsername(jsonObject.getString("username"));
                        user.setContact(jsonObject.getString("contact"));
                        user.setAddress(jsonObject.getString("address"));
                        user.setDob(jsonObject.getString("dob"));
                        user.setGender(jsonObject.getString("gender"));
                        user.setImage(jsonObject.getString("image"));
                        lstUser.add(user);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                setuprecyclerview(lstUser);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(UserRecyclerView.this);
        requestQueue.add(request);
    }
    private void setuprecyclerview(List<User> lstUser) {
        RecyclerViewAdapter myadapter = new RecyclerViewAdapter(this,lstUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);
    }
}