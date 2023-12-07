package com.example.evoting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddElection extends AppCompatActivity {
    TextInputEditText  startDateEditText, endDateEditText;
    Spinner edtElection;
    AppCompatButton btnAddElection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_election);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        edtElection = findViewById(R.id.edtElection);
        btnAddElection = findViewById(R.id.btnAddElection);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddElection.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);

                        Calendar today = Calendar.getInstance();
                        if (selectedDate.compareTo(today) >= 0) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            startDateEditText.setText(dateFormat.format(selectedDate.getTime()));
                        } else {
                            Toast.makeText(AddElection.this, "Start date must be greater than or equal to the current date", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddElection.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        endDateEditText.setText(dateFormat.format(selectedDate.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        startDateEditText.addTextChangedListener(new DateTextWatcher());
        endDateEditText.addTextChangedListener(new DateTextWatcher());

        btnAddElection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDateEditText.getText().toString().equals("")) {
                    startDateEditText.setError("Please enter start date of election");
                } else if (endDateEditText.getText().toString().equals("")) {
                    endDateEditText.setError("Please enter end date of the election");
                } else if (edtElection.getSelectedItem().toString().equals("Select Election")) {
                    Toast.makeText(AddElection.this, "Select the election", Toast.LENGTH_SHORT).show();
                } else {
                    String electionType = edtElection.getSelectedItem().toString();

                    if (isStartDateGreaterThanEndDate()) {
                        storeElectionDetails(electionType);
                        showConfirmationDialog();

                    } else {
                        Toast.makeText(AddElection.this, "Start date must be greater than end date", Toast.LENGTH_SHORT).show();
                    }
                }
//                showConfirmationDialog();
            }
            private void showConfirmationDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddElection.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to add this user?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadtodb();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.create().show();

            }
        });
    }

    public class DateTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            validateDates();
        }

        private void validateDates() {
            String startDateString = startDateEditText.getText().toString();
            String endDateString = endDateEditText.getText().toString();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            if (!startDateString.isEmpty() && !endDateString.isEmpty()) {
                try {
                    Date startDate = dateFormat.parse(startDateString);
                    Date endDate = dateFormat.parse(endDateString);

                    if (endDate.before(startDate)) {
                        endDateEditText.setError("End Date must be after Start Date");
                    } else {
                        // Dates are valid, clear any error messages
                        endDateEditText.setError(null);
                    }
                } catch (ParseException e) {
                    endDateEditText.setError("Invalid date format");
                }
            }
        }
    }

    private boolean isStartDateGreaterThanEndDate() {
        String startDateString = startDateEditText.getText().toString();
        String endDateString = endDateEditText.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            return endDate.after(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void uploadtodb() {
        edtElection = findViewById(R.id.edtElection);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);

        final String election = edtElection.getSelectedItem().toString();
        final String startDate = startDateEditText.getText().toString().trim();
        final String endDate = endDateEditText.getText().toString().trim();

        if (startDate.equals(endDate)) {
            deleteAllCandidates();
        }

        StringRequest request = new StringRequest(Request.Method.POST, UrlPath.AddElection_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                startDateEditText.setText("");
                endDateEditText.setText("");
                Toast.makeText(AddElection.this, response.toString(), Toast.LENGTH_LONG).show();
                // Set the "election_date_added" flag to true after successfully adding an election
                setElectionDateAddedFlag();
                // After adding the election, navigate to the AddCandidate activity
                Intent intent = new Intent(AddElection.this, AdminHome.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddElection.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("election", election);
                map.put("startDate", startDate);
                map.put("endDate", endDate);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    private void deleteAllCandidates() {
        StringRequest deleteRequest = new StringRequest(Request.Method.POST, UrlPath.DeleteAllCandidates_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Handle the response after candidates are deleted
                Toast.makeText(AddElection.this, "All candidates deleted", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                Toast.makeText(AddElection.this, "Error deleting candidates: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(deleteRequest);
    }

    private void setElectionDateAddedFlag() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("election_date_adds", true);
        editor.apply();
    }

    private void storeElectionDetails(String electionType) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("election_type_", electionType);
        // Store the first letter of the election type
        if (!electionType.isEmpty()) {
            editor.putString("election_type_add_first_", String.valueOf(electionType.charAt(0)));
        }
        editor.apply();
    }

}