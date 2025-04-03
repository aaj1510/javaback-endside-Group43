package com.example.java1d;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MinorTaskFragment extends Fragment {
    private DatabaseReference databaseReference;
    private List<ListTaskItem> taskList;
    private TaskAdapter taskAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_minor_task, container, false);
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);
        RecyclerView recyclerView = rootView.findViewById(R.id.minor_task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(taskAdapter);
        getMinorTasks();
        return rootView;
    }

    private void getMinorTasks(){
        User user = getUserInfo();
        String heroClass = user.getHero_class();
        databaseReference = FirebaseDatabase.getInstance().getReference("PresetTasks");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                Log.d("FirebaseData", "Failed to read data: " + error.getMessage());
            }
        });
    }

    private User getUserInfo(){
        MainActivity activity = (MainActivity) getActivity();
        return activity.getUserInfo();
    }
}