package com.example.evoting.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.example.evoting.model.VicePresident;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VicePresidentRecyclerViewAdapter extends RecyclerView.Adapter<VicePresidentRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<VicePresident> mData;
    RequestOptions option;
    private String uid;


    public VicePresidentRecyclerViewAdapter(Context mContext, List<VicePresident> mData, String uid)
    {
        this.mContext = mContext;
        this.mData = mData;
        this.uid = uid;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public VicePresidentRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.display_vice_president_row_item,parent,false);
        return new VicePresidentRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VicePresidentRecyclerViewAdapter.MyViewHolder holder, int position) {
        VicePresident vicePresident = mData.get(position);
        holder.candidateUserName.setText("Name:"+vicePresident.getName());
        holder.candidatePartyName.setText("Party:"+vicePresident.getParty());
        holder.candidateNominees.setText("Nominees:"+vicePresident.getNominees());
        holder.btnVicePresidentVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = vicePresident.getName();
                String party = vicePresident.getParty();
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

    public void setData(List<VicePresident> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView candidateUserName, candidatePartyName, candidateNominees;
        ImageView userImage;
        AppCompatButton btnVicePresidentVote;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            candidateUserName = itemView.findViewById(R.id.candidateUserName);
            candidatePartyName = itemView.findViewById(R.id.candidatePartyName);
            candidateNominees = itemView.findViewById(R.id.candidateNominees);
            userImage = itemView.findViewById(R.id.userImage);
            btnVicePresidentVote = itemView.findViewById(R.id.btnVicePresidentVote);

        }
    }
    private void insertVote(String name, String party, String uid) {
        StringRequest request = new StringRequest(Request.Method.POST, UrlPath.VicePresidentVote_Url,
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
