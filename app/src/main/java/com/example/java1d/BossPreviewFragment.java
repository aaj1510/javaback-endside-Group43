package com.example.java1d;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

public class BossPreviewFragment extends Fragment {

    Boss boss;
    DatabaseReference bossOfTheWeekDatabaseReference;
    DatabaseReference bossesDatabaseReference;
    Boolean dataLoaded = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.boss_preview_page, container, false);
        ImageButton battleButton = view.findViewById(R.id.battleButton);
        TextView remainingTime = view.findViewById(R.id.remaining_time);
        ImageView bossImage = view.findViewById(R.id.boss_image);
        TextView bossDescriptionText = view.findViewById(R.id.boss_description);

        //Gets current date and time
        LocalDate today = LocalDate.now();
        Integer dateOfToday = today.getDayOfMonth();
        LocalTime currentTime = LocalTime.now();
        Integer currentHour = currentTime.getHour();

        bossOfTheWeekDatabaseReference = FirebaseDatabase.getInstance().getReference("BossOfTheWeek");
        //Gets data from firebase of boss of the week
        bossOfTheWeekDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String bossId = snapshot.child("boss_id").getValue(String.class);
                String bossEndDate = snapshot.child("end_date").getValue(String.class);
                String bossEndTime = snapshot.child("end_time").getValue(String.class);
                String[] endDate = bossEndDate.split("/");
                String[] endTime = bossEndTime.split(":");
                int remainingDays = Integer.parseInt(endDate[0]) - dateOfToday;
                int remainingHours = Integer.parseInt(endTime[0]) - currentHour;
                //Compares and calculate end date of boss battle with current date then displays it
                String formattedTime = String.format(Locale.US,"%d DAYS %02d HOURS", remainingDays, remainingHours);
                Log.d("Remaining Day", String.valueOf(remainingDays));
                remainingTime.setText(formattedTime);
                String imageResourceName;
                imageResourceName = bossId;
                //Set to respective boss image based on database value
                bossImage.setImageResource(getContext().getResources().getIdentifier(imageResourceName, "drawable", getContext().getPackageName()));
                bossesDatabaseReference = FirebaseDatabase.getInstance().getReference("Bosses").child(bossId);
                //From the boss id of boss of the week data, get the values from the bosses table in firebase
                bossesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String bossDescription = snapshot.child("boss_description").getValue(String.class);
                        bossDescriptionText.setText(bossDescription);

                        String bossName = snapshot.child("boss_name").getValue(String.class);
                        Integer bossGold = snapshot.child("boss_gold").getValue(Integer.class);
                        Integer bossHp = snapshot.child("boss_hp").getValue(Integer.class);
                        String bossReward = snapshot.child("boss_reward").getValue(String.class);
                        boss = new Boss(bossId, bossName, bossGold, bossHp, bossReward, formattedTime); //Instantiate boss from the returned values from the bosses table in firebase
                        dataLoaded = true; //Boolean variable to check if data has finished loading
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        battleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataLoaded){ //If data is loaded, go to boss activity (For cases where user tries to enter before data has fully loaded)
                    Intent intent = new Intent(getActivity(), BossActivity.class);
                    intent.putExtra("boss_info",boss);
                    startActivity(intent);
                } else { //Else if data is not loaded, display toast to user
                    Toast.makeText(getContext(), "Retrieving data, please wait", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}