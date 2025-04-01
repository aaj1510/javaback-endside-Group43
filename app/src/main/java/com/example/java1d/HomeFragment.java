package com.example.java1d;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_page, container, false);
        ImageButton task_button = view.findViewById(R.id.task_button);
        task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskFragment dialogFragment = new TaskFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "TaskFragment");
            }
        });


        return view;
    }
}