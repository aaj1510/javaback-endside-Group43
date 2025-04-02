package com.example.java1d;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;


public class CreateTaskFragment extends DialogFragment {
    private DatabaseReference db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.create_task_page, container, false);
        EditText task_name_input = view.findViewById(R.id.task_name_input);
        EditText task_description_input = view.findViewById(R.id.task_description_input);
        SeekBar difficulty_seekbar = view.findViewById(R.id.difficulty_seekbar);
        Button create_task_button = view.findViewById(R.id.create_task_button);
        Button cancel_task_button = view.findViewById(R.id.cancel_create_button);

        cancel_task_button.setOnClickListener(v -> dismiss());  // Close the dialog

        create_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseDatabase.getInstance().getReference("MajorTasks");
                String task_id = db.push().getKey();
                String task_name = task_name_input.getText().toString();
                String task_description = task_description_input.getText().toString();
                Integer difficulty = difficulty_seekbar.getProgress();
//                LocalDate date =
//                LocalTime time =
                String userId = getUserId();
                MajorTask majorTask = new MajorTask(userId,task_id,task_name,task_description,null,null,difficulty);
                Map<String, Object> taskValues = majorTask.toMap();
                if(task_name.isEmpty()){
                    Toast.makeText(getContext(),"Please enter the task name.", Toast.LENGTH_SHORT).show();
                }
                db.child(task_id).setValue(taskValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(),"Your task have been successfully created",Toast.LENGTH_SHORT).show();
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
        }
    }

    public String getUserId(){
        MainActivity activity = (MainActivity) getActivity();
        return activity.getUserId();
    }
}