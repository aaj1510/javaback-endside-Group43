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

public class HomeFragment extends Fragment implements View.OnClickListener{
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
        pts_text.setText(String.valueOf(getUserInfo().getAction_points()));

        ImageView avatar_image = view.findViewById(R.id.avatar);
        String avatar = user.getHero_class().toLowerCase();
        String imageResourceName = "avatar_" + avatar;
        avatar_image.setImageResource(getContext().getResources().getIdentifier(imageResourceName, "drawable", getContext().getPackageName()));



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
//            TaskFragment dialogFragment = new TaskFragment();
//            dialogFragment.show(getActivity().getSupportFragmentManager(), "TaskFragment");
            TasksFragment dialogFragment = new TasksFragment();
            dialogFragment.show(getActivity().getSupportFragmentManager(), "PresetTasksFragment");
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

//        task_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //TaskFragment dialogFragment = new TaskFragment();
//                PresetTasksFragment dialogFragment = new PresetTasksFragment();
//                // dialogFragment.show(getActivity().getSupportFragmentManager(), "TaskFragment");
//                dialogFragment.show(getActivity().getSupportFragmentManager(), "PresetTasksFragment");
//            }
//        });

    public void updatePoints(){
        pts_text.setText(String.valueOf(getUserInfo().getAction_points()));
    }

    public User getUserInfo(){
        MainActivity activity = (MainActivity) getActivity();
        return activity.getUserInfo();
    }

}