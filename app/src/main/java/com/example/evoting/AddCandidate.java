package com.example.evoting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.UriMatcherCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
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
import com.example.evoting.Url.UrlPath;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddCandidate extends AppCompatActivity {
    TextInputEditText edtName, edtAddress, edtContact, edtParty;
    AppCompatButton btnAddCandidate, btnImg;
    Spinner edtNominees;
    ImageView img;
    Bitmap bitmap;
    String encodeImageString;
    String selectedElectionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate);
        edtName = findViewById(R.id.edtName);
        edtAddress = findViewById(R.id.edtAddress);
        edtContact = findViewById(R.id.edtContact);
        edtParty = findViewById(R.id.edtParty);
        edtNominees = findViewById(R.id.edtNominees);
        img = findViewById(R.id.img);
        btnImg = findViewById(R.id.btnImg);
        btnAddCandidate = findViewById(R.id.btnAddCandidate);

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withContext(AddCandidate.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        btnAddCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isElectionDateAdd()) {
                    Toast.makeText(AddCandidate.this, "An election date must be added first", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the election type and year match before adding the candidate
                    String storedFirstLetter = getStoredElectionFirstLetter();
                    String selectedFirstLetter = getSelectedElectionFirstLetter();

                    if (selectedFirstLetter.equals(storedFirstLetter)) {
                        // Check conditions for adding a candidate
                        if (!isValidCandidateName(edtName.getText().toString())) {
                            Toast.makeText(AddCandidate.this, "Candidate name cannot contain numeric characters", Toast.LENGTH_SHORT).show();
                        } else if (edtName.getText().toString().isEmpty()) {
                            Toast.makeText(AddCandidate.this, "Enter Candidate Name", Toast.LENGTH_SHORT).show();
                        } else if (edtAddress.getText().toString().isEmpty()) {
                            Toast.makeText(AddCandidate.this, "Enter Address", Toast.LENGTH_SHORT).show();
                        } else if (edtContact.getText().toString().isEmpty() || edtContact.length() != 10 || !edtContact.getText().toString().startsWith("98")) {
                            Toast.makeText(AddCandidate.this, "Phone number must be of 10 digits and must start with 98", Toast.LENGTH_SHORT).show();
                        } else if (edtParty.getText().toString().isEmpty()) {
                            Toast.makeText(AddCandidate.this, "Enter Party Name", Toast.LENGTH_SHORT).show();
                        } else if (!isValidPartyName(edtParty.getText().toString())) {
                            Toast.makeText(AddCandidate.this, "Party name cannot contain numeric characters", Toast.LENGTH_SHORT).show();
                        } else {
                            uploaddatatodb();
                        }
                    } else {
                        Toast.makeText(AddCandidate.this, "Election type must be the same", Toast.LENGTH_SHORT).show();
                    }
//                    showConfirmationDialog();
                }
            }
//            private void showConfirmationDialog() {
//                AlertDialog.Builder builder = new AlertDialog.Builder(AddCandidate.this);
//                builder.setTitle("Confirmation");
//                builder.setMessage("Are you sure you want to add this user?");
//
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        uploaddatatodb();
//
//                    }
//                });
//
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Do nothing, user canceled the operation
//                    }
//                });
//
//                builder.create().show();
//            }
        });
    }
    private boolean isValidCandidateName(String name) {
        return !name.matches(".*\\d.*");
    }

    private boolean isValidPartyName(String name) {
        return !name.matches(".*\\d.*");
    }
    private boolean isElectionDateAdd() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("election_date_", false);
    }

    private String getStoredElectionFirstLetter() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("election_type_add_first_", "");
    }

    private String getSelectedElectionFirstLetter() {
        String selectedElectionType = edtNominees.getSelectedItem().toString();
        if (!selectedElectionType.isEmpty()) {
            return String.valueOf(selectedElectionType.charAt(0));
        }
        return "";
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            Uri filepath=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
            }
            catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void encodeBitmapImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytesofimage=byteArrayOutputStream.toByteArray();
        encodeImageString=android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }

    private void uploaddatatodb() {
        edtName = findViewById(R.id.edtName);
        edtAddress = findViewById(R.id.edtAddress);
        edtContact = findViewById(R.id.edtContact);
        edtParty = findViewById(R.id.edtParty);
        edtNominees = findViewById(R.id.edtNominees);

        final String name = capitalizeFirstLetter(edtName.getText().toString().trim());
        final String address = capitalizeFirstLetter(edtAddress.getText().toString().trim());
        final String contact = edtContact.getText().toString().trim();
        final String party = edtParty.getText().toString().trim();
        final String nominees = edtNominees.getSelectedItem().toString();


        StringRequest request = new StringRequest(Request.Method.POST, UrlPath.AddCandidate_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                edtName.setText("");
                edtAddress.setText("");
                edtContact.setText("");
                edtParty.setText("");
                img.setImageResource(R.drawable.ic_launcher_foreground);
                Toast.makeText(AddCandidate.this, response.toString(), Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddCandidate.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
                map.put("name",name);
                map.put("address",address);
                map.put("contact",contact);
                map.put("party",party);
                map.put("nominees",nominees);
                map.put("upload",encodeImageString);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

    }
    private String capitalizeFirstLetter(String input) {
        if (input.length() > 0) {
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        }
        return input;
    }
}