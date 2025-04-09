package com.example.java1d;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class BossActivity extends BackgroundActivity {

    DatabaseReference bossBattleDatabaseReference;
    DatabaseReference userDatabaseReference;
    ProgressBar bossHealthBar;
    TextView actionPointsText;
    TextView bossHealthText;
    Integer bossHp;

    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.boss_page);

        Intent serviceIntent = new Intent(BossActivity.this, BackgroundService.class);
        serviceIntent.putExtra("musicId", R.raw.boss_battle_music);
        serviceIntent.setAction("play_music");
        startService(serviceIntent);

        Intent bossIntent = getIntent();
        Boss boss = bossIntent.getParcelableExtra("boss_info");
        //Boss Views
        ImageView bossImage = findViewById(R.id.boss_image);
        TextView bossNameText = findViewById(R.id.boss_name);
        bossHealthText = findViewById(R.id.boss_health_text);
        TextView bossTimeLeft = findViewById(R.id.time_left_text);
        bossHealthBar = findViewById(R.id.boss_health_bar);
        TextView bossDefeatedText = findViewById(R.id.boss_defeated_text);

        // Buttons
        ImageButton atkBtn = findViewById(R.id.basic_attack);
        TextView atkText = findViewById(R.id.attack_text);
        ImageButton skillBtn = findViewById(R.id.skill);
        TextView skillText = findViewById(R.id.skill_text);
        ImageButton shopBtn = findViewById(R.id.inventory);
        ImageButton retreatBtn = findViewById(R.id.retreat);

        // Changes Based on User Info
        // Action Points
        User user = getUserInfo();
        actionPointsText = findViewById(R.id.boss_battle_action_pts);
        actionPointsText.setText(String.valueOf(user.getActionPoints()));

        // Avatar
        ImageView avatar_image = findViewById(R.id.avatar);
        String avatar = user.getHeroClass().toLowerCase();
        String avatarImageResourceName = "avatar_" + avatar;
        avatar_image.setImageResource(getResources().getIdentifier(avatarImageResourceName, "drawable", getPackageName()));

        //Boss
        String bossImageResourceName = boss.getBossId();
        bossImage.setImageResource(getResources().getIdentifier(bossImageResourceName, "drawable", getPackageName()));
        String bossName = boss.getBossName();
        bossNameText.setText(bossName.toUpperCase());
        bossHp = boss.getBossHp();
        bossTimeLeft.setText(boss.getBossRemainingTime());
        String userId = getUserInfo().getUserId();
        bossBattleDatabaseReference = FirebaseDatabase.getInstance().getReference("BossBattle");

        bossBattleDatabaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer currentHp = boss.getBossCurrentHealth();
                if(snapshot.exists()){
                    currentHp = snapshot.child("boss_health").getValue(Integer.class);
                    boss.setBossCurrentHealth(currentHp);
                    Log.d("Firebase Data", currentHp.toString());
                    Integer bossLevel = snapshot.child("boss_level").getValue(Integer.class);
                    Log.d("Firebase Data", bossLevel.toString());
                    boss.setBossLevel(bossLevel);
                } else {
                    Map<String, Object> bossBattleValues = boss.toMap();
                    bossBattleDatabaseReference.child(userId).setValue(bossBattleValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Firebase Data", "Data successfully written");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firebase Data", e.getLocalizedMessage());
                        }
                    });
                }
                bossHealthBar.setMax(bossHp);
                bossHealthBar.setProgress(currentHp);
                String formattedHeath = String.format(Locale.US,"%d/%d", currentHp, bossHp);
                bossHealthText.setText(formattedHeath);
                if(boss.getBossCurrentHealth().equals(0)){
                    atkBtn.setEnabled(false);
                    atkBtn.setColorFilter(Color.argb(100, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);
                    atkText.setTextColor(Color.BLACK);
                    skillBtn.setEnabled(false);
                    skillBtn.setColorFilter(Color.argb(100, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);
                    skillText.setTextColor(Color.BLACK);
                    bossDefeatedText.setVisibility(View.VISIBLE);
                    bossImage.setColorFilter(Color.argb(230, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Listeners
        atkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int min = 3;
                int max = 10;
                Integer randomDamage = random.nextInt(max - min + 1) + min;
                damageBoss(5,randomDamage);

            }
        });
        skillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int min = 50;
                int max = 99;
                Integer randomDamage = random.nextInt(max - min + 1) + min;
                damageBoss(20,randomDamage);
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

    public void damageBoss(Integer points, Integer damage){
        User user = getUserInfo();
        String userId = user.getUserId();
        Intent bossIntent = getIntent();
        Boss boss = bossIntent.getParcelableExtra("boss_info");
        Integer actionPoints = user.getActionPoints();
        Integer currentBossHp = boss.getBossCurrentHealth();
        if(actionPoints >= points && currentBossHp > 0){
            userDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
            Integer totalDamage = user.getTotalDamageDealt();
            int updatedTotalDamage = totalDamage + damage;
            user.setTotalDamageDealt(updatedTotalDamage);
            int updatedActionPoints = actionPoints - points;
            user.setActionPoints(updatedActionPoints);
            int updatedBossHealth;
            int calculatedBossHealth = currentBossHp - damage;
            if(calculatedBossHealth < 0){
                updatedBossHealth = 0;
                Integer totalBossDefeated = user.getTotalBossDefeated();
                int updatedTotalBossDefeated = totalBossDefeated + 1;
                user.setTotalBossDefeated(updatedTotalBossDefeated);
                userDatabaseReference.child(userId).child("total_boss_defeated").setValue(updatedTotalBossDefeated).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firebase User", "Updated total number of boss defeated");
                    }
                });
                //To Do: Add dialog fragment for user to collect reward
            } else {
                updatedBossHealth = calculatedBossHealth;
            }
            boss.setBossCurrentHealth(updatedBossHealth);
            Map<String,Object> attackMap = user.attackBossMap();
            userDatabaseReference.child(userId).updateChildren(attackMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    actionPointsText.setText(String.valueOf(updatedActionPoints));
                }
            });
            bossBattleDatabaseReference.child(userId).child("boss_health").setValue(updatedBossHealth).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    bossHealthBar.setProgress(boss.getBossCurrentHealth());
                    String formattedHeath = String.format(Locale.US,"%d/%d", boss.getBossCurrentHealth(), bossHp);
                    bossHealthText.setText(formattedHeath);
                }
            });
        } else {
            Toast.makeText(BossActivity.this,"You do not have enough action points", Toast.LENGTH_SHORT).show();
        }

    }

    public User getUserInfo(){
        return BackgroundService.getUserInfo();
    }
}
