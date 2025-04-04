package com.example.java1d;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.AchievementViewHolder>{
    private List<AchievementsList> ListOfUsers;
    private Context context;
    public AchievementsAdapter(List<AchievementsList> ListOfUsers){
        this.ListOfUsers = ListOfUsers;
    }


    public static class AchievementViewHolder extends RecyclerView.ViewHolder{
        public TextView textRankTabUser;
        public TextView textRankTabBy;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            textRankTabUser = itemView.findViewById(R.id.rankTabUser);
            textRankTabBy = itemView.findViewById(R.id.rankTabBy);
        }
    }

    // Creating new views -> invoked by layout manager
    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View u = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_achievement_user, parent, false);
        return new AchievementViewHolder(u);
    }

    // Replacing contents of view -> invoked by layout manager
    @Override
    public void onBindViewHolder(@NonNull AchievementsAdapter.AchievementViewHolder holder, int position) {
        // Get element from dataset and replace content of view with element
        AchievementsList achievementTab = ListOfUsers.get(position);
        holder.textRankTabUser.setText(achievementTab.getUsername());
        holder.textRankTabBy.setText(achievementTab.getRankBy());
// to do
    }

    // Return size of dataset -> invoked by layout manager
    @Override
    public int getItemCount() {
        return ListOfUsers.size();
    }

}
