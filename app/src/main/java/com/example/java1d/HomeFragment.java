package com.example.java1d;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment implements View.OnClickListener {

//    BackgroundActivity backgroundActivity;
//    private DatabaseReference databaseRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Intent intent = backgroundActivity.getIntent(); THIS CRASHES APPLICATION
        //User user = intent.getParcelableExtra("user_key");
        //String userId = user.getUid();
        //String username = user.getUsername();
        //Log.d("User", userId + "/" + username);

        // Retrieve Class Data
        //databaseRef = FirebaseDatabase.getInstance().getReference();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_page, container, false);

        // Modifiable Views
        TextView username_text = view.findViewById(R.id.username);
        //username_text.setText(username);
        ImageView avatar_image = view.findViewById(R.id.avatar);

        // Buttons
        ImageButton task_button = view.findViewById(R.id.task_button);
        ImageButton boss_button = view.findViewById(R.id.boss_button);
        ImageButton inventory_button = view.findViewById(R.id.inventory_button);
        ImageButton achievements_button = view.findViewById(R.id.achievement_button);

        // Listeners
        task_button.setOnClickListener(this);
        boss_button.setOnClickListener(this);
        inventory_button.setOnClickListener(this);
        achievements_button.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.task_button) {
            TaskFragment dialogFragment = new TaskFragment();
            dialogFragment.show(getActivity().getSupportFragmentManager(), "TaskFragment");
        }
        else if (view.getId() == R.id.boss_button) {
            replaceFragment(new ViewBossFragment());
        }
        else if (view.getId() == R.id.inventory_button) {
            replaceFragment(new InventoryFragment());
        }
        else if (view.getId() == R.id.achievement_button) {
            replaceFragment(new AchievementsFragment());
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}