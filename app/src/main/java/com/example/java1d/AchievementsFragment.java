package com.example.java1d;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AchievementsFragment extends Fragment {
    private List<User> userList;
    private LeaderboardAdapter leaderboardAdapter;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    TextView firstPlaceTv, firstPlaceScore, secondPlaceTv, secondPlaceScore, thirdPlaceTv, thirdPlaceScore;

    ImageView firstPlaceAvatar,secondPlaceAvatar, thirdPlaceAvatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.achievements_page, container, false);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize the task list and adapter
        userList = new ArrayList<>();
        leaderboardAdapter = new LeaderboardAdapter(userList);


        recyclerView = view.findViewById(R.id.rv_leaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerview associate adapter to leaderboard adapter
        recyclerView.setAdapter(leaderboardAdapter);


        fetchUsers();

        firstPlaceTv = view.findViewById(R.id.first_username);
        firstPlaceAvatar = view.findViewById(R.id.first_place_avatar);
        firstPlaceScore = view.findViewById(R.id.first_score);

        secondPlaceTv = view.findViewById(R.id.second_username);
        secondPlaceScore = view.findViewById(R.id.second_score);
        secondPlaceAvatar = view.findViewById(R.id.second_class_avatar);

        thirdPlaceTv = view.findViewById(R.id.third_username);
        thirdPlaceScore = view.findViewById(R.id.third_score);
        thirdPlaceAvatar = view.findViewById(R.id.third_class_avatar);

        return view;

    }


    private void fetchUsers() {
        // Start by clearing any old data
        //userList.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                // Log the entire snapshot for debugging
                Log.d("FirebaseData", "DataSnapshot: " + snapshot.toString());

                // fetch data from snapshot
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    String username = taskSnapshot.child("username").getValue(String.class);
                    String hero_class = taskSnapshot.child("class").getValue(String.class);
                    Integer bossDefeated = taskSnapshot.child("total_boss_defeated").getValue(Integer.class); //will use after boss battle is ready
                    Integer total_damage_dealt = taskSnapshot.child("total_damage_dealt").getValue(Integer.class); //will use after boss battle is ready
                    Integer action_pts = taskSnapshot.child("action_points").getValue(Integer.class); //used for debugging

                    // if value is null, set it to 0
                    if (action_pts == null) {
                        action_pts = 0;
                    }

                    User user = new User(username, hero_class, action_pts, 0); //later change to bossDefeated (After attack is working)
                    //System.out.println(user.getUsername());
                    //System.out.println(user.getHero_class());
                    userList.add(user);


                }
                List<User> sortedList = rankManualUsers(userList);

                for (User u : sortedList) {
                    Log.d("Check Sorting", "Details: " + u.getUsername() + " | " + u.getAction_points());
                } //sorting works

                //Set data into the textviews based on user rank
                if (getContext() != null) {

                    //first place to 3rd place
                    updateLeaderboardItem(sortedList.get(0).getUsername(), sortedList.get(0).getAction_points(), sortedList.get(0).getHero_class().toLowerCase(), firstPlaceTv, firstPlaceScore, firstPlaceAvatar);
                    updateLeaderboardItem(sortedList.get(1).getUsername(), sortedList.get(1).getAction_points(), sortedList.get(1).getHero_class().toLowerCase(), secondPlaceTv, secondPlaceScore, secondPlaceAvatar);
                    updateLeaderboardItem(sortedList.get(2).getUsername(), sortedList.get(2).getAction_points(), sortedList.get(2).getHero_class().toLowerCase(), thirdPlaceTv, thirdPlaceScore, thirdPlaceAvatar);

                    /*
                    //first place
                    String first_username = sortedList.get(0).getUsername();
                    String first_avatar = sortedList.get(0).getHeroClass().toLowerCase(); //do avatar later
                    Integer first_score = sortedList.get(0).getActionPoints();
                    String firstImageResourceName;
                    firstPlaceTv.setText(first_username);
                    firstPlaceScore.setText(String.valueOf(first_score));
                    if (first_avatar.equals("nil")) {
                        firstImageResourceName = "avatar_warrior";
                    } else {
                        firstImageResourceName = "avatar_" + first_avatar;
                    }
                    firstPlaceAvatar.setImageResource(getContext().getResources().getIdentifier(firstImageResourceName, "drawable", getContext().getPackageName()));

                    //second place
                    String second_username = sortedList.get(1).getUsername();
                    String second_avatar = sortedList.get(1).getHeroClass().toLowerCase();
                    Integer second_score = sortedList.get(1).getActionPoints();
                    secondPlaceTv.setText(second_username);
                    secondPlaceScore.setText(String.valueOf(second_score));
                    String secondImageResourceName;
                    if (second_avatar.equals("nil")) {
                        secondImageResourceName = "avatar_warrior";
                    } else {
                        secondImageResourceName = "avatar_" + second_avatar;
                    }
                    secondPlaceAvatar.setImageResource(getContext().getResources().getIdentifier(secondImageResourceName, "drawable", getContext().getPackageName()));
                    Log.d("Check Avatar", "Details: " + second_avatar);


                    //third place
                    String third_username = sortedList.get(2).getUsername();
                    Integer third_score = sortedList.get(2).getActionPoints();
                    String third_avatar = sortedList.get(2).getHeroClass().toLowerCase();
                    thirdPlaceTv.setText(third_username);
                    thirdPlaceScore.setText(String.valueOf(third_score));
                    String thirdImageResourceName;
                    if (third_avatar.equals("nil")) {
                        thirdImageResourceName = "avatar_warrior";
                    } else {
                        thirdImageResourceName = "avatar_" + third_avatar;
                    }
                    thirdPlaceAvatar.setImageResource(getContext().getResources().getIdentifier(thirdImageResourceName, "drawable", getContext().getPackageName()));
                    */

                    //4th place to all the way to be shown as in leaderboard
                    List<User> filteredList = sortedList.subList(3, sortedList.size());
                    leaderboardAdapter.updateData(filteredList);

                    // Notify the adapter that the data has been updated
                    leaderboardAdapter.notifyDataSetChanged();

                    Log.d("Leaderboard", "User List: " + sortedList.size());
                    for (User user : sortedList) {
                        Log.d("Leaderboard", "User: " + user.getUsername() + " | Score: " + user.getActionPoints());
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Log.d("FirebaseData", "Failed to read data: " + error.getMessage());
            }
        });
    }




    private List<User> rankManualUsers(List<User> userList) {

        List<User> newList = manualSort(userList);
        // Assign ranks based on the sorted list
        for (int i = 0; i < newList.size(); i++) { //works
            User user = newList.get(i);
            //rank starts at 1
            user.setRank(i + 1);
        }

        return newList;
    }

    //sorts the list and adds into a new arraylist
    //returns the new arraylist
    private List<User> manualSort(List<User> userList) {
        //
        List<User> sortedUsers = new ArrayList<>();
        for (int i = 1; i < userList.size(); i++) {
            User user = userList.get(i);
            int j;
            for (j = 0; j < sortedUsers.size() && sortedUsers.get(j).getAction_points().compareTo(user.getAction_points())>=0; j++){}
            sortedUsers.add(j, user);
        }
        return sortedUsers;
    }

    // To avoid repetitions, updateLeaderboardItem - sets data in UI for first place, second place and third place
    private void updateLeaderboardItem(String user_username, int user_score, String user_avatar, TextView usernameTextView, TextView scoreTextView, ImageView avatarImageView) {
        usernameTextView.setText(user_username);
        scoreTextView.setText(String.valueOf(user_score));

        String imageResourceName;
        if (user_avatar.equals("nil")) {
            imageResourceName = "avatar_warrior"; //default warrior
        } else {
            imageResourceName = "avatar_" + user_avatar;
        }

        avatarImageView.setImageResource(getContext().getResources().getIdentifier(imageResourceName, "drawable", getContext().getPackageName()));
    }


    //rank users function using collections.sort() //for debugging

    /*
    private void rankUsers(List<User> userList) {
        // Sort the list of users by score in descending order using compare function
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                // Compare by score in descending order
                // Higher score comes first
                return Integer.compare(u2.getAction_points(), u1.getAction_points());
            }
        });

        // Assign ranks based on the sorted list
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            //rank starts at 1
            user.setRank(i + 1);

        }
    }*/
}