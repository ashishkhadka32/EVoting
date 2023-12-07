package com.example.evoting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evoting.R;
import com.example.evoting.VicepresidentVoteCountRecycler;
import com.example.evoting.model.VicePresident;

import java.util.List;

public class VoteCountRecyclerViewAdapter extends RecyclerView.Adapter<VoteCountRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<VicePresident> voteCounts;

    public VoteCountRecyclerViewAdapter(Context context, List<VicePresident> voteCounts) {
        this.context = context;
        this.voteCounts = voteCounts;
    }

    @NonNull
    @Override
    public VoteCountRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vote_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoteCountRecyclerViewAdapter.ViewHolder holder, int position) {
        VicePresident voteCount = voteCounts.get(position);
        holder.candidateName.setText(voteCount.getName());
        holder.voteCount.setText(String.valueOf(voteCount.getVoteCount()));
    }

    @Override
    public int getItemCount() {
        return voteCounts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView candidateName;
        TextView voteCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            candidateName = itemView.findViewById(R.id.candidateName);
            voteCount = itemView.findViewById(R.id.voteCount);

        }
    }
}
