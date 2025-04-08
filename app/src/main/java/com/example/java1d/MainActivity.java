package com.example.java1d;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.java1d.databinding.ActivityMainBinding;

public class MainActivity extends BackgroundActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        replaceFragment(new HomeFragment());

        Intent serviceIntent = new Intent(MainActivity.this, BackgroundService.class);
        serviceIntent.putExtra("musicId", R.raw.background_music);
        serviceIntent.setAction("play_music");
        startService(serviceIntent);
        getUserInfo();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            binding.navigationBar.setPadding(0,0,0,0);
            return insets;
        });
        ViewCompat.setOnApplyWindowInsetsListener(binding.navigationBar, (v, insets) -> {
            v.setPadding(0,0,0,0);
            return insets;
        });

        // TODO: Enable Navigation Bar on Home, Inventory, Tasks and Achievements (Leaderboard)
        // For Navigation Bar

        binding.navigationBar.setItemIconTintList(null);
        binding.navigationBar.setBackground(null);
        binding.navigationBar.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.inventory){
                replaceFragment(new InventoryFragment());
            }
            else if (item.getItemId() == R.id.battle) {
                replaceFragment(new BossPreviewFragment());
            }
            else if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment(), "CurrentHomeFragment");
            }
            else if (item.getItemId() == R.id.achievements) {
                replaceFragment(new AchievementsFragment());
            }
            else if (item.getItemId() == R.id.tasks) {
                TasksFragment dialogFragment = new TasksFragment();
                dialogFragment.show(getSupportFragmentManager(), null);
            }

            return true;
        });


    }

    // Replaces Fragment (For UI)
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment, String tag){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment,tag).commit();
    }

    public User getUserInfo(){
        return BackgroundService.getUserInfo();
    }

}