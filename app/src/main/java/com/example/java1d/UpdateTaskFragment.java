package com.example.java1d;



import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class UpdateTaskFragment extends DialogFragment {
    private DatabaseReference db;
    private String taskId;

    private String taskName;



    public static UpdateTaskFragment newInstance(String taskId) {
        UpdateTaskFragment fragment = new UpdateTaskFragment();
        Bundle args = new Bundle();
        args.putString("taskId", taskId); // Pass the task ID to the fragment
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.update_task_page, container, false);
        EditText taskNameInput = view.findViewById(R.id.task_name_input);
        EditText taskDescriptionInput = view.findViewById(R.id.task_description_input);
        EditText dateInput = view.findViewById(R.id.task_dateline_input);
        EditText timeInput = view.findViewById(R.id.task_duration_input);
        SeekBar difficultySeekbar = view.findViewById(R.id.difficulty_seekbar);
        ImageButton updateTaskBtn = view.findViewById(R.id.update_task_button);
        ImageButton cancelTaskBtn = view.findViewById(R.id.cancel_task_button);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        // Retrieve the taskId
        taskId = getArguments().getString("taskId");


        db = FirebaseDatabase.getInstance().getReference("MajorTasks");
        db.child(taskId).addListenerForSingleValueEvent(new ValueEventListener() {
            //get data to show in update fragment based on taskid
            @Override
            public void onDataChange(@NonNull DataSnapshot taskSnapshot) {
                String taskName = taskSnapshot.child("task_name").getValue(String.class);
                String taskDesc = taskSnapshot.child("task_description").getValue(String.class);
                String taskEndDate = taskSnapshot.child("end_date").getValue(String.class);
                String taskEndTime = taskSnapshot.child("end_time").getValue(String.class);
                Integer difficulty = taskSnapshot.child("difficulty").getValue(Integer.class);

                taskNameInput.setText(taskName);
                taskDescriptionInput.setText(taskDesc);
                dateInput.setText(taskEndDate);
                timeInput.setText(taskEndTime);
                Log.d("Seekbar", "Value " + difficulty);
                difficultySeekbar.setProgress(difficulty-1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Log.d("FirebaseData", "Failed to read data: " + error.getMessage());
            }
        });


        cancelTaskBtn.setOnClickListener(v -> dismiss());  // Close the dialog

        updateTaskBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String taskName = taskNameInput.getText().toString();
                String taskDesc = taskDescriptionInput.getText().toString();
                Integer difficulty = difficultySeekbar.getProgress() + 1;
                String selected_date = dateInput.getText().toString();
                String selected_time = timeInput.getText().toString();

                if (taskName.isEmpty() || selected_date.isEmpty() || selected_time.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = getUserInfo();
                String userId = user.getUserId();

                MajorTask majorTask = new MajorTask(userId,
                        taskId,taskName,taskDesc,selected_date,selected_time,difficulty);
                Map<String, Object> taskValues = majorTask.toMap();

                db.child(taskId).setValue(taskValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Your task has been successfully updated", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });

             }
         });

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),(dateView, selectedYear, selectedMonth, selectedDay) -> {
                    String dateString = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dateInput.setText(dateString);
                }, year,month,day);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),(timeView, selectedHour, selectedMinutes) -> {
                    String timeString = String.format(Locale.US,"%02d:%02d", selectedHour, selectedMinutes);
                    timeInput.setText(timeString);
                }, hour,minutes,true);
                timePickerDialog.show();
            }
        });
        return view;
    }

    public void onStart(){
        super.onStart();

        if(getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
        }
    }

    public User getUserInfo(){
        return BackgroundService.getUserInfo();
    }
}
