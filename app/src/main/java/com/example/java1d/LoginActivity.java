package com.example.java1d;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginActivity extends BackgroundActivity {
    EditText usernameInput, passwordInput;

    FirebaseAuth mAuth;
    DatabaseReference usersRef;
    DatabaseReference minorTasksRef;
    String className;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        Intent serviceIntent = new Intent(LoginActivity.this, BackgroundService.class);
        serviceIntent.putExtra("musicId", R.raw.background_music);
        serviceIntent.setAction("play_music");
        startService(serviceIntent);

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        Button signUpButton = findViewById(R.id.sign_up_button);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        minorTasksRef = FirebaseDatabase.getInstance().getReference("MinorTasks");
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                // get email by searching username in realtime database..
                // sign in with email and password using firebase auth.



                Query checkUserDatabase = usersRef.orderByChild("username").equalTo(username);


                checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot:snapshot.getChildren()){
                                String retrived_email = userSnapshot.child("email").getValue(String.class);
                                if (retrived_email != null) {
                                    mAuth.signInWithEmailAndPassword(retrived_email, password)
                                            .addOnCompleteListener(LoginActivity.this, task -> {
                                                if (task.isSuccessful()) {
                                                    // Sign-in success,check class.
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    String username = userSnapshot.child("username").getValue(String.class);
                                                    userId = userSnapshot.child("uid").getValue(String.class);
                                                    className = userSnapshot.child("class").getValue(String.class);
                                                    Integer gold = userSnapshot.child("gold").getValue(Integer.class);
                                                    Integer action_points = userSnapshot.child("action_points").getValue(Integer.class);
                                                    Integer total_boss_defeated = userSnapshot.child("total_boss_defeated").getValue(Integer.class);
                                                    Integer total_damage_dealt = userSnapshot.child("total_damage_dealt").getValue(Integer.class);
                                                    String last_login_date = userSnapshot.child("last_login_date").getValue(String.class);

                                                    Calendar calendar = Calendar.getInstance();
                                                    int year = calendar.get(Calendar.YEAR);
                                                    int month = calendar.get(Calendar.MONTH);
                                                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                                                    String currentDate = day + "/" + (month + 1) + "/" + year;
                                                    usersRef.child(userId).child("last_login_date").setValue(currentDate);

                                                    if(last_login_date == null || last_login_date.isEmpty() || !last_login_date.equals(currentDate)){
                                                        if(!className.equals("NIL")){
                                                            assignTaskBasedOnHero(className);
                                                        }
                                                    }


                                                    User userInfo = new User(userId,username,retrived_email, className, gold,action_points,total_boss_defeated,total_damage_dealt, last_login_date);
                                                    if(userInfo.getHeroClass().equals("NIL")){
                                                        // go to hero selection
                                                        Intent serviceIntent = new Intent(LoginActivity.this, BackgroundService.class);
                                                        serviceIntent.putExtra("user_key", userInfo);
                                                        startService(serviceIntent);
                                                        Intent intent = new Intent(LoginActivity.this, HeroSelectionActivity.class);
                                                        startActivity(intent);
                                                    }
                                                    else{
                                                        // go to home page
                                                        Intent serviceIntent = new Intent(LoginActivity.this, BackgroundService.class);
                                                        serviceIntent.putExtra("user_key", userInfo);
                                                        startService(serviceIntent);
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                } else {
                                                    // Sign-in failed, show an error message
                                                    Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Email not found for this username.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }

                        else{
                            Toast.makeText(LoginActivity.this, "Username not found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }



    public void generateMinorTasks(Integer startIndex,Integer endIndex,Integer min, Integer max){
        //Using a for loop, randomly generate tasks id with various ranges base on the classes
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
                minorTasksRef.child(userId).child("task_" + i).updateChildren(minorTaskMap);
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
