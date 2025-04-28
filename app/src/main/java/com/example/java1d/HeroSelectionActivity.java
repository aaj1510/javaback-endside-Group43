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
        selectWarrior.setOnClickListener(new View.OnClickListener() { //Sets hero class to Warrior
            @Override
            public void onClick(View view) {
                String className = "Warrior";
                if(heroClass.equals("NIL")){ //If user has no hero class yet, assign hero class tasks
                    assignTaskBasedOnHero(className);
                }
               updateUserClass(className);
            }
        });

        selectMage.setOnClickListener(new View.OnClickListener() { //Sets hero class to Mage
            @Override
            public void onClick(View view) {
                String className = "Mage";
                if(heroClass.equals("NIL")){ //If user has no hero class yet, assign hero class tasks
                    assignTaskBasedOnHero(className);
                }
               updateUserClass(className);
            }
        });

        selectArcher.setOnClickListener(new View.OnClickListener() { //Sets hero class to Archer
            @Override
            public void onClick(View view) {
                String className = "Archer";
                if(heroClass.equals("NIL")){ //If user has no hero class yet, assign hero class tasks
                    assignTaskBasedOnHero(className);
                }
               updateUserClass(className);
            }
        });

        selectPirate.setOnClickListener(new View.OnClickListener() { //Sets hero class to Pirate
            @Override
            public void onClick(View view) {
                String className = "Pirate";
                if(heroClass.equals("NIL")){ //If user has no hero class yet, assign hero class tasks
                    assignTaskBasedOnHero(className);
                }
                updateUserClass(className);
            }
        });

    }

    public void updateUserClass(String selectedClass){ //Update user's hero class in firebase and user class
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
        //Using a for loop, randomly generate tasks id with various ranges base on the classes
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

    private void assignTaskBasedOnHero(String className) { //Sets the task range of each class using hash map
        Map<String, int[]> heroTaskRange = new HashMap<>();
        heroTaskRange.put("Warrior", new int[]{1, 5});
        heroTaskRange.put("Mage", new int[]{6, 10});
        heroTaskRange.put("Archer", new int[]{11, 15});
        heroTaskRange.put("Pirate", new int[]{16, 20});

        if (heroTaskRange.containsKey(className)) {
            int[] range = heroTaskRange.get(className);
            generateMinorTasks(1, 3, range[0], range[1]);//class based tasks
            generateMinorTasks(4, 6, 21, 28);//all tasks
        }
    }
}
