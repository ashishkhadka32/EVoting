package com.example.evoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class NumberOtp extends AppCompatActivity {

    EditText inputMobile;
    AppCompatButton getOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_otp);
        inputMobile = findViewById(R.id.inputMobile);
        getOtp =findViewById(R.id.getOtp);
        final ProgressBar progressBar = findViewById(R.id.progressbarSendOtp);

        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inputMobile.getText().toString().trim().isEmpty())
                {
                    if((inputMobile.getText().toString().trim()).length()==10)
                    {
                        progressBar.setVisibility(view.VISIBLE);
                        getOtp.setVisibility(view.INVISIBLE);
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+977" + inputMobile.getText().toString(),
                                60,
                                TimeUnit.SECONDS,
                                NumberOtp.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        progressBar.setVisibility(view.GONE);
                                        getOtp.setVisibility(view.INVISIBLE);

                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressBar.setVisibility(view.GONE);
                                        getOtp.setVisibility(view.VISIBLE);
                                        Toast.makeText(NumberOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        progressBar.setVisibility(view.GONE);
                                        getOtp.setVisibility(view.VISIBLE);
                                        // Pass the uid to GetOtp activity
                                        Intent intent = new Intent(NumberOtp.this, GetOtp.class);
                                        intent.putExtra("uid", getIntent().getStringExtra("uid"));
                                        intent.putExtra("mobile", inputMobile.getText().toString());
                                        intent.putExtra("backendotp",backendotp);
                                        startActivity(intent);
                                    }
                                }

                        );



//                        Intent intent = new Intent(getApplicationContext(), GetOtp.class);
//                        intent.putExtra("mobile", inputMobile.getText().toString());
//                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(NumberOtp.this, "Enter correct number", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(NumberOtp.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}