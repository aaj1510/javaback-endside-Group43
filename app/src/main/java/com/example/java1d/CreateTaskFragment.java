package com.example.java1d;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;
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
        EditText date_input = view.findViewById(R.id.task_dateline_input);
        EditText time_input = view.findViewById(R.id.task_duration_input);
        SeekBar difficulty_seekbar = view.findViewById(R.id.difficulty_seekbar);
        ImageButton create_task_button = view.findViewById(R.id.create_task_button);
        ImageButton cancel_task_button = view.findViewById(R.id.cancel_create_button);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        cancel_task_button.setOnClickListener(v -> dismiss());  // Close the dialog

        create_task_button.setOnClickListener(new View.OnClickListener() { //On clicking create button
            @Override
            public void onClick(View v) {
                db = FirebaseDatabase.getInstance().getReference("MajorTasks");
                User user = getUserInfo();
                //Get all the values from the inputs
                String task_id = db.push().getKey(); //Allows firebase to create a unique key for the primary key of storing the data
                String task_name = task_name_input.getText().toString();
                String task_description = task_description_input.getText().toString();
                Integer difficulty = difficulty_seekbar.getProgress() + 1;
                String selected_date = date_input.getText().toString();
                String selected_time = time_input.getText().toString();
                String userId = user.getUserId();
                //Creates an instance of Major Task class with the require values
                MajorTask majorTask = new MajorTask(userId,task_id,task_name,task_description,selected_date,selected_time,difficulty);
                Map<String, Object> taskValues = majorTask.toMap(); //Calls method to map values with firebase field
                if(task_name.isEmpty() || task_name.equals(" ")){ //Check if task name input is empty
                    Toast.makeText(getContext(),"Please enter the task name.", Toast.LENGTH_SHORT).show();
                }
                if (selected_date.isEmpty()){ //Checks if date input is empty
                    Toast.makeText(getContext(), "Please select an end date", Toast.LENGTH_SHORT).show();
                }
                if (selected_time.isEmpty()){ //Checks if time input is empty
                    Toast.makeText(getContext(), "Please select an end time", Toast.LENGTH_SHORT).show();
                }
                if(!task_name.isEmpty() && !selected_date.isEmpty() && !selected_time.isEmpty()){ //If necessary fields are not empty
                    db.child(task_id).setValue(taskValues).addOnSuccessListener(new OnSuccessListener<Void>() { //Update major task table in firebase
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(),"Your task have been successfully created",Toast.LENGTH_SHORT).show();
                            dismiss(); //dismiss dialog on success
                        }
                    });
                }
            }
        });

        date_input.setOnClickListener(new View.OnClickListener() { //On clicking date input
            @Override
            public void onClick(View view) {
                //Creates a calendar instance and gets the current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                //Creates an instance of a date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),(dateView, selectedYear, selectedMonth, selectedDay) -> {
                    String dateString = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    date_input.setText(dateString); //set date input to selected date
                }, year,month,day); //Set value of date picker to current date
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis()); //Set selectable dates to be current and future days only
                datePickerDialog.show();//Display date picker
            }
        });

        time_input.setOnClickListener(new View.OnClickListener() { //On clicking time input
            @Override
            public void onClick(View view) {
                //Creates a calender instance and gets the current time
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                //Creates an instance of a time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),(timeView, selectedHour, selectedMinutes) -> {
                    String timeString = String.format(Locale.US,"%02d:%02d", selectedHour, selectedMinutes); //Formats the time
                    time_input.setText(timeString); //Set the time input to be the selected time
                }, hour,minutes,true); //Sets value of the time picker to current time
                timePickerDialog.show();//Display time picker
            }
        });



        return view;
    }

    @Override
    public void onStart(){ //Settings for sizing and fullscreen effect of dialog
        super.onStart();
        if(getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            getDialog().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }
    }

    public User getUserInfo(){
        return BackgroundService.getUserInfo();
    } //Method to get user data
}