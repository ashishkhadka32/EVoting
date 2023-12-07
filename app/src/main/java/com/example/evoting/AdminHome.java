package com.example.evoting;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import com.example.evoting.model.VicePresident;

import java.util.ArrayList;

public class AdminHome extends AppCompatActivity {
    CardView addUser, displayUser, addCandidate, addElection, displayCandidate, displayVote, displayPresidentVote, btnLogout, displayElection;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        sharedPreferences = getSharedPreferences("Login File",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        addUser = findViewById(R.id.addUser);
        displayUser = findViewById(R.id.displayUser);
        addCandidate = findViewById(R.id.addCandidate);
        addElection = findViewById(R.id.addElection);
        displayCandidate = findViewById(R.id.displayCandidate);
        displayVote = findViewById(R.id.displayVote);
        displayPresidentVote = findViewById(R.id.displayPresidentVote);
        btnLogout = findViewById(R.id.btnLogout);
        displayElection = findViewById(R.id.displayElection);



        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHome.this, AddUser.class);
                startActivity(intent);
            }
        });

        displayUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, UserRecyclerView.class);
                startActivity(intent);
            }
        });

        addCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, AddCandidate.class);
                startActivity(intent);
            }
        });

        addElection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, AddElection.class);
                startActivity(intent);
            }
        });

        displayCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, CandidateRecyclerView.class);
                startActivity(intent);
            }
        });

        displayVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, VicepresidentVoteCountRecycler.class);
                startActivity(intent);
            }
        });
        displayPresidentVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, PresidentVoteCountRecycler.class);
                startActivity(intent);
            }
        });


        displayElection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, ElectionRecyclerView.class);
                startActivity(intent);
            }
        });
        
        btnLogout.setOnClickListener(v -> {
            showLogoutDialog();
        });


    }

        @Override
        public void onBackPressed() {
            if (doubleBackToExitPressedOnce) {
                showLogoutDialog();
            } else {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        }

        private void showLogoutDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Yes", (dialog, which) -> logout());
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        }

        private void logout() {
            editor.putString("isLoggedIn", "false");
            editor.apply();
            startActivity(new Intent(AdminHome.this, MainActivity.class));
            finishAffinity();
        }
    }