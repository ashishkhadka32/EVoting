package com.example.evoting.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.evoting.EditCandidate;
import com.example.evoting.R;
import com.example.evoting.Url.UrlPath;
import com.example.evoting.model.Candidate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandidateRecyclerViewAdapter extends RecyclerView.Adapter<CandidateRecyclerViewAdapter.MyViewHolder>{


    private Context mContext;
    private List<Candidate> mData;
    RequestOptions option;
    public CandidateRecyclerViewAdapter(Context mContext, List<Candidate> mData) {
        this.mContext = mContext;
        this.mData = mData;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public CandidateRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.display_candidate_row_item,parent,false);
        return new CandidateRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateRecyclerViewAdapter.MyViewHolder holder, int position) {
        Candidate candidate = mData.get(position);
        holder.username.setText("Name:"+candidate.getName());
        holder.address.setText("Address:"+candidate.getAddress());
        holder.contact.setText("Contact:"+candidate.getContact());
        holder.party.setText("Party:"+candidate.getParty());
        holder.nominees.setText("Nominees:"+candidate.getNominees());


//        Glide.with(mContext).load(mData.get(position).getImg()).apply(option).into(holder.userImage);
        Glide.with(mContext)
                .asBitmap()
                .load(UrlPath.Main_Url +mData.get(position).getImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(new CustomTarget<Bitmap>(){
                    private Bitmap bitmap;

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Process the fetched bitmap here
                        processBitmap(resource);
                    }

                    private void processBitmap(Bitmap resource) {
                        this.bitmap = resource;
                        holder.userImage.setImageBitmap(bitmap);


                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Optional: Handle when the resource is cleared
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView  username, address, contact, party, nominees;
        ImageView userImage, imgMenu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            address = itemView.findViewById(R.id.address);
            contact = itemView.findViewById(R.id.contact);
            party = itemView.findViewById(R.id.party);
            nominees = itemView.findViewById(R.id.nominees);
            userImage = itemView.findViewById(R.id.userImage);
            imgMenu = itemView.findViewById(R.id.imgMenu);
            imgMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.edit:
                    editUser(getAdapterPosition());
                    return true;
                case R.id.delete:
                    confirmationDialog();
                    return true;
                default:
                    return false;
            }
        }



        private void confirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete Candidate");
            builder.setMessage("Are you sure you want to delete this user?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteCandidate(getAdapterPosition());
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

    }
    private void deleteCandidate(int adapterPosition) {
        int id = mData.get(adapterPosition).getId();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlPath.DeleteCandidate_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DeleteResponse", response);
                if (response.equals("1")) {
                    mData.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    Toast.makeText(mContext, "Candidate deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Failed to delete candidate", Toast.LENGTH_SHORT).show();
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
    private void editUser(int adapterPosition) {
        Candidate candidate1 = mData.get(adapterPosition);
        Intent intent = new Intent(mContext, EditCandidate.class);
        intent.putExtra("id", candidate1.getId());
        intent.putExtra("name", candidate1.getName());
        intent.putExtra("address", candidate1.getAddress());
        intent.putExtra("contact", candidate1.getContact());
        intent.putExtra("party", candidate1.getParty());
        intent.putExtra("nominees", candidate1.getNominees());
        intent.putExtra("upload",candidate1.getImage());
        mContext.startActivity(intent);
    }
}

