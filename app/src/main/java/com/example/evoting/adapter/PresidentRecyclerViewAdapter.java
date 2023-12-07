package com.example.evoting.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.evoting.R;
import com.example.evoting.Url.UrlPath;
import com.example.evoting.model.President;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresidentRecyclerViewAdapter extends RecyclerView.Adapter<PresidentRecyclerViewAdapter.MyViewHolder>{
    private Context mContext;
    private List<President> mData;
    RequestOptions option;

    private String uid;
    public PresidentRecyclerViewAdapter(Context mContext, List<President> mData, String uid)
    {
        this.mContext = mContext;
        this.mData = mData;
        this.uid = uid;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public PresidentRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.display_president_row_item,parent,false);
        return new PresidentRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PresidentRecyclerViewAdapter.MyViewHolder holder, int position) {

        President president = mData.get(position);
        holder.candidateName.setText("Name:"+president.getName());
        holder.partyName.setText("Party:"+president.getParty());
        holder.nominees.setText("Nominees:"+president.getNominees());
        holder.btnVote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = president.getName();
                String party = president.getParty();
                insertVote(name, party, uid);
            }
        });

        Glide.with(mContext)
                .asBitmap()
                .load(UrlPath.Main_Url+mData.get(position).getImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(new CustomTarget<Bitmap>() {
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

    public void setData(List<President> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView candidateName, partyName, nominees;
        ImageView userImage;
        AppCompatButton btnVote;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            candidateName = itemView.findViewById(R.id.candidateName);
            partyName = itemView.findViewById(R.id.partyName);
            nominees = itemView.findViewById(R.id.nominees);
            userImage = itemView.findViewById(R.id.userImage);
            btnVote = itemView.findViewById(R.id.btnVote);
        }
    }
    private void insertVote(String name, String party, String uid) {
            StringRequest request = new StringRequest(Request.Method.POST, UrlPath.AddVote_Url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(mContext, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("party", party);
                    params.put("uid", uid);
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(mContext);
            queue.add(request);

    }
}
