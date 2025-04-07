package com.example.java1d;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    TextView firstPlaceTv,firstPlaceAvatar,firstPlaceScore, secondPlaceTv, secondPlaceScore, thirdPlaceTv, thirdPlaceScore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.achievements_page, container, false);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize the task list and adapter
        userList = new ArrayList<>();
        leaderboardAdapter = new LeaderboardAdapter(userList);


        recyclerView = view.findViewById(R.id.rv_leaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(leaderboardAdapter);


        fetchUsers();


        firstPlaceTv = view.findViewById(R.id.first_username);
        //firstPlaceAvatar = view.findViewById(R.id.first_user_win);
        firstPlaceScore = view.findViewById(R.id.first_score);
        //firstPlaceTv = view.findViewById(R.id.first_username);
        secondPlaceTv = view.findViewById(R.id.second_username);
        secondPlaceScore = view.findViewById(R.id.second_score);

        thirdPlaceTv = view.findViewById(R.id.third_username);
        thirdPlaceScore = view.findViewById(R.id.third_score);
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
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    String username = taskSnapshot.child("username").getValue(String.class);
                    String hero_class = taskSnapshot.child("class").getValue(String.class);
                    Integer bossDefeated = taskSnapshot.child("total_boss_defeated").getValue(Integer.class);
                    Integer total_damage_dealt = taskSnapshot.child("total_damage_dealt").getValue(Integer.class);
                    Integer action_pts = taskSnapshot.child("action_points").getValue(Integer.class);

                    // if value is null, set it to 0
                    if (action_pts == null) {
                        action_pts = 0;
                    }


                    User user = new User(username,hero_class,action_pts,0); //later change to bossDefeated (After attack is working)
                    //System.out.println(user.getUsername());
                    //System.out.println(user.getHero_class());
                    userList.add(user);



                }
                rankUsers(userList);
                //Set data into the textviews based on user rank

                //first place
                String first_username = userList.get(0).getUsername();
                //String first_avatar = userList.get(0).getHero_class(); //do avatar later
                Integer first_score = userList.get(0).getActionPoints();
                firstPlaceTv.setText(first_username);
                firstPlaceScore.setText(String.valueOf(first_score) + " POINTS");

                //System.out.println(userList.get(1).getUsername()); //for debugging
                //second place
                String second_username = userList.get(1).getUsername();
                Integer second_score = userList.get(1).getActionPoints();
                secondPlaceTv.setText(second_username);
                secondPlaceScore.setText(String.valueOf(second_score) + " POINTS");

                //third place
                String third_username = userList.get(2).getUsername();
                Integer third_score = userList.get(2).getActionPoints();
                thirdPlaceTv.setText(third_username);
                thirdPlaceScore.setText(String.valueOf(third_score) + " POINTS");

                //4th place to all the way to be shown as in leaderboard
                List<User> filteredList =  userList.subList(3, userList.size());
                leaderboardAdapter.updateData(filteredList);

                // Notify the adapter that the data has been updated
                leaderboardAdapter.notifyDataSetChanged();

                Log.d("Leaderboard", "User List: " + userList.size());
                for (User user : userList) {
                    Log.d("Leaderboard", "User: " + user.getUsername() + " | Score: " + user.getActionPoints());
                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Log.d("FirebaseData", "Failed to read data: " + error.getMessage());
            }
        });
    }

    //TODO: USE ALGO TO RANK THE USERS
    //rank users function using collections.sort()
    private void rankUsers(List<User> userList) {
        // Sort the list of users by score in descending order using compare function
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                // Compare by score in descending order
                // Higher score comes first
                return Integer.compare(u2.getActionPoints(), u1.getActionPoints());
            }
        });

        // Assign ranks based on the sorted list
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            //rank starts at 1
            user.setRank(i + 1);

        }
    }

}

