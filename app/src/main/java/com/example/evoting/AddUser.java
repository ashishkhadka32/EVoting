package com.example.evoting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddUser extends AppCompatActivity {
    TextInputEditText edtUid, edtUsername, edtContact, edtAddress, edtPassword, edtDob;
    AppCompatButton btnImg, btnUser;
    ImageView img;
    RadioGroup edtRadio;
    Bitmap bitmap;
    String encodeImageString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

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

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddUser.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Format the selected date to "yyyy-MM-dd" format
                        String formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        edtDob.setText(formattedDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(AddUser.this)
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
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uidText = edtUid.getText().toString().trim();
                String passwordText = edtPassword.getText().toString().trim();
                if (!isValidUID(uidText)) {
                    edtUid.setError("Invalid UID format. It must be at least 6 characters long and contain only letters and digits.");
                    return;
                }
                else if(edtUid.getText().toString().equals(""))
                {
                    edtUid.setError("Please enter Uid");
                } else if (edtUsername.getText().toString().equals("")) {
                    edtUsername.setError("Please enter username");
                } else if (edtContact.getText().toString().equals("") || edtContact.length()!=10 || !edtContact.getText().toString().startsWith("98")) {
                    edtContact.setError("Phone number must be of 10 digits and must start with 98");
                } else if (edtAddress.getText().toString().equals("")) {
                    edtAddress.setError("Please enter address");
                } else if (passwordText.equals("") || !isValidPassword(passwordText)) {
                    edtPassword.setError("Invalid password format. It must contain at least 8 characters, including both letters and numbers.");
                } else if (edtDob.getText().toString().equals("")) {
                    edtDob.setError("Pleaser enter your DOB");
                }
                else {
                    // Calculate age based on date of birth
                    String dob = edtDob.getText().toString();
                    int age = calculateAge(dob);

                    // Check if the age is greater than or equal to 16
                    if (age != -1 && age < 16) {
                        Toast.makeText(AddUser.this, "User must be at least 16 years old to vote", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int selectedRadioButtonId = edtRadio.getCheckedRadioButtonId();
                    if (selectedRadioButtonId != -1) {
                        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                        String selectedValue = selectedRadioButton.getText().toString();
                        uploaddatatodb(selectedValue);
                    } else {
                        Toast.makeText(AddUser.this, "Please select an option", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                showConfirmationDialog();
            }
            private int calculateAge(String dob) {
                // Parse the date of birth and calculate the age
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date birthDate = sdf.parse(dob);
                    Calendar dobCalendar = Calendar.getInstance();
                    dobCalendar.setTime(birthDate);

                    Calendar currentCalendar = Calendar.getInstance();
                    int age = currentCalendar.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);

                    // Adjust age if the birthdate has not occurred yet this year
                    if (currentCalendar.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                        age--;
                    }

                    return age;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return -1; // Return -1 in case of an error
                }
            }

            private void showConfirmationDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddUser.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to add this user?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Proceed with data upload
                        int selectedRadioButtonId = edtRadio.getCheckedRadioButtonId();
                        if (selectedRadioButtonId != -1) {
                            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                            String selectedValue = selectedRadioButton.getText().toString();
                            uploaddatatodb(selectedValue);
                        } else {
                            Toast.makeText(AddUser.this, "Please select an option", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, user canceled the operation
                    }
                });

                builder.create().show();
            }



            private boolean isValidUID(String uid) {
                // Define the regular expression pattern for UID validation
                String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{6,}$";
                return uid.matches(regex);
            }

            private boolean isValidPassword(String password) {
                // Define the regular expression pattern for password validation
                String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
                return password.matches(regex);
            }
        });

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
    private void uploaddatatodb(String selectedValue)
    {
        edtUid = findViewById(R.id.edtUid);
        edtUsername = findViewById(R.id.edtUsername);
        edtContact = findViewById(R.id.edtContact);
        edtAddress = findViewById(R.id.edtAddress);
        edtPassword = findViewById(R.id.edtPassword);
        edtDob = findViewById(R.id.edtDob);
        edtRadio = findViewById(R.id.edtRadio);

        final String uid = edtUid.getText().toString().trim();
        final String username = edtUsername.getText().toString().trim();
        final String contact = edtContact.getText().toString().trim();
        final String address = edtAddress.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();
        final String dob = edtDob.getText().toString().trim();



        StringRequest request = new StringRequest(Request.Method.POST, UrlPath.AddUser_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                edtUid.setText("");
                edtUsername.setText("");
                edtContact.setText("");
                edtAddress.setText("");
                edtPassword.setText("");
                edtDob.setText("");
                img.setImageResource(R.drawable.ic_launcher_foreground);
                Toast.makeText(AddUser.this, response.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddUser.this, AdminHome.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddUser.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
                map.put("uid",uid);
                map.put("username",username);
                map.put("contact",contact);
                map.put("address",address);
                map.put("password",password);
                map.put("dob",dob);
                map.put("gender",selectedValue);
                map.put("upload",encodeImageString);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

}