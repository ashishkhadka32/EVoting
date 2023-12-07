package com.example.evoting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class UserRegister extends AppCompatActivity {
    TextInputEditText edtUser, edtEmail, edtContact, edtAdderss, edtPassword;
    AppCompatButton btnRegister;
    String strUser, strEmail, strContact, strAddress, strPassword;
    String url = "http://192.168.1.79/E-voting/Register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        edtUser = findViewById(R.id.edtUser);
        edtEmail = findViewById(R.id.edtEmail);
        edtContact = findViewById(R.id.edtContact);
        edtAdderss = findViewById(R.id.edtAddress);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtUser.getText().toString().equals(""))
                {
                    Toast.makeText(UserRegister.this, "Enter Username", Toast.LENGTH_SHORT).show();
                } else if (edtEmail.getText().toString().equals("")) {
                    Toast.makeText(UserRegister.this, "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (edtContact.getText().toString().equals("")) {
                    Toast.makeText(UserRegister.this, "Enter Contact", Toast.LENGTH_SHORT).show();
                } else if (edtAdderss.getText().toString().equals("")) {
                    Toast.makeText(UserRegister.this, "", Toast.LENGTH_SHORT).show();
                } else if (edtPassword.getText().toString().equals("")) {
                    Toast.makeText(UserRegister.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    strUser = edtUser.getText().toString().trim();
                    strEmail = edtEmail.getText().toString().trim();
                    strContact = edtContact.getText().toString().trim();
                    strAddress = edtAdderss.getText().toString().trim();
                    strPassword = edtPassword.getText().toString().trim();

                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(UserRegister.this, "Registration successfull", Toast.LENGTH_SHORT).show();
                            Log.e("error", "onResponse: "+response );
                        }
                    },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(UserRegister.this, "Error: error1" , Toast.LENGTH_SHORT).show();
                    }
                }
                ) {

                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("user",strUser);
                            params.put("email",strEmail);
                            params.put("contact",strContact);
                            params.put("address",strAddress);
                            params.put("password",strPassword);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(UserRegister.this);
                    requestQueue.add(request);
                }
            }
        });

    }
}