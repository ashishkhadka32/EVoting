package com.example.evoting;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class HomeFragment extends Fragment {
    Spinner edtElectionRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_home, container, false);
        edtElectionRes = inflate.findViewById(R.id.edtElectionRes);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.election_result, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtElectionRes.setAdapter(adapter);

        edtElectionRes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Select Election Result")) {
                    // Delay the toast message by 3 seconds
                    new android.os.Handler().postDelayed(
                            () -> Toast.makeText(getActivity(), "Please select a valid election", Toast.LENGTH_SHORT).show(),
                            2000
                    );
                }else if (selectedItem.equals("President 2080 Result")) {
                    Intent intent = new Intent(requireActivity(), PresidentVoteCountRecycler.class);
                    startActivity(intent);
                } else if (selectedItem.equals("Vice-President 2080 Result")) {
                    Intent intent = new Intent(requireActivity(), VicepresidentVoteCountRecycler.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return inflate;
    }
}