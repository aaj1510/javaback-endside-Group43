package com.example.java1d;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PresetTasksFragment extends DialogFragment {

    private List<ListTaskItem> taskList;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private Switch taskModeSwitch;

    private DatabaseReference databaseReference;
    private DatabaseReference db_majorRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.presettasks_page, container, false);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("PresetTasks");
        db_majorRef = FirebaseDatabase.getInstance().getReference("MajorTasks");

        // Initialize the task list and adapter
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);
        // Fetch data using fetchPresetTasks
        //fetchPresetTasks();
        // Set up the RecyclerView
        recyclerView = rootView.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(taskAdapter);

        Button create_task_btn = rootView.findViewById(R.id.create_task_btn);
        Button clear_task_btn = rootView.findViewById(R.id.clear_task_btn);


        taskModeSwitch = rootView.findViewById(R.id.taskModeSwitch);
        taskModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Fetch major tasks when the switch is ON
                fetchMajorTasks();
                // show the buttons to create tasks: add and clear
                create_task_btn.setVisibility(View.VISIBLE);
                clear_task_btn.setVisibility(View.VISIBLE);

                taskModeSwitch.setText("Major Tasks");
            } else {
                // Fetch minor tasks when the switch is OFF
                fetchPresetTasks();
                //hide those buttons
                create_task_btn.setVisibility(View.GONE);
                clear_task_btn.setVisibility(View.GONE);

                taskModeSwitch.setText("Minor Tasks");
            }

        });

        // Initially, fetch minor tasks
        fetchPresetTasks();

        create_task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateTaskFragment createTaskFragment = new CreateTaskFragment();
                createTaskFragment.show(getActivity().getSupportFragmentManager(), "CreateTaskFragment"); //need to fix layout
            }
        });

        ImageButton closeButton = rootView.findViewById(R.id.imageButton);
        closeButton.setOnClickListener(v -> dismiss());  // Close the dialog




        return rootView;
    }

    /* private List<ListTaskItem> generateTasks() { //hardcoded values to test layout design only
        List<ListTaskItem> taskList = new ArrayList<>();
        taskList.add(new ListTaskItem("Task 1", "Just Binge every single season"));
        taskList.add(new ListTaskItem("Task 2", "Checkoff 2 is due soon"));
        taskList.add(new ListTaskItem("Task 3", "Example task description"));
        return taskList;
    } */

    private void fetchPresetTasks() {
        // Start by clearing any old data
        taskList.clear();
        String heroClass = getHeroClass();
        // Add ValueEventListener to fetch data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Log the entire snapshot for debugging
                Log.d("FirebaseData", "DataSnapshot: " + snapshot.toString());


                // Check if "Mage" node exists
                if (snapshot.child(heroClass).exists()) {
                    Log.d("FirebaseData", heroClass + "exists.");

                    for (DataSnapshot taskSnapshot : snapshot.child(heroClass).getChildren()) {
                        String taskName = taskSnapshot.child("task_name").getValue(String.class);
                        String taskDesc = taskSnapshot.child("task_description").getValue(String.class);
                        //int taskDifficulty = taskSnapshot.child("Difficulty").getValue(Integer.class); //add later
                        //Log.d("FirebaseData", "Task Name: " + taskName); // Debug log for task names

                        // Create a new ListTaskItem and add it to the list
                        ListTaskItem taskItem = new ListTaskItem(taskName, taskDesc);
                        taskList.add(taskItem);
                    }
                }

                // Notify the adapter that the data has been updated
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Log.d("FirebaseData", "Failed to read data: " + error.getMessage());
            }
        });
        //return taskList;
    }




    // get major tasks based on userid
    private void fetchMajorTasks() {
        // Start by clearing any old data
        taskList.clear();
        String userId = getUserId();

        Query userTasksQuery = db_majorRef.orderByChild("user_id").equalTo(userId);
        // Add ValueEventListener to fetch data from Firebase
        userTasksQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Log the entire snapshot for debugging
                Log.d("FirebaseData", "DataSnapshot: " + snapshot.toString());


                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    String taskName = taskSnapshot.child("task_name").getValue(String.class);
                    String taskDesc = taskSnapshot.child("task_description").getValue(String.class);


                    // Create a new ListTaskItem and add it to the list
                    ListTaskItem taskItem = new ListTaskItem(taskName, taskDesc);
                    taskList.add(taskItem);
                }


                // Notify the adapter that the data has been updated
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Log.d("FirebaseData", "Failed to read data: " + error.getMessage());
            }
        });
        //return taskList;
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

    public String getUserId(){
        MainActivity activity = (MainActivity) getActivity();
        return activity.getUserId();
    }

    public String getHeroClass(){
        MainActivity activity = (MainActivity) getActivity();
        return activity.getHeroClass();
    }


}
