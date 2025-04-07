package com.example.java1d;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>{

    private List<User> userlist;
    private Context context;

    public LeaderboardAdapter(List<User> userlists) {
        this.userlist = userlists;
        this.context = context; //check if need this
    }

    public void updateData(List<User> newUserList) {
        this.userlist = newUserList;
        notifyDataSetChanged();  // Notify the adapter that the data has changed
    }

    @NonNull
    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_leaderboard_item, parent, false);
        return new LeaderboardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.ViewHolder holder, int position) {

        User item = userlist.get(position);
        holder.usernameTv.setText(item.getUsername());
        holder.bossDefeatTv.setText("Total Points : "  + String.valueOf(item.getActionPoints()));
        holder.rankTv.setText("Rank - " + String.valueOf(item.getRank()));
        System.out.println(item.getHeroClass());

        String imageResourceName = "avatar_" + item.getHeroClass().toLowerCase();
        holder.userclassIv.setImageResource(holder.itemView.getContext().getResources().getIdentifier(imageResourceName, "drawable", holder.itemView.getContext().getPackageName()));

    }

    @Override
    public int getItemCount() {
        //if(userlist.size() > 10) {
         //   return 10;
        //}
        //else{
          //  return userlist.size();
        //}
        return userlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView usernameTv, bossDefeatTv, rankTv;
        public ImageView userclassIv;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            usernameTv = itemView.findViewById(R.id.user_name);
            bossDefeatTv = itemView.findViewById(R.id.user_bosses);
            rankTv = itemView.findViewById(R.id.user_rank);
            userclassIv = itemView.findViewById(R.id.user_class);


        }


    }
}
