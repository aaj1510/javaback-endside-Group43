package com.example.java1d;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
//        setContentView(R.layout.activity_main);
        replaceFragment(new HomeFragment());
        getUserId();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // TODO: Enable Navigation Bar on Home, Inventory, Tasks and Achievements (Leaderboard)
        // For Navigation Bar


        binding.navigationBar.setBackground(null);
        binding.navigationBar.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.inventory){
                replaceFragment(new InventoryFragment());
            }
//            else if (item.getItemId() == R.id.tasks) {
//                replaceFragment(new TaskFragment());
//            }
            else if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            }
            else if (item.getItemId() == R.id.achievements) {
                replaceFragment(new AchievementsFragment());
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

    public String getUserId(){
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user_key");
        if(user!= null){
            return user.getUid();
        }
        return null;
    }
}