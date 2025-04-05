package com.example.java1d;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class BossActivity extends BackgroundActivity {

    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.boss_page);
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user_key");

        // Buttons
        ImageButton atkBtn = findViewById(R.id.basic_attack);
        ImageButton skillBtn = findViewById(R.id.skill);
        ImageButton shopBtn = findViewById(R.id.inventory);
        ImageButton retreatBtn = findViewById(R.id.retreat);

        // Changes Based on User Info
        // Action Points
        TextView pts_text = findViewById(R.id.actionPts);
        pts_text.setText(String.valueOf(user.getAction_points()));

        // Avatar
        ImageView avatar_image = findViewById(R.id.avatar);
        String avatar = user.getHero_class().toLowerCase();
        String imageResourceName = "avatar_" + avatar;
        avatar_image.setImageResource(getResources().getIdentifier(imageResourceName, "drawable", getPackageName()));

        // Listeners
        atkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        skillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        retreatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BossActivity.this, MainActivity.class);
                intent.putExtra("user_key",user);
                startActivity(intent);
            }
        });

    }
}
