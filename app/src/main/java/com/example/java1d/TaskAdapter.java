package com.example.java1d;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

    private List<ListTaskItem> listTasks;
    private Context context;
    private DatabaseReference databaseReference;


    public TaskAdapter(List<ListTaskItem> listTasks) {
        this.listTasks = listTasks;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task_item, parent, false);
        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListTaskItem taskItem = listTasks.get(position);

        holder.textViewTask.setText(taskItem.getTaskName());
        holder.taskDesc.setText(taskItem.getTaskDesc());
        if(taskItem.getTaskCompleted() == null){

        }
        else if(taskItem.getTaskCompleted() == true){
            holder.completeBtn.setVisibility(View.GONE);
            holder.cardView.setBackgroundColor(Color.parseColor("#525252"));
        } else {
            holder.completeBtn.setTag(taskItem.getTaskId());
            holder.completeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String taskId = (String) holder.completeBtn.getTag();
                    databaseReference = FirebaseDatabase.getInstance().getReference("MajorTasks");
                    databaseReference.child(taskId).child("completed").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            String userId = taskItem.getUserId();
                            Integer task_actionPoints = taskItem.getTaskDifficulty();
                            Log.d("Firebase Data", "Updated task completed to true");
                            Toast.makeText(view.getContext(), "Great Job, you earned " + task_actionPoints.toString() + " actions points!", Toast.LENGTH_SHORT).show();
                            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                            databaseReference.child(userId).child("action_points").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Integer action_points = snapshot.getValue(Integer.class);
                                    action_points += task_actionPoints;
                                    databaseReference.child(userId).child("action_points").setValue(action_points);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });

                }
            });
        }



        // Initially set the description to be hidden
//        holder.taskDesc.setVisibility(View.GONE);

        // Set a click listener on the itemView (the entire card)
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Toggle visibility of the task description
//                if (holder.taskDesc.getVisibility() == View.GONE) {
//                    holder.taskDesc.setVisibility(View.VISIBLE);  // Show the description
//                } else {
//                    holder.taskDesc.setVisibility(View.GONE);     // Hide the description
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return listTasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewTask;
        public TextView taskDesc;
        public Button completeBtn;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            textViewTask = itemView.findViewById(R.id.taskTv);
            taskDesc = itemView.findViewById(R.id.descriptionTv);
            completeBtn = itemView.findViewById(R.id.completeBtn);

        }
    }


}

