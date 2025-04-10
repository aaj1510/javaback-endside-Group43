package com.example.java1d;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
    ImageView bossImage;
    TextView damageText;

    Boss boss;

    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.boss_page);

        Intent serviceIntent = new Intent(BossActivity.this, BackgroundService.class);
        serviceIntent.putExtra("musicId", R.raw.boss_battle_music);
        serviceIntent.setAction("play_music");
        startService(serviceIntent);

        boss = getBossInfo();

        //Boss Views
        bossImage = findViewById(R.id.boss_image);
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

        damageText = findViewById(R.id.damage_text);

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
                    bossImage.setColorFilter(Color.argb(220, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);

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
        Integer actionPoints = user.getActionPoints();
        Integer currentBossHp = boss.getBossCurrentHealth();
        if(actionPoints >= points && currentBossHp > 0){
            userDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
            playDamageSound(damage);
            showDamaged(damage);
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
                        //To Do: Add dialog fragment for user to collect reward
                        RewardFragment dialogFragment = new RewardFragment();
                        dialogFragment.show(BossActivity.this.getSupportFragmentManager(),null);
                    }
                });
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

    public void playDamageSound(Integer damage){
        int soundId;
        SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        if(damage < 50){
            soundPool.load(BossActivity.this, R.raw.attack_damage_sound, 1);
        } else {
            soundPool.load(BossActivity.this, R.raw.skill_damage_sound,1);
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int soundIdCallback, int status) {
                if (status == 0){
                    soundPool.play(soundIdCallback, 1,1,0,0,1);
                }
            }
        });
    }

    public void showDamaged(Integer damage){
        String formattedDamage = String.format(Locale.US,"- %d HP",damage);
        damageText.setText(formattedDamage);
        damageText.setVisibility(View.VISIBLE);
        damageText.setAlpha(1f);
        damageText.animate().alpha(0).setDuration(1000).setStartDelay(2000).withEndAction(new Runnable() {
            @Override
            public void run() {
                damageText.setVisibility(View.GONE);
            }
        }).start();

        bossImage.setColorFilter(Color.parseColor("#80FF0000"), PorterDuff.Mode.MULTIPLY);
        new Handler(Looper.getMainLooper()).postDelayed(() ->{
            if(!boss.getBossCurrentHealth().equals(0)){
                bossImage.clearColorFilter();
            }
        }, 1000);

    }


    public User getUserInfo(){
        return BackgroundService.getUserInfo();
    }

    public Boss getBossInfo(){
        Intent bossIntent = getIntent();
        return bossIntent.getParcelableExtra("boss_info");
    }
}
