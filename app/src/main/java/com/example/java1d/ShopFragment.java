package com.example.java1d;

import android.media.SoundPool;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShopFragment extends DialogFragment {

    DatabaseReference userDatabaseReference;
    User user;

    TextView goldAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        BossActivity bossActivity = (BossActivity) getActivity();
        user = bossActivity.getUserInfo();
        ImageButton superStamina = view.findViewById(R.id.super_stamina);
        ImageButton arcaneAura = view.findViewById(R.id.arcane_aura);
        ImageButton doubleDamage = view.findViewById(R.id.double_damage);
        ImageButton fieryFury = view.findViewById(R.id.fiery_fury);
        ImageView backButton = view.findViewById(R.id.close_shop_button);
        goldAmount = view.findViewById(R.id.gold_amount);
        goldAmount.setText(String.valueOf(user.getGold()));


        //OnClickListener of various buttons
        superStamina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchasePowerUp(5, "Super Stamina");
            }
        });

        arcaneAura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchasePowerUp(20,"Arcane Aura");
            }
        });

        doubleDamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchasePowerUp(50,"Double Damage");
            }
        });

        fieryFury.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchasePowerUp(100, "Fiery Fury");
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;

    }

    @Override
    public void onStart() { //Dialog fragment settings
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            getDialog().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }
    }

    public void purchasePowerUp(Integer goldCost, String powerUp){
        if(user.getGold() < goldCost){ //Checks if user has enough gold
            Toast.makeText(this.getContext(), "You do not have enough gold", Toast.LENGTH_SHORT).show();
        } else if (!user.getPowerUp().equals("None")){ //Checks if user already have a power up
            Toast.makeText(this.getContext(), "You already have a power up active", Toast.LENGTH_SHORT).show();
        } else { //If user does not have a power up and has enough gold
            int newGoldAmount = user.getGold() - goldCost; //Calculate gold value after purchase
            userDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
            userDatabaseReference.child(user.getUserId()).child("gold").setValue(newGoldAmount).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override //Update user's gold value in firebase
                public void onSuccess(Void unused) { //On success, play sound effect
                    SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
                    soundPool.load(getContext(), R.raw.purchase_sound, 1);
                    soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                        @Override
                        public void onLoadComplete(SoundPool soundPool, int soundIdCallback, int status) {
                            if (status == 0){
                                soundPool.play(soundIdCallback, 1,1,0,0,1);
                            }
                        }
                    });
                    //Update the user class with the new values and set the text view of the new gold amount
                    user.setPowerUp(powerUp);
                    user.setGold(newGoldAmount);
                    goldAmount.setText(String.valueOf(user.getGold()));
                }
            });
        }
    }
}