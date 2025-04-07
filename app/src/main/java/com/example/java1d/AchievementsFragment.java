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
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class AchievementsFragment extends Fragment {
    private List<AchievementsList> ListOfUsers;
    private RecyclerView achievementsRecyclerView;
    private AchievementsAdapter achievementsAdapter;
    private DatabaseReference databaseReference;
    private List<UserSorting> unsortedUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.achievements_page, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        ListOfUsers = new ArrayList<>();

        unsortedUsers = new ArrayList<>(); // List for unsorted users retrieved from database

        achievementsAdapter = new AchievementsAdapter(ListOfUsers);
        achievementsRecyclerView = v.findViewById(R.id.achievementRecycleView);
        achievementsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        achievementsRecyclerView.setAdapter(achievementsAdapter);

        Button sort_by_bosses_defeated_btn = v.findViewById(R.id.rank_by_boss_defeated);
        Button sort_by_total_dmg_btn = v.findViewById(R.id.rank_by_dmg);

        // Button to toggle sorting by boss defeated
        sort_by_bosses_defeated_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListOfUsers.clear();
                getUsers("Boss Defeated: ");
                manualsort(unsortedUsers);
                for (UserSorting user: sort(unsortedUsers)){
                    ListOfUsers.add(new AchievementsList(user.username, user.criteria, user.criteriaValue));
                    System.out.println(user.username + user.criteria + user.criteriaValue);
                }

            }
        });

        // Button to toggle sorting by total damage
        sort_by_total_dmg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListOfUsers.clear();
                getUsers("Total DMG: ");
                sort(unsortedUsers);
                for (UserSorting user: sort(unsortedUsers)) {
                    ListOfUsers.add(new AchievementsList(user.username, user.criteria, user.criteriaValue));
                }
            }
        });

        return v;
    }

    // Get the required data from database and assign to new UserSorting
    public void getUsers(String criteria){
        List<UserUser> userArray = new ArrayList<>();
        userArray.add(new UserUser("user1", 31, 11));
        userArray.add(new UserUser("user2", 1, 22));
        userArray.add(new UserUser("user3", 15, 16));
        userArray.add(new UserUser("user4", 22, 1));
        userArray.add(new UserUser("user5", 11, 31));

        for(UserUser user: userArray){
            if (Objects.equals(criteria, "Boss Defeated: ")){
                UserSorting user_new = new UserSorting(user.username, criteria, user.total_boss_defeated);
                unsortedUsers.add(user_new);
            }
            else{
                UserSorting user_new = new UserSorting(user.username, criteria, user.total_damage_dealt);
            }
        }
    }

    /* Start of Sorting Algorithm */
    public static List<UserSorting> sort(List<UserSorting> unsortedUsers){
        List<UserSorting> sortedUsers = new ArrayList<>();
        for (int i=0; i<unsortedUsers.size(); i++){
            insert(unsortedUsers.get(i), sortedUsers);
        }
        return sortedUsers;
    }
    private static void insert(UserSorting user, List<UserSorting> sortedUsers){
        int i;
        for(i=0; i< sortedUsers.size()&&sortedUsers.get(i).criteriaValue.compareTo(user.criteriaValue)<0; i++){
        }
        sortedUsers.add(i, user);
    }
    /* End of sorting Algorithm */

    public void manualsort(List<UserSorting> unsortedUsers){
        List<UserSorting> sortedUsers = new ArrayList<>();
        for(int i=0; i<unsortedUsers.size(); i++){
            UserSorting user = unsortedUsers.get(i);
            for(int j=0; j< sortedUsers.size(); j++){
                if(sortedUsers.get(i).criteriaValue.compareTo(user.criteriaValue)>=0){
                    sortedUsers.add(i, user);
                }
            }
        }
    }

    protected static class UserSorting{
        private String username;
        private String criteria;
        private Integer criteriaValue;

        protected UserSorting(String username, String criteria, Integer criteriaValue){
            this.username = username;
            this.criteria = criteria;
            this.criteriaValue = criteriaValue;
        }
    }

    // This class was for testing -> can delete
    protected static class UserUser{
        private String username;
        private Integer total_boss_defeated;
        private Integer total_damage_dealt;

        protected UserUser(String username, Integer boss_defeated, Integer total_dmg){
            this.username = username;
            this.total_boss_defeated = boss_defeated;
            this.total_damage_dealt = total_dmg;
        }
    }

}