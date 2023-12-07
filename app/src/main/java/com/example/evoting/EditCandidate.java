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
import android.widget.Spinner;
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
import com.example.evoting.model.Candidate;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class EditCandidate extends AppCompatActivity {
    TextInputEditText edtName;
    TextInputEditText edtAddress;
    TextInputEditText edtContact;
    TextInputEditText edtParty;
    AppCompatButton btnUpdateCandidate, btnImg;
    Spinner edtNominees;
    ImageView img;
    Bitmap bitmap;
    private int id;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_candidate);


        edtName = findViewById(R.id.edtName);
        edtAddress = findViewById(R.id.edtAddress);
        edtContact = findViewById(R.id.edtContact);
        edtParty = findViewById(R.id.edtParty);
        edtNominees = findViewById(R.id.edtNominees);
        img = findViewById(R.id.img);
        btnImg = findViewById(R.id.btnImg);
        btnUpdateCandidate = findViewById(R.id.btnUpdateCandidate);

        Intent intent = getIntent();
       id = intent.getIntExtra("id", 0);
        String name = intent.getStringExtra("name");
        String address = intent.getStringExtra("address");
        String contact = intent.getStringExtra("contact");
        String party = intent.getStringExtra("party");
        String nominees = intent.getStringExtra("nominees");
        String img = intent.getStringExtra("upload");

        edtName.setText(name);
        edtAddress.setText(address);
        edtContact.setText(contact);
        edtParty.setText(party);

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }


        });


        btnUpdateCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCandidate();
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
    private void saveCandidate() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String name = edtName.getText().toString();
        String address = edtAddress.getText().toString();
        String contact = edtContact.getText().toString();
        String party = edtParty.getText().toString();
        String nominees = edtNominees.getSelectedItem().toString();

        // Additional Validation Checks
        if (name.isEmpty() || address.isEmpty() || contact.isEmpty() || contact.length() != 10 || party.isEmpty() || nominees.equals("Select Nominees")) {
            Toast.makeText(EditCandidate.this, "Please fill in all details and select valid nominees", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate Contact starts with "98"
        if (!contact.startsWith("98")) {
            Toast.makeText(EditCandidate.this, "Contact must start with '98'", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate Candidate name does not contain numeric value
        if (name.matches(".*\\d.*")) {
            Toast.makeText(EditCandidate.this, "Candidate name cannot contain numeric value", Toast.LENGTH_SHORT).show();
            return;
        }


        if (bitmap!=null) {
//            bitmap = resizeBitmap(bitmap, 800, 600);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytesofimage = byteArrayOutputStream.toByteArray();
            final String base64Img = Base64.encodeToString(bytesofimage, Base64.DEFAULT);
            StringRequest request = new StringRequest(Request.Method.POST, UrlPath.EditCandidate_Url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("edit", "onResponse: "+response);
                    if (response.equals("1")) {
                        Toast.makeText(EditCandidate.this, "Candidate details updated", Toast.LENGTH_SHORT).show();
                    } else {
                        // Failed to update candidate details
                        Toast.makeText(EditCandidate.this, "Failed to update candidate details", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EditCandidate.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id", String.valueOf(id));
                    map.put("name", name);
                    map.put("address", address);
                    map.put("contact", contact);
                    map.put("party", party);
                    map.put("nominees", nominees);
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

//    private Bitmap resizeBitmap(Bitmap bitmap, int i, int i1) {
//        float scaleWidth = ((float) i) / bitmap.getWidth();
//        float scaleHeight = ((float) i1) / bitmap.getHeight();
//        // Create a matrix for the scaling
//        android.graphics.Matrix matrix = new android.graphics.Matrix();
//        // Resize the bitmap
//        matrix.postScale(scaleWidth, scaleHeight);
//        // Recreate the new bitmap
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//    }

}