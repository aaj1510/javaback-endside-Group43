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

public class LoginActivity extends BackgroundActivity {
    EditText usernameInput, passwordInput;

    FirebaseAuth mAuth;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        playMusic(R.raw.background_music);
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        Button signUpButton = findViewById(R.id.sign_up_button);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("Users");

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



                Query checkUserDatabase = db.orderByChild("username").equalTo(username);


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


                                                    User userInfo = new User(uid,username,retrived_email, className, gold,action_points,total_boss_defeated,total_damage_dealt);


//                                                    Log.d("Firebase", className);
                                                    Toast.makeText(LoginActivity.this,"Data retrieved",Toast.LENGTH_SHORT).show();
                                                    if(userInfo.getHero_class().equals("NIL")){
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
