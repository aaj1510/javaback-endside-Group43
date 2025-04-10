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


public class HeroSelectionActivity extends BackgroundActivity{
    private DatabaseReference databaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_selection_page);
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user_key");
        String userId = user.getUserId();
        String heroClass = user.getHeroClass();
        Log.d("User", userId + "/" + heroClass);
        ImageButton selectWarrior = findViewById(R.id.warrior_poster);
        ImageButton selectMage = findViewById(R.id.mage_poster);
        ImageButton selectArcher = findViewById(R.id.archer_poster);
        ImageButton selectPirate = findViewById(R.id.pirate_poster);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        selectWarrior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseRef.child("Users").child(userId).child("class").setValue("Warrior").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.setHeroClass("Warrior");
                        Log.d("User Class", user.getHeroClass());
                        Intent newIntent = new Intent(HeroSelectionActivity.this,MainActivity.class);
                        newIntent.putExtra("user_key",user);
                        startActivity(newIntent);
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
        });

        selectMage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseRef.child("Users").child(userId).child("class").setValue("Mage").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                user.setHeroClass("Mage");
                                Log.d("User Class", user.getHeroClass());
                                Intent newIntent = new Intent(HeroSelectionActivity.this,MainActivity.class);
                                newIntent.putExtra("user_key",user);
                                startActivity(newIntent);
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
        });

        selectArcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseRef.child("Users").child(userId).child("class").setValue("Archer").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                user.setHeroClass("Archer");
                                Log.d("User Class", user.getHeroClass());
                                Intent newIntent = new Intent(HeroSelectionActivity.this,MainActivity.class);
                                newIntent.putExtra("user_key",user);
                                startActivity(newIntent);
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
        });

        selectPirate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseRef.child("Users").child(userId).child("class").setValue("Pirate").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                user.setHeroClass("Pirate");
                                Log.d("User Class", user.getHeroClass());
                                Intent newIntent = new Intent(HeroSelectionActivity.this,MainActivity.class);
                                newIntent.putExtra("user_key",user);
                                startActivity(newIntent);
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
        });

    }
}
