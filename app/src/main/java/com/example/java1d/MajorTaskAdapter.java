package com.example.java1d;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class MajorTaskAdapter extends RecyclerView.Adapter<MajorTaskAdapter.ViewHolder>{

    private List<ListTaskItem> listTasks;
    private Context context;
    private DatabaseReference databaseReference;


    public MajorTaskAdapter(List<ListTaskItem> listTasks) {
        this.listTasks = listTasks;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_major_task_item, parent, false);
        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListTaskItem taskItem = listTasks.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference("MajorTasks");
        String date = taskItem.getTaskEndDate();
        String time = taskItem.getTaskEndTime();
        String dateTime = String.format("%s, %s", date, time);
        Integer difficulty = taskItem.getTaskDifficulty();
        String points = String.format(Locale.US," +%d", difficulty);
        holder.taskPoints.setText(points);
        holder.taskDeadline.setText(dateTime);
        holder.completeBtn.setTag(taskItem.getTaskId());
        holder.textViewTask.setText(taskItem.getTaskName());
        holder.taskDesc.setText(taskItem.getTaskDesc());
        if(taskItem.getTaskDesc() == null || taskItem.getTaskDesc().isEmpty()){
            holder.descIcon.setVisibility(View.GONE);
        }
        if(taskItem.getTaskCompleted() == true){
            holder.completeBtn.setText("Delete");
            holder.completeBtn.setBackgroundColor(Color.parseColor("#990303"));
            holder.completeBtn.setOnClickListener(new View.OnClickListener() {
                int position = holder.getAdapterPosition();
                @Override
                public void onClick(View view) {
                    String taskId = (String) holder.completeBtn.getTag();
                    databaseReference.child(taskId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //BUG: DELETE WILL CAUSE THE NEXT ITEM TO BE BUGGED BECAUSE THE DATABASE IS RETRIEVED BUT THE VIEW IS NOT UPDATED
//                            for (int i = 0; i < listTasks.size(); i ++){
//                                if (listTasks.get(i).getTaskId().equals(taskId)) {
//                                    listTasks.remove(i);
//                                    notifyItemRemoved(i);
//                                    notifyItemRangeChanged(i, listTasks.size());
//                                    break;
//                                }
//                            }
                            Toast.makeText(view.getContext(), "Task successfully deleted",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            holder.cardView.setBackgroundColor(Color.parseColor("#525252"));
        } else {
            holder.completeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String taskId = (String) holder.completeBtn.getTag();
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
        public TextView taskDeadline;
        public TextView taskPoints;
        public Button completeBtn;
        public CardView cardView;
        public ImageView descIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            textViewTask = itemView.findViewById(R.id.minorTaskTv);
            taskDesc = itemView.findViewById(R.id.minorDescriptionTv);
            taskDeadline = itemView.findViewById(R.id.taskDeadline);
            taskPoints = itemView.findViewById(R.id.minorTaskPoints);
            completeBtn = itemView.findViewById(R.id.minorCompleteBtn);
            descIcon = itemView.findViewById(R.id.desc_icon);


        }
    }


}

