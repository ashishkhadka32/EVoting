package com.example.evoting;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class SettingsFragment extends Fragment {
    private CardView btnLogout;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_settings, container, false);
        btnLogout = inflate.findViewById(R.id.btnLogout);

        sharedPreferences = getActivity().getSharedPreferences("Login File", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }


        });

        return inflate;
    }
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutUser();
            }


        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
    private void logoutUser() {
        editor.putString("isLoggedInUser","false");
        editor.commit();
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finishAffinity();
    }
}
