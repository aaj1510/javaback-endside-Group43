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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginActivity extends BackgroundActivity {
    EditText usernameInput, passwordInput;

    FirebaseAuth mAuth;
    DatabaseReference usersRef;
    DatabaseReference minorTasksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        playMusic(R.raw.background_music);
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
//                Toast.makeText(LoginActivity.this,"OnClick Works",Toast.LENGTH_SHORT).show();
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
                                                    String uid = userSnapshot.child("uid").getValue(String.class);
                                                    String className = userSnapshot.child("class").getValue(String.class);
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
                                                    usersRef.child(uid).child("last_login_date").setValue(currentDate);

                                                    if(last_login_date == null || last_login_date.isEmpty() || !last_login_date.equals(currentDate)){
                                                        Map<String, Object> minorTaskMap = new HashMap<>();
                                                        Random random = new Random();
                                                        if(className.equals("NIL")){
                                                            for(int i = 1; i < 6; i ++ ){
                                                                int max = 28;                                                                int min = 21;
                                                                int number = random.nextInt(max - min + 1) + min;
                                                                minorTaskMap.put("task_id", "task" + number);
                                                                minorTaskMap.put("completed", false);
                                                                minorTasksRef.child(uid).child("task_" + i).updateChildren(minorTaskMap);
                                                            }
                                                        } else {
                                                            for(int i = 1; i < 3; i ++ ){
                                                                int max = 28;
                                                                int min = 21;
                                                                int number = random.nextInt(max - min + 1) + min;
                                                                minorTaskMap.put("task_id", "task" + number);
                                                                minorTaskMap.put("completed", false);
                                                                minorTasksRef.child(uid).child("task_" + i).updateChildren(minorTaskMap);
                                                            }
                                                            int number = 0;
                                                            for(int j = 3; j < 6; j ++){
                                                                if(className.equals("Warrior")){
                                                                    int min = 1;
                                                                    int max = 5;
                                                                    number = random.nextInt(max - min + 1) + min;
                                                                } else if (className.equals("Mage")){
                                                                    int min = 6;
                                                                    int max = 10;
                                                                    number = random.nextInt(max - min + 1) + min;
                                                                } else if (className.equals("Archer")){
                                                                    int min = 11;
                                                                    int max = 15;
                                                                    number = random.nextInt(max - min + 1) + min;
                                                                } else if (className.equals("Pirate")){
                                                                    int min = 16;
                                                                    int max = 20;
                                                                    number = random.nextInt(max - min + 1) + min;
                                                                }
                                                                minorTaskMap.put("task_id", "task" + number);
                                                                minorTaskMap.put("completed", false);
                                                                minorTasksRef.child(uid).child("task_" + j).updateChildren(minorTaskMap);
                                                            }
                                                        }
                                                    }
                                                    User userInfo = new User(uid,username,retrived_email, className, gold,action_points,total_boss_defeated,total_damage_dealt, last_login_date);



//                                                    Log.d("Firebase", className);
                                                    Toast.makeText(LoginActivity.this,"Data retrieved",Toast.LENGTH_SHORT).show();
                                                    if(userInfo.getHeroClass().equals("NIL")){
//                                                    if (className.equals("NIL")){
                                                        // go to hero selection
                                                        Toast.makeText(LoginActivity.this,"Classname is nil",Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(LoginActivity.this, HeroSelectionActivity.class);
                                                        intent.putExtra("user_key", userInfo);
                                                        startActivity(intent);
                                                    }
                                                    else{
                                                        // go to home page
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        intent.putExtra("user_key", userInfo);
                                                        startActivity(intent);
                                                        Toast.makeText(LoginActivity.this,"Classname is not nil",Toast.LENGTH_SHORT).show();
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}
