package com.example.java1d;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//Handles the Hero Selection Page
public class HeroSelectionActivity extends BackgroundActivity{
    private DatabaseReference databaseRef;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_selection_page);
        user = getUserInfo();
        String heroClass = user.getHeroClass();
        ImageButton selectWarrior = findViewById(R.id.warrior_poster);
        ImageButton selectMage = findViewById(R.id.mage_poster);
        ImageButton selectArcher = findViewById(R.id.archer_poster);
        ImageButton selectPirate = findViewById(R.id.pirate_poster);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        selectWarrior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(heroClass.equals("NIL")){
                    generateMinorTasks(1,3, 1,5);
                    generateMinorTasks(4,6,21,28);
                }
               updateUserClass("Warrior");
            }
        });

        selectMage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(heroClass.equals("NIL")){
                    generateMinorTasks(1,3,6,10);
                    generateMinorTasks(4,6,21,28);
                }
               updateUserClass("Mage");
            }
        });

        selectArcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(heroClass.equals("NIL")){
                    generateMinorTasks(1,3,11,15);
                    generateMinorTasks(4,6,21,28);
                }
               updateUserClass("Archer");
            }
        });

        selectPirate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(heroClass.equals("NIL")){
                    generateMinorTasks(1,3,16,20);
                    generateMinorTasks(4,6,21,28);
                }
                updateUserClass("Pirate");
            }
        });

    }

    public void updateUserClass(String selectedClass){
        databaseRef.child("Users").child(user.getUserId()).child("class").setValue(selectedClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.setHeroClass(selectedClass);
                        Log.d("User Class", user.getHeroClass());
                        Intent intent = new Intent(HeroSelectionActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){
                        Log.e("Firebase", e.getLocalizedMessage());
                        Toast.makeText(HeroSelectionActivity.this,"An error occurred",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public User getUserInfo(){
        return BackgroundService.getUserInfo();
    }

    public void generateMinorTasks(Integer startIndex,Integer endIndex,Integer min, Integer max){
        DatabaseReference minorTasksRef;
        minorTasksRef = FirebaseDatabase.getInstance().getReference("MinorTasks");
        Map<String, Object> minorTaskMap = new HashMap<>();
        ArrayList<Integer> selectedNumbers = new ArrayList<>();
        Random random = new Random();
        int number;
        for(int i = startIndex; i <= endIndex;){
            number = random.nextInt(max - min + 1) + min;
            if(!selectedNumbers.contains(number)){
                selectedNumbers.add(number);
                Log.d("Creating Minor Tasks", selectedNumbers.toString());
                minorTaskMap.put("task_id", "task" + number);
                minorTaskMap.put("completed", false);
                minorTasksRef.child(user.getUserId()).child("task_" + i).updateChildren(minorTaskMap);
                i++;
            }
        }
    }
}
