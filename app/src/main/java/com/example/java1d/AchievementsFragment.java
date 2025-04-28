package com.example.java1d;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AchievementsFragment extends Fragment {
    private List<User> userList;
    private LeaderboardAdapter leaderboardAdapter;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;

    private String criteria = "damageDealt"; // default sorting criterion

    TextView firstPlaceTv, firstPlaceScore, secondPlaceTv, secondPlaceScore, thirdPlaceTv, thirdPlaceScore;

    Button damageDealtBtn, totalBossDefeatBtn;

    ImageView firstPlaceAvatar, secondPlaceAvatar, thirdPlaceAvatar;

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

        //retrieve users based on criteria
        fetchUsers(criteria);

        firstPlaceTv = view.findViewById(R.id.first_username);
        firstPlaceAvatar = view.findViewById(R.id.first_place_avatar);
        firstPlaceScore = view.findViewById(R.id.first_score);

        secondPlaceTv = view.findViewById(R.id.second_username);
        secondPlaceScore = view.findViewById(R.id.second_score);
        secondPlaceAvatar = view.findViewById(R.id.second_class_avatar);

        thirdPlaceTv = view.findViewById(R.id.third_username);
        thirdPlaceScore = view.findViewById(R.id.third_score);
        thirdPlaceAvatar = view.findViewById(R.id.third_class_avatar);

        damageDealtBtn = view.findViewById(R.id.damageDealtBtn);
        totalBossDefeatBtn = view.findViewById(R.id.bossDefeatedBtn);


        damageDealtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!criteria.equals("damageDealt")){//only do it if current criteria is different //if criteria not equal damage dealt
                    criteria = "damageDealt"; //set criteria to damage dealt and change the background color of the button to indicate which filter is applied.
                    totalBossDefeatBtn.setBackgroundColor(Color.GRAY); //button not active
                    damageDealtBtn.setBackgroundColor(Color.WHITE); //button active
                    fetchUsers(criteria); //fetch user data based on criteria
                }

            }
        });

        totalBossDefeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criteria = "totalBossDefeat";
                totalBossDefeatBtn.setBackgroundColor(Color.WHITE);
                damageDealtBtn.setBackgroundColor(Color.GRAY);
                fetchUsers(criteria);

            }
        });

        return view;

    }


    private void fetchUsers(String criteria) {
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                Log.d("FirebaseData", "DataSnapshot: " + snapshot.toString());

                // fetch data from snapshot
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    String username = taskSnapshot.child("username").getValue(String.class);
                    String hero_class = taskSnapshot.child("class").getValue(String.class);
                    Integer bossDefeated = taskSnapshot.child("total_boss_defeated").getValue(Integer.class);
                    Integer total_damage_dealt = taskSnapshot.child("total_damage_dealt").getValue(Integer.class);

                    // if value is null, set it to 0
                    if (total_damage_dealt == null) {
                        total_damage_dealt = 0;
                    }

                    if (bossDefeated == null){
                        bossDefeated = 0;
                    }

                    //if criteria equals to damage dealt, pass the criteria value as total_damage_dealt
                    if(criteria.equals("damageDealt")){
                        User user = new User(username, hero_class,criteria, total_damage_dealt, 0);
                        userList.add(user);
                    }
                    else{ //else if criteria equals to boss defeat, pass the criteria value as bossDefeated
                        User user = new User(username, hero_class,criteria, bossDefeated, 0);
                        userList.add(user);
                    }

                }

                System.out.println(criteria);
                //call the function to rank
                rankUsers(userList,criteria);

                //Set data into the textviews based on user rank
                if (getContext() != null) {

                    //first place to 3rd place

                    //update the first 3 ranks based on criteria and rank using a method called updateLeaderboardItem to avoid repetitions of code
                    for(int positon = 0; positon < 3 && positon < userList.size(); positon++){
                        System.out.println(userList.get(positon));
                        System.out.println(userList.get(positon).getUsername());
                        updateLeaderboardItem(userList.get(positon),criteria);

                    }

                    //4th place to all the way to be shown as in leaderboard
                    List<User> filteredList = userList.subList(3, userList.size());
                    leaderboardAdapter.updateData(filteredList,criteria);

                    // Notify the adapter that the data has been updated
                    leaderboardAdapter.notifyDataSetChanged();

                    Log.d("Leaderboard", "User List: " + userList.size());
                    for (User user : userList) {
                        Log.d("Leaderboard", "User: " + user.getUsername() + " | Score: " + user.getTotalDamageDealt());
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


    //rankusers using merge sort
    private void rankUsers(List<User> userList,String criteria) {

        mergeSort(userList, 0, userList.size() - 1,criteria);
        // Assign ranks based on the sorted list
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            //rank starts at 1
            user.setRank(i + 1);
        }
        
    }

    //merge function to combine two sorted subarrays into one sorted array in the merge sort algorithm
    public static List<User> merge(List<User> userList, int low, int mid, int high,String criteria) {

        Boolean comparisonResult;

        List<User> sortedUsers = new ArrayList<>();
        int startL = low;
        int startR = mid + 1;

        while (startL <= mid && startR <= high) {
            //condition based on criteria
            if(criteria.equals("damageDealt")){
                //If criteria is "damageDealt", it compares the getTotalDamageDealt() values of the left and right users.
                comparisonResult = userList.get(startL).getTotalDamageDealt() >= userList.get(startR).getTotalDamageDealt();
            }
            else{
                //else compares based on getTotalBossDefeated()
                comparisonResult = userList.get(startL).getTotalBossDefeated() >= userList.get(startR).getTotalBossDefeated();

            }
            //adds user to the sortedUsers list based on result
            if (comparisonResult) {
                sortedUsers.add(userList.get(startL));
                startL += 1;
            } else {
                sortedUsers.add(userList.get(startR));
                startR += 1;
            }
        }

        // add remaining elements from left sublist
        while (startL <= mid) {
            sortedUsers.add(userList.get(startL));
            startL += 1;
        }

        while (startR <= high) {
            sortedUsers.add(userList.get(startR));
            startR += 1;
        }

        // Update the original list with sorted values
        for (int i = low; i <= high; i++) {
            userList.set((i), sortedUsers.get(i - low));
        }
        return userList;
    }

    public static void mergeSort(List<User> unsortedUsers, int low, int high, String criteria){
        if (low < high){ //base case - continues sorting until the condition is false
            int mid = (low + high)/2;
            mergeSort(unsortedUsers, low, mid ,criteria); //sort first half
            mergeSort(unsortedUsers, mid+1, high,criteria); // sort second half
            merge(unsortedUsers, low, mid, high,criteria); // combine 2 sorted arrays
        }

    }


    // To avoid repetitions, updateLeaderboardItem - sets data in UI for first place, second place and third place

    private void updateLeaderboardItem(User user, String criteria) {
        System.out.println(user);
        System.out.println(user.getUsername());
        Integer criteriaVal;
        if (criteria.equals("damageDealt")){
            criteriaVal = user.getTotalDamageDealt();

        }
        else{//criteria is totalBossDefeat
            criteriaVal = user.getTotalBossDefeated();
        }

        //set textviews for each rank //update avatar using the updateAvatar function
        if (user.getRank() == 1){ //rank 1
            firstPlaceTv.setText(user.getUsername());
            firstPlaceScore.setText(String.valueOf(criteriaVal));
            updateAvatar(user, firstPlaceAvatar);
        }
        else if (user.getRank() == 2){ //rank 2
            secondPlaceTv.setText(user.getUsername());
            secondPlaceScore.setText(String.valueOf(criteriaVal));
            updateAvatar(user,secondPlaceAvatar);
        }

        else if (user.getRank()==3){ //rank 3
            thirdPlaceTv.setText(user.getUsername());
            thirdPlaceScore.setText(String.valueOf(criteriaVal));
            updateAvatar(user,thirdPlaceAvatar);
        }

    }


    //need to have funtion for updateavatar in ui - to mitigate the case if getHeroClass is null - which happens when user signs up but haven't login to choose avatar
    private void updateAvatar(User user, ImageView avatarImageView) {
        String imageResourceName;
        String user_avatar = user.getHeroClass().toLowerCase();
        if (user_avatar.equals("nil")) {
            imageResourceName = "avatar_warrior"; //default warrior
        } else {
            imageResourceName = "avatar_" + user_avatar;
        }

        avatarImageView.setImageResource(getContext().getResources().getIdentifier(imageResourceName, "drawable", getContext().getPackageName()));


    }

}