package com.example.java1d;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class TaskFragment extends DialogFragment {
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tasks_page, container, false);
        ImageButton close_button = view.findViewById(R.id.close_task_fragment_button);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Button create_task_btn = view.findViewById(R.id.create_task_btn);
        create_task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateTaskFragment createTaskFragment = new CreateTaskFragment();
                createTaskFragment.show(getActivity().getSupportFragmentManager(), "CreateTaskFragment");
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
       }
    }
}