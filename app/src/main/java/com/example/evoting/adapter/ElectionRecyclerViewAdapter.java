package com.example.evoting.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.example.evoting.ElectionRecyclerView;
import com.example.evoting.R;
import com.example.evoting.Url.UrlPath;
import com.example.evoting.model.Election;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElectionRecyclerViewAdapter extends RecyclerView.Adapter<ElectionRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Election> mData;

    public ElectionRecyclerViewAdapter(Context mContext, List<Election> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ElectionRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.display_election_row_item, parent, false);
        return new ElectionRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ElectionRecyclerViewAdapter.MyViewHolder holder, int position) {
        Election election = mData.get(position);
        holder.election.setText("Election:" + election.getElection());
        holder.start.setText("StartDate:" + election.getStartDate());
        holder.end.setText("EndDate:" + election.getEndDate());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView election, start, end;
        ImageView imgMenu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            election = itemView.findViewById(R.id.election);
            start = itemView.findViewById(R.id.startDate);
            end = itemView.findViewById(R.id.endDate);
            imgMenu = itemView.findViewById(R.id.imgMenu);
            imgMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.menus, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:
                    confirmationDialog();
                    return true;
                default:
                    return false;
            }
        }

        private void confirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete Election");
            builder.setMessage("Are you sure you want to delete this election?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteElection(getAdapterPosition());
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

        private void deleteElection(int adapterPosition) {
            int id = mData.get(adapterPosition).getId();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlPath.DeleteElection_Url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("DeleteResponse", response);
                    if (response.equals("1")) {
                        removeElectionDataFromSharedPreferences(id); // Remove data from SharedPreferences
                        mData.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                        Toast.makeText(mContext, "Election deleted successfully", Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("election_dates_added", false);
                        editor.putString("election_types_", "");
                        editor.apply();
                        
                    } else {
                        Toast.makeText(mContext, "Failed to delete election", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(id));  // Pass the id parameter to the server
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(mContext);
            queue.add(stringRequest);
        }

        // Remove election data from SharedPreferences
        private void removeElectionDataFromSharedPreferences(int id) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = preferences.edit();

            // Replace with your actual keys
            String electionTypeKey = "election_type_" + id;
            String firstLetterKey = "election_type_added_first" + id;

            editor.remove(electionTypeKey);
            editor.remove(firstLetterKey);
            editor.apply();
        }
    }
}

