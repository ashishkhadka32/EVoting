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

public class ElectionFragment extends Fragment {
    private String uid;
    Spinner edtElection;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_election, container, false);
         edtElection = inflate.findViewById(R.id.edtElection);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.election_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtElection.setAdapter(adapter);
        uid = requireActivity().getIntent().getStringExtra("uid");

        edtElection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Select Election Result")) {
                    // Delay the toast message by 3 seconds
                    new android.os.Handler().postDelayed(
                            () -> Toast.makeText(getActivity(), "Please select a valid election result", Toast.LENGTH_SHORT).show(),
                            2000
                    );
                }else if (selectedItem.equals("President 2080")) {
                    Intent intent = new Intent(requireActivity(), PresidentRecyclerView.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                } else if (selectedItem.equals("Vice-President 2080")) {
                    Intent intent = new Intent(requireActivity(), VicePresidentRecyclerView.class);
                    intent.putExtra("uid", uid);
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