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

        //inflate the layout of a single item in the RecyclerView
        //convert the xml layout into a View object
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_leaderboard_item, parent, false);
        return new LeaderboardAdapter.ViewHolder(view);
    }

    // using onBindViewHolder to
    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.ViewHolder holder, int position) {
        //get current user from userlist based on position/index
        User item = userlist.get(position);

        //set textviews in UI based user data
        holder.usernameTv.setText(item.getUsername());
        holder.bossDefeatTv.setText("Total Damage Dealt : "  + String.valueOf(item.getTotalDamageDealt()));
        holder.rankTv.setText("Rank - " + String.valueOf(item.getRank()));
        //System.out.println(item.getHero_class());

        String imageResourceName;
        if (item.getHeroClass() == null || item.getHeroClass().toLowerCase().equals("nil")){
            imageResourceName = "avatar_warrior"; //set to warrior default
        }
        else{
            imageResourceName = "avatar_" + item.getHeroClass().toLowerCase();
        }
        //sets the image for the ImageView by using the getResources().getIdentifier() method to get the correct drawable resource based on the imageResourceName
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


    //method for holding references to the views - textviews, imageviews
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
