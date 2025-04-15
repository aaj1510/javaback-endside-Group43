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
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MajorTaskFragment extends Fragment {

    private DatabaseReference databaseReference;
    private List<ListTaskItem> taskList;
    private MajorTaskAdapter majorTaskAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        taskList = new ArrayList<>();
        majorTaskAdapter = new MajorTaskAdapter(taskList,getActivity());
        View rootView = inflater.inflate(R.layout.fragment_major_task, container, false);
        Button create_task_button = rootView.findViewById(R.id.create_task_btn);
        create_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateTaskFragment createTaskFragment = new CreateTaskFragment();
                createTaskFragment.show(getActivity().getSupportFragmentManager(), "CreateTaskFragment");
                taskList.clear();
            }
        });
        RecyclerView recyclerView = rootView.findViewById(R.id.major_task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(majorTaskAdapter);
        getMajorTasks();
        return rootView;
    }

    public void getMajorTasks(){
        User user = getUserInfo();
        String userId = user.getUserId();
        Log.d("UserID", userId);
        databaseReference = FirebaseDatabase.getInstance().getReference("MajorTasks");
        Query userMajorTasksQuery = databaseReference.orderByChild("user_id").equalTo(userId);
        userMajorTasksQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taskList.clear();
                Log.d("FirebaseData", "DataSnapshot: " + snapshot.toString());

                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    String taskId = taskSnapshot.getKey();
                    String taskName = taskSnapshot.child("task_name").getValue(String.class);
                    String taskDesc = taskSnapshot.child("task_description").getValue(String.class);
                    String taskEndDate = taskSnapshot.child("end_date").getValue(String.class);
                    String taskEndTime = taskSnapshot.child("end_time").getValue(String.class);
                    Boolean taskCompleted = taskSnapshot.child("completed").getValue(Boolean.class);
                    Integer difficulty = taskSnapshot.child("difficulty").getValue(Integer.class);

                    // Create a new ListTaskItem and add it to the list
                    ListTaskItem taskItem = new ListTaskItem(taskId, taskName, taskDesc, taskEndDate, taskEndTime,taskCompleted, difficulty);
                    taskList.add(taskItem);
                }


                // Notify the adapter that the data has been updated
                majorTaskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Log.d("FirebaseData", "Failed to read data: " + error.getMessage());
            }
        });
    }


    public User getUserInfo(){
        return BackgroundService.getUserInfo();
    }
}