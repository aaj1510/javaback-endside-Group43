package com.example.java1d;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class RewardFragment extends DialogFragment {

    DatabaseReference userDatabaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reward, container, false);

        BossActivity bossActivity = (BossActivity) getActivity();
        Boss boss = bossActivity.getBossInfo();
        User user = bossActivity.getUserInfo();
        TextView coinText = view.findViewById(R.id.coin_text);
        String formattedCoinText = String.format(Locale.US,"+ %d",boss.getBossGold());
        coinText.setText(formattedCoinText);
        Button collectButton = view.findViewById(R.id.collect_button);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
                userDatabaseReference.child(user.getUserId()).child("gold").setValue(boss.getBossGold()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.setGold(boss.getBossGold());
                        Log.d("Firebase Data", "Successfully updated user's gold");
                        dismiss();
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        if(getDialog() != null && getDialog().getWindow() != null){
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
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
        }
    }
}