package com.example.evoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class GetOtp extends AppCompatActivity {
    EditText inputotp1, inputotp2, inputotp3, inputotp4, inputotp5, inputotp6;
    TextView showMobile;
    AppCompatButton btnSubmit;

    String getotpbackend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_otp);
        inputotp1 = findViewById(R.id.inputotp1);
        inputotp2 = findViewById(R.id.inputotp2);
        inputotp3 = findViewById(R.id.inputotp3);
        inputotp4 = findViewById(R.id.inputotp4);
        inputotp5 = findViewById(R.id.inputotp5);
        inputotp6 = findViewById(R.id.inputotp6);
        showMobile = findViewById(R.id.showMobile);
        btnSubmit = findViewById(R.id.btnSubmit);

        final ProgressBar progressBar = findViewById(R.id.progressbarReceiveOtp);

        showMobile.setText(String.format(
                "+977-%s",getIntent().getStringExtra("mobile")
        ));
        getotpbackend = getIntent().getStringExtra("backendotp");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inputotp1.getText().toString().trim().isEmpty() && !inputotp2.getText().toString().trim().isEmpty() && !inputotp3.getText().toString().trim().isEmpty()
                        && !inputotp4.getText().toString().trim().isEmpty() && !inputotp5.getText().toString().trim().isEmpty() && !inputotp6.getText().toString().trim().isEmpty())
                {
//                    Toast.makeText(GetOtp.this, "otp verify", Toast.LENGTH_SHORT).show();
                    String entercodeotp = inputotp1.getText().toString() +
                            inputotp2.getText().toString() +
                            inputotp3.getText().toString()+
                            inputotp4.getText().toString()+
                            inputotp5.getText().toString()+
                            inputotp6.getText().toString();
                    if(getotpbackend!=null)
                    {
                        progressBar.setVisibility(view.VISIBLE);
                        btnSubmit.setVisibility(view.INVISIBLE);

                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                getotpbackend, entercodeotp
                        );
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(view.GONE);
                                        btnSubmit.setVisibility(view.VISIBLE);

                                        if (task.isSuccessful()){
                                            Intent intent = new Intent(getApplicationContext(), UserHome.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.putExtra("uid", getIntent().getStringExtra("uid"));
                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(GetOtp.this, "Enter correct OTP", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }
                    else {
                        Toast.makeText(GetOtp.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(GetOtp.this, "please enter all number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        numberotpmove();

        findViewById(R.id.textResendOtp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+977" + getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        GetOtp.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(GetOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String newbackendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                getotpbackend = newbackendotp;
                                Toast.makeText(GetOtp.this, "Otp send successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                );
            }
        });


    }

    private void numberotpmove() {
        inputotp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                {
                    inputotp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputotp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                {
                    inputotp3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputotp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                {
                    inputotp4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputotp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                {
                    inputotp5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputotp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().trim().isEmpty())
                {
                    inputotp6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}