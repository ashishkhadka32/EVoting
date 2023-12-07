package com.example.evoting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.evoting.Url.UrlPath;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdminLogin extends AppCompatActivity {
    private TextInputEditText edtEmail, edtPassword;
    private AppCompatButton btnLogin;

    private FirebaseAuth auth;

    private int failedLoginAttempts = 1;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        sharedPreferences = getSharedPreferences("Login File", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getString("isLoggedIn", "").equals("true")) {
            startActivity(new Intent(AdminLogin.this, AdminHome.class));
            finishAffinity();
        }

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        final ProgressBar progressBar = findViewById(R.id.progressbarAdminLogin);
        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(edtEmail.getText()).toString().equals("")) {
                    Toast.makeText(AdminLogin.this, "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(edtPassword.getText()).toString().equals("")) {
                    Toast.makeText(AdminLogin.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    // Show progress bar
                    progressBar.setVisibility(View.VISIBLE);

                    String txt_email = edtEmail.getText().toString();
                    String txt_password = edtPassword.getText().toString();
                    loginUser(txt_email, txt_password, progressBar);
                }
            }
        });
    }

    private void loginUser(String email, String password, ProgressBar progressBar) {
        // Hide the login button and show the progress bar
        btnLogin.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Reset failed login attempts on successful login
                        failedLoginAttempts = 0;

                        if (auth.getCurrentUser() != null && auth.getCurrentUser().isEmailVerified()) {
                            // Hide progress bar
                            progressBar.setVisibility(View.GONE);

                            // Update shared preferences
                            editor.putString("isLoggedIn", "true");
                            editor.apply();

                            startActivity(new Intent(AdminLogin.this, AdminHome.class));
                            Toast.makeText(AdminLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            finishAffinity();
                        } else {
                            // Hide progress bar
                            progressBar.setVisibility(View.GONE);

                            // Show login button
                            btnLogin.setVisibility(View.VISIBLE);

                            Toast.makeText(AdminLogin.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                            sendVerificationEmail();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);

                    // Show login button
                    btnLogin.setVisibility(View.VISIBLE);

                    // Handle exceptions here
                    failedLoginAttempts++;

                    // Check if the failure is due to an incorrect password
                    if (e.getMessage() != null && e.getMessage().contains("password is invalid")) {
                        // Show remaining attempts in a dialog box
                        int remainingAttempts = MAX_FAILED_ATTEMPTS - failedLoginAttempts + 1;
                        showRemainingAttemptsDialog(remainingAttempts);
                    } else {
                        // Show other failure messages
                        Toast.makeText(AdminLogin.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showRemainingAttemptsDialog(int remainingAttempts) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Login Failed");

        if (remainingAttempts > 0) {
            builder.setMessage("Invalid password. " + remainingAttempts + " attempts left.");
        } else {
            builder.setMessage("Reset Password link has been sent to your Gmail account.");
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Clear the password EditText when OK is pressed
                edtPassword.setText("");

                // Check if the maximum attempts are reached
                if (remainingAttempts <= 0) {
                    // Redirect to the password reset screen
                    resetPassword(edtEmail.getText().toString());
                }
            }
        });

        builder.create().show();
    }


    private void sendVerificationEmail() {
        // Check if the user is logged in
        if (auth.getCurrentUser() != null) {
            auth.getCurrentUser().sendEmailVerification()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AdminLogin.this, "Verification email sent. Please check your email.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle exceptions here
                        Toast.makeText(AdminLogin.this, "Failed to send verification email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
    private void resetPassword(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AdminLogin.this, "Password reset email sent. Please check your email.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle exceptions here
                    Toast.makeText(AdminLogin.this, "Failed to send password reset email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}