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
    private DatabaseReference minorTasksRef;
    private DatabaseReference presetTasksRef;
    private List<ListTaskItem> taskList;
    private MinorTaskAdapter minorTaskAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_minor_task, container, false);
        taskList = new ArrayList<>();
        minorTaskAdapter = new MinorTaskAdapter(taskList);
        RecyclerView recyclerView = rootView.findViewById(R.id.minor_task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(minorTaskAdapter);
        getMinorTasks();
        return rootView;
    }

    private void getMinorTasks(){
        User user = getUserInfo();
        String userId = user.getUserId();
        minorTasksRef = FirebaseDatabase.getInstance().getReference("MinorTasks");
        presetTasksRef = FirebaseDatabase.getInstance().getReference("PresetTasks");
        minorTasksRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taskList.clear();
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        String taskNumber = dataSnapshot.getKey();
                        Log.d("Minor Task Fragment", taskNumber);
                        String taskId = dataSnapshot.child("task_id").getValue(String.class);
                        Log.d("Minor Task Fragment", taskId );
                        Boolean completed = dataSnapshot.child("completed").getValue(Boolean.class);
                        presetTasksRef.child(taskId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String taskName = snapshot.child("task_name").getValue(String.class);
                                    String taskDesc = snapshot.child("task_description").getValue(String.class);
                                    Integer difficulty = snapshot.child("difficulty").getValue(Integer.class);

                                    ListTaskItem taskItem = new ListTaskItem(userId,taskNumber,taskName,taskDesc,difficulty,completed);
                                    taskList.add(taskItem);

                                minorTaskAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("FirebaseData", "Failed to read data: " + error.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private User getUserInfo(){
        MainActivity activity = (MainActivity) getActivity();
        return activity.getUserInfo();
    }
}