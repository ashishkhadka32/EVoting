package com.example.evoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.evoting.Url.UrlPath;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserLogin extends AppCompatActivity {
//    TextView txtRegister;
    TextInputEditText txtUid;
    AppCompatButton btnLogin;
    String strUid;
//    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        txtUid = findViewById(R.id.txtUid);

        btnLogin = findViewById(R.id.btnLogin);
        final ProgressBar progressBar = findViewById(R.id.progressbarLogin);

        

        btnLogin.setOnClickListener(view -> {
            if(Objects.requireNonNull(txtUid.getText()).toString().equals(""))
            {
                Toast.makeText(UserLogin.this, "Enter User Id", Toast.LENGTH_SHORT).show();
            }
            else
            {
                strUid = txtUid.getText().toString().trim();

                StringRequest request = new StringRequest(Request.Method.POST, UrlPath.UserLogin_Url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Logged in successfully"))
                        {
                            progressBar.setVisibility(view.VISIBLE);
                            btnLogin.setVisibility(view.INVISIBLE);
                            txtUid.setText("");
                            Toast.makeText(UserLogin.this, response, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(UserLogin.this, NumberOtp.class);
                            intent.putExtra("uid", strUid); // Pass the user ID to UserHome activity
                            startActivity(intent);
//                            editor.putString("isLoggedInUser","true");
//                            editor.commit();
                        }
                        else {
                            progressBar.setVisibility(view.GONE);
                            btnLogin.setVisibility(view.VISIBLE);
                            Toast.makeText(UserLogin.this, "Invalid user id", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, (VolleyError error) -> {
                    Toast.makeText(UserLogin.this, "user id invalid", Toast.LENGTH_LONG).show();
                }
                ) {

                    @NonNull
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("uid",strUid);
//                        params.put("password",strPassword);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(UserLogin.this);
                requestQueue.add(request);
            }
            });
//        sharedPreferences = getSharedPreferences("Login File",MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        if(sharedPreferences.getString("isLoggedInUser","").equals("true"))
//        {
//            startActivity(new Intent(UserLogin.this, UserHome.class));
//            finishAffinity();
//        }
    }
}