package com.example.evoting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.evoting.Url.UrlPath;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class EditUser extends AppCompatActivity {
    TextInputEditText edtUid, edtUsername, edtContact, edtAddress, edtPassword, edtDob;
    AppCompatButton btnImg, btnUser;
    ImageView img;
    RadioGroup edtRadio;
    Bitmap bitmap;
    private int id;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        edtUid = findViewById(R.id.edtUid);
        edtUsername = findViewById(R.id.edtUsername);
        edtContact = findViewById(R.id.edtContact);
        edtAddress = findViewById(R.id.edtAddress);
        edtPassword = findViewById(R.id.edtPassword);
        edtDob = findViewById(R.id.edtDob);
        edtRadio = findViewById(R.id.edtRadio);
        img = findViewById(R.id.img);
        btnImg = findViewById(R.id.btnImg);
        btnUser = findViewById(R.id.btnUser);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        String uid = intent.getStringExtra("uid");
        String name = intent.getStringExtra("name");
        String contact = intent.getStringExtra("contact");
        String address = intent.getStringExtra("address");
        String dob = intent.getStringExtra("dob");
        String gender = intent.getStringExtra("gender");
        String img = intent.getStringExtra("upload");

        edtUid.setText(uid);
        edtUsername.setText(name);
        edtContact.setText(contact);
        edtAddress.setText(address);
        edtDob.setText(dob);

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }


        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });

    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            // Load the selected image into the ImageView using Glide
            Glide.with(this)
                    .asBitmap()
                    .load(selectedImageUri)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(img);
            try {
                // Decode the selected image URI into a bitmap
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void saveUser() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String uid = edtUid.getText().toString();
        String name = edtUsername.getText().toString();
        String address = edtAddress.getText().toString();
        String contact = edtContact.getText().toString();
        String password = edtPassword.getText().toString();
        String dob = edtDob.getText().toString();

        // Validate that all fields are not empty
        if (uid.isEmpty() || name.isEmpty() || address.isEmpty() || contact.isEmpty() || contact.length() != 10 || password.isEmpty() || dob.isEmpty()) {
            Toast.makeText(EditUser.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validate Contact starts with "98"
        if (!contact.startsWith("98")) {
            Toast.makeText(EditUser.this, "Contact must start with '98'", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate Candidate name does not contain numeric value
        if (name.matches(".*\\d.*")) {
            Toast.makeText(EditUser.this, "Name cannot contain numeric value", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!uid.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{6,}$")){
            Toast.makeText(EditUser.this, "Invalid UID format. It must be at least 6 characters long and contain only letters and digits.", Toast.LENGTH_SHORT).show();
            return;
        }

        int gender = edtRadio.getCheckedRadioButtonId();
        if (gender != -1) {
            RadioButton selectedRadioButton = findViewById(gender);
            String genders = selectedRadioButton.getText().toString();
        } else {
            Toast.makeText(EditUser.this, "Please select an option", Toast.LENGTH_SHORT).show();
        }


        if (bitmap!=null) {
//            bitmap = resizeBitmap(bitmap, 800, 600);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytesofimage = byteArrayOutputStream.toByteArray();
            final String base64Img = Base64.encodeToString(bytesofimage, Base64.DEFAULT);
            StringRequest request = new StringRequest(Request.Method.POST, UrlPath.EditUser_Url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("edit", "onResponse: "+response);
                    if (response.equals("1")) {
                        Toast.makeText(EditUser.this, "Candidate details updated", Toast.LENGTH_SHORT).show();
                    } else {
                        // Failed to update candidate details
                        Toast.makeText(EditUser.this, "Failed to update candidate details", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EditUser.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id", String.valueOf(id));
                    map.put("uid",uid);
                    map.put("username", name);
                    map.put("address", address);
                    map.put("contact", contact);
                    map.put("password", password);
                    map.put("dob", dob);
                    if (gender != -1) {
                        RadioButton selectedRadioButton = findViewById(gender);
                        String genderValue = selectedRadioButton.getText().toString();
                        map.put("gender", genderValue);
                    } else {
                        // Handle if no gender is selected
                    }
                    map.put("upload", base64Img);

                    return map;
                }

            };
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        }
        else {
            Toast.makeText(this, "Failed to load the image", Toast.LENGTH_SHORT).show();
        }

    }

}