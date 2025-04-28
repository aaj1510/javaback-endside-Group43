package com.example.java1d;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment implements View.OnClickListener{
    FirebaseAuth mAuth;
    TextView pts_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_page, container, false);

        User user = getUserInfo();

        // Changes Based on User Info
        TextView username_text = view.findViewById(R.id.username);
        username_text.setText(getUserInfo().getUsername().toLowerCase());

        pts_text = view.findViewById(R.id.actionPts);
        pts_text.setText(String.valueOf(getUserInfo().getActionPoints()));

        ImageView avatar_image = view.findViewById(R.id.avatar);
        String avatar = user.getHeroClass().toLowerCase();
        String imageResourceName = "avatar_" + avatar;
        avatar_image.setImageResource(getContext().getResources().getIdentifier(imageResourceName, "drawable", getContext().getPackageName()));



        // Buttons
        ImageButton tasksBtn = view.findViewById(R.id.tasksBtn);
        ImageButton bossBtn = view.findViewById(R.id.boss_button);
        ImageButton inventoryBtn = view.findViewById(R.id.inventory_button);
        ImageButton achievementsBtn = view.findViewById(R.id.achievement_button);
        ImageButton settingsBtn = view.findViewById(R.id.settings);

        // Listeners
        tasksBtn.setOnClickListener(this);
        bossBtn.setOnClickListener(this);
        inventoryBtn.setOnClickListener(this);
        achievementsBtn.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) { //onClick of buttons, switches to respective fragments base on id of buttons
        if (view.getId() == R.id.tasksBtn) {
            TasksFragment dialogFragment = new TasksFragment();
            dialogFragment.show(getActivity().getSupportFragmentManager(), null);
        }
        else if (view.getId() == R.id.boss_button) {
            replaceFragment(new BossPreviewFragment());
        }
        else if (view.getId() == R.id.inventory_button) {
            replaceFragment(new InventoryFragment());
        }
        else if (view.getId() == R.id.achievement_button) {
            replaceFragment(new AchievementsFragment());
        }
        if (view.getId() == R.id.settings) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            SettingsFragment settingsDialogFragment = new SettingsFragment();
            settingsDialogFragment.show(getActivity().getSupportFragmentManager(), null);
        }
    }

    private void replaceFragment(Fragment fragment){ //Replaces current fragment with respective fragments
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void updateActionPointsText(){ //Updates action points text view
        pts_text.setText(String.valueOf(getUserInfo().getActionPoints()));
    }

    public User getUserInfo(){
        return BackgroundService.getUserInfo();
    }

}