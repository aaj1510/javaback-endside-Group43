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
import android.widget.RelativeLayout;
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

    RelativeLayout resetButtonLayout;
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
        ImageButton resetBtn = findViewById(R.id.reset_button);

        damageText = findViewById(R.id.damage_text);

        // Changes Based on User Info
        // Action Points
        User user = getUserInfo();
        actionPointsText = findViewById(R.id.boss_battle_action_pts);
        actionPointsText.setText(String.valueOf(user.getActionPoints()));

        resetButtonLayout = findViewById(R.id.reset_button_layout);

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
                Integer currentHp = boss.getBossCurrentHealth(); //Get current health of the boss
                if(snapshot.exists()){ //If data exists (Player has fought or entered the boss battle before)
                    currentHp = snapshot.child("boss_health").getValue(Integer.class);  //Get boss health from Firebase
                    boss.setBossCurrentHealth(currentHp); //Sets boss health in boss class
                    Integer bossLevel = snapshot.child("boss_level").getValue(Integer.class); //Get boss level from Firebase
                    boss.setBossLevel(bossLevel); //Sets boss level in boss class
                } else {
                    Map<String, Object> bossBattleValues = boss.toMap(); //Map String keys with health and level fields for Firebase input
                    bossBattleDatabaseReference.child(userId).setValue(bossBattleValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override //If data does not exists, Create a new instance of health and level fields and set values in Firebase
                        public void onSuccess(Void unused) {
                            Log.d("Firebase Data", "Data successfully written");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firebase Data", e.getLocalizedMessage()); //On failure, log error message
                        }
                    });
                }
                bossHealthBar.setMax(bossHp); //Set Progress bar maximum value to boss max health
                bossHealthBar.setProgress(currentHp); //Set Progress bar value to bosses current health
                String formattedHeath = String.format(Locale.US,"%d/%d", currentHp, bossHp);
                bossHealthText.setText(formattedHeath); //Set text view of boss health to show value currentHealth/MaxHealth
                if(boss.getBossCurrentHealth().equals(0)){ //If bosses current health is 0, change corresponding views to show defeated boss state
                    atkBtn.setEnabled(false); //Disable attack button
                    atkBtn.setColorFilter(Color.argb(100, 0, 0, 0), PorterDuff.Mode.SRC_ATOP); //Sets a grey colour filter of attack button
                    atkText.setTextColor(Color.BLACK); //Set attack text view to black
                    skillBtn.setEnabled(false); //Disable skill button
                    skillBtn.setColorFilter(Color.argb(100, 0, 0, 0), PorterDuff.Mode.SRC_ATOP); //Sets a grey colour filter of skill button
                    skillText.setTextColor(Color.BLACK); //Set skill text view to black
                    bossDefeatedText.setVisibility(View.VISIBLE); //Show defeated boss text view
                    bossImage.setColorFilter(Color.argb(220, 0, 0, 0), PorterDuff.Mode.SRC_ATOP); //Sets a grey colour filter of boss image
                    resetButtonLayout.setVisibility(View.VISIBLE); //Show reset boss button

                } else { //If boss current health is not 0, change corresponding views to show alive boss state
                    atkBtn.setEnabled(true); //Enable attack button
                    atkBtn.clearColorFilter(); //Clear colour filter of attack button
                    atkText.setTextColor(Color.parseColor("#5DEEFF")); //Sets attack text view back to the default colour
                    skillBtn.setEnabled(true); //Enable skill button
                    skillBtn.clearColorFilter(); //Clear colour filter of skill button
                    skillText.setTextColor(Color.parseColor("#AE4FE7")); //Sets skill text view back to the default colour
                    bossDefeatedText.setVisibility(View.GONE); //Hide defeated boss text view
                    bossImage.clearColorFilter(); //Clear colour filter of boss image
                    resetButtonLayout.setVisibility(View.GONE); //Hide reset boss button
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Listeners
        atkBtn.setOnClickListener(new View.OnClickListener() { //When attack button is clicked
            @Override
            public void onClick(View v) {
                int points = 5; //Default action points required value
                int damage = randomDamage(3,10); //Default attack value from randomDamage method

                if(user.getPowerUp().equals("Super Stamina")){ //If user has super stamina power up
                    points = 0; //Set action points required to 0
                    user.setPowerUp("None"); // Remove power up
                }
                if(user.getPowerUp().equals("Fiery Fury")){ //If user has fiery fury power up
                    damage = boss.getBossHp() / 2; //Deal 50% of boss total health
                    user.setPowerUp("None"); //Remove power up
                }
                damageBoss(points,damage); //Calls damageBoss method with point and damage

            }
        });
        skillBtn.setOnClickListener(new View.OnClickListener() { //When skill button is clicked
            @Override
            public void onClick(View v) {
                int damage = randomDamage(50,99); //Default damage value from randomDamage method
                int points = 20; //Default action points required value
                if(user.getPowerUp().equals("Double Damage")){ //If user has double damage power up
                    damage = damage * 2; //Multiply the default damage by 2
                    user.setPowerUp("None"); //Remove power up
                }
                if (user.getPowerUp().equals("Arcane Aura")){ //If user has arcane aura power up
                    points = 5; //Set action points required value to 5
                    user.setPowerUp("None"); //Remove power up
                }
                damageBoss(points,damage); //Calls damageBoss method with points and damage
            }
        });
        shopBtn.setOnClickListener(new View.OnClickListener() { //When shop button is clicked
            @Override
            public void onClick(View v) {
                ShopFragment dialogFragment = new ShopFragment(); //Create a new fragment for shop and display
                dialogFragment.show(getSupportFragmentManager(), null);
            }
        });
        retreatBtn.setOnClickListener(new View.OnClickListener() { //When retreat button is clicked
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BossActivity.this, MainActivity.class); //Return back to MainActivity
                intent.putExtra("user_key",user); //Update user class through intent
                startActivity(intent);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() { //When reset button is clicked
            @Override
            public void onClick(View view) {
                boss.setBossCurrentHealth(boss.getBossHp()); //Sets current boss health to boss maximum health
                bossBattleDatabaseReference.child(userId).child("boss_health").setValue(boss.getBossHp()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override //Updates current boss health value in firebase
                    public void onSuccess(Void unused) {
                        Log.d("Boss Battle", "Boss reset successful");
                    }
                });
            }
        });

    }

    public Integer randomDamage(int min, int max){
        Random random = new Random(); //Creates a new random instance
        Integer randomDamage = random.nextInt(max - min + 1) + min; //Set range of random integer values from min and max parameters and get a random value
        return randomDamage; //Return random value
    }

    public void damageBoss(Integer points, Integer damage){
        User user = getUserInfo();
        String userId = user.getUserId();
        Integer actionPoints = user.getActionPoints();
        Integer currentBossHp = boss.getBossCurrentHealth();
        if(actionPoints >= points && currentBossHp > 0){ //If there is enough action points to spend and current boss health is more than 0
            userDatabaseReference = FirebaseDatabase.getInstance().getReference("Users"); //Set user path for firebase
            playDamageSound(damage); //Play sound by calling playDamageSound method
            showDamaged(damage); //Show visual effects and text for damage done by calling showDamage method
            Integer totalDamage = user.getTotalDamageDealt(); //Get totalDamage value from user class;
            int updatedTotalDamage = totalDamage + damage; //Calculate new total damage value
            user.setTotalDamageDealt(updatedTotalDamage); //Set new total damage value in user class
            int updatedActionPoints = actionPoints - points; //Calculate new action points value
            user.setActionPoints(updatedActionPoints); //Set new total action points values in user class
            int updatedBossHealth;
            int calculatedBossHealth = currentBossHp - damage; //Calculate boss health
            if(calculatedBossHealth < 0){ //If boss current health is less than 0 (Excess damage)
                updatedBossHealth = 0; //Set boss current health to 0
                Integer totalBossDefeated = user.getTotalBossDefeated(); //Get totalBossDefeated value from user class
                int updatedTotalBossDefeated = totalBossDefeated + 1; //Iterate totalBossDefeated value by 1
                user.setTotalBossDefeated(updatedTotalBossDefeated); //Update totalBossDefeated value in user class
                userDatabaseReference.child(userId).child("total_boss_defeated").setValue(updatedTotalBossDefeated).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override //Update totalBossDefeated value in firebase
                    public void onSuccess(Void unused) {
                        Log.d("Firebase User", "Updated total number of boss defeated");
                        RewardFragment dialogFragment = new RewardFragment(); //Create a new instance of reward fragment
                        dialogFragment.show(BossActivity.this.getSupportFragmentManager(),null); //Display reward fragment
                    }
                });
            } else { //Else if boss current health is not 0
                updatedBossHealth = calculatedBossHealth; //set boss current health to the updated value
            }
            boss.setBossCurrentHealth(updatedBossHealth); //Update boss current health in boss class
            Map<String,Object> attackMap = user.attackBossMap(); //Map String keys to action points and total damage dealt fields to firebase
            userDatabaseReference.child(userId).updateChildren(attackMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override //Update user in firebase
                public void onSuccess(Void unused) {
                    actionPointsText.setText(String.valueOf(updatedActionPoints)); //Update text view of action points value
                }
            });
            bossBattleDatabaseReference.child(userId).child("boss_health").setValue(updatedBossHealth).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    bossHealthBar.setProgress(boss.getBossCurrentHealth()); //Update progress bar value to new current boss health from getting value from boss class
                    String formattedHeath = String.format(Locale.US,"%d/%d", boss.getBossCurrentHealth(), bossHp);
                    bossHealthText.setText(formattedHeath); //Update text view of boss health
                }
            });
        } else { //Else if action points is not enough
            Toast.makeText(BossActivity.this,"You do not have enough action points", Toast.LENGTH_SHORT).show(); //Display toast to inform user
        }

    }

    public void playDamageSound(Integer damage){ //Method for playing sound when boss is damaged
        SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build(); //Creates new instance of SoundPool
        if(damage < 50){ //If damage value is less than 50
            soundPool.load(BossActivity.this, R.raw.attack_damage_sound, 1); //Set attack sound track
        } else { //Else if damage is more than 50
            soundPool.load(BossActivity.this, R.raw.skill_damage_sound,1); //Set skill sound track
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int soundIdCallback, int status) {
                if (status == 0){ //If status is not playing
                    soundPool.play(soundIdCallback, 1,1,0,0,1); //Play corresponding sound track
                }
            }
        });
    }

    public void showDamaged(Integer damage){ //Method for showing visual effects and text when boss is damaged
        String formattedDamage = String.format(Locale.US,"- %d HP",damage);
        damageText.setText(formattedDamage); //set text view of damage output to formattedDamage
        damageText.setVisibility(View.VISIBLE);//show hidden text view
        damageText.setAlpha(1f);
        damageText.animate().alpha(0).setDuration(1000).setStartDelay(2000).withEndAction(new Runnable() {
            @Override //Animate text view to display for 1 second before fading for 2 seconds
            public void run() {
                damageText.setVisibility(View.GONE);
            }
        }).start();

        bossImage.setColorFilter(Color.parseColor("#80FF0000"), PorterDuff.Mode.MULTIPLY);
        new Handler(Looper.getMainLooper()).postDelayed(() ->{ //Set Red colour filter to boss image to show visual effect of taking damage
            if(!boss.getBossCurrentHealth().equals(0)){ //If boss health is not 0
                bossImage.clearColorFilter(); //Clear colour filter after 1.5 seconds
            }
        }, 1500);

    }


    public User getUserInfo(){ //Returns user class passed from BackgroundService method
        return BackgroundService.getUserInfo();
    }

    public Boss getBossInfo(){ //Returns boss class passed from BossPreviewFragment
        Intent bossIntent = getIntent();
        return bossIntent.getParcelableExtra("boss_info");
    }
}
