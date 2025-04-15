package com.example.java1d;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsFragment extends DialogFragment {

    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.settings_page, container, false);

        String userId = getUserInfo().getUserId();
        Log.d("UserID", userId);

        //delete Account
        Button deleteAccBtn = view.findViewById(R.id.deleteAccBtn);

        // Close Dialog
        ImageButton closeBtn = view.findViewById(R.id.exit);
        closeBtn.setOnClickListener(v -> dismiss());

        // Log out
        Button logoutBtn = view.findViewById(R.id.logoutBtn);


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(),"Signed out", Toast.LENGTH_SHORT).show();
            }
        });


        deleteAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //1. Remove linked table with userid data first
                removeUserDataFromMJTasks(userId);
                removeUserDataFromMNTasks(userId);
                removeUserDataFromBossBattle(userId);
                removeUserDetails(userId);

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(),"Account Successfully Deleted", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }



    private void removeUserDataFromMJTasks(String userid) {
        DatabaseReference majorTasksRef = FirebaseDatabase.getInstance().getReference("MajorTasks");

        majorTasksRef.orderByChild("user_id").equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot majorTasksSnapshot : snapshot.getChildren()){
                    majorTasksSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("SETTINGS_FRAGMENT", "Major Tasks Error" + error);
            }
        });


    }


    private void removeUserDataFromMNTasks(String userid) {
        DatabaseReference minorTasksRef = FirebaseDatabase.getInstance().getReference("MinorTasks");

        minorTasksRef.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null){
                    snapshot.getRef().removeValue();
                    Log.d("SETTINGS_FRAGMENT", "Minor Tasks Removed");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("SETTINGS_FRAGMENT", "Minor Tasks Error" + error);


            }
        });


    }


    private void removeUserDataFromBossBattle(String userId) {


        DatabaseReference bossbattleRef = FirebaseDatabase.getInstance().getReference("BossBattle");

        bossbattleRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null){
                    snapshot.getRef().removeValue();
                    Log.d("SETTINGS_FRAGMENT", "Boss User Data  Removed");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("SETTINGS_FRAGMENT", "Boss Battle Error" + error);


            }
        });


    }

    private void removeUserDetails(String userId) {

        // Remove user profile data from the 'users' table
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.child(userId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Successfully deleted user profile data
                        Log.d("SETTINGS_FRAGMENT", "Sucessfully removed user data");

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user != null){
                            user.delete().addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()){
                                    Log.d("SETTINGS_FRAGMENT", "User account deleted successfully.");

                                    mAuth.signOut();
                                    Log.d("SETTINGS_FRAGMENT", "User signed out.");
                                }
                                else{
                                    Log.e("SETTINGS_FRAGMENT", "Failed to delete user account.");
                                }
                            });
                        }



                    } else {
                        // Handle failure to delete user profile data
                    }
                });

    }






    public User getUserInfo(){
        return BackgroundService.getUserInfo();
    }
}