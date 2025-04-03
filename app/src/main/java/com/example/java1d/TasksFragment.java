package com.example.java1d;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.tabs.TabLayout;

public class TasksFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.tasks_page, container, false);
        TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
        tabLayout.getTabAt(0).select();
        getChildFragmentManager().beginTransaction().replace(R.id.task_fragment_layout, new MinorTaskFragment()).commit();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        Log.d("Task Fragment", "Minor task tab opened");
                        getChildFragmentManager().beginTransaction().replace(R.id.task_fragment_layout, new MinorTaskFragment()).commit();
                        break;
                    case 1:
                        // Fetch major tasks when the switch is ON
                        Log.d("Task Fragment", "Major task tab opened");
                        getChildFragmentManager().beginTransaction().replace(R.id.task_fragment_layout, new MajorTaskFragment()).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ImageButton closeButton = rootView.findViewById(R.id.imageButton);
        closeButton.setOnClickListener(v -> dismiss());  // Close the dialog

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
        }
    }

}
