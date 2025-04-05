package com.example.java1d;

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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class MinorTaskAdapter extends RecyclerView.Adapter<MinorTaskAdapter.ViewHolder> {

    private List<ListTaskItem> listTasks;
    private DatabaseReference minorTasksRef;
    private DatabaseReference userTasksRef;

    public MinorTaskAdapter(List<ListTaskItem> listTasks){
        this.listTasks = listTasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_minor_task_item, parent, false);
        minorTasksRef = FirebaseDatabase.getInstance().getReference("MinorTasks");
        userTasksRef = FirebaseDatabase.getInstance().getReference("Users");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        ListTaskItem taskItem = listTasks.get(position);
        String userId = taskItem.getUserId();
        Integer difficulty = taskItem.getTaskDifficulty();
        String points = String.format(Locale.US," +%d", difficulty);
        holder.taskPoints.setText(points);
        holder.taskName.setText(taskItem.getTaskName());
        holder.taskDesc.setText(taskItem.getTaskDesc());
        if(taskItem.getTaskCompleted().equals(true)){
            holder.completeBtn.setVisibility(View.GONE);
            holder.cardView.setBackgroundColor(Color.parseColor("#525252"));
        }else {
            holder.completeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    minorTasksRef.child(userId).child(taskItem.getTaskNumber()).child("completed").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Firebase Data", "Updated task completed to true");
                            Toast.makeText(view.getContext(), "Great Job, you earned " + difficulty.toString() + " actions points!", Toast.LENGTH_SHORT).show();
                            userTasksRef.child(userId).child("action_points").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Integer action_points = snapshot.getValue(Integer.class);
                                    action_points += difficulty;
                                    userTasksRef.child(userId).child("action_points").setValue(action_points);
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
    }

    @Override
    public int getItemCount(){
        return listTasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public Button completeBtn;
        public TextView taskDesc;
        public TextView taskName;
        public TextView taskPoints;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            cardView = itemView.findViewById(R.id.minorCardView);
            completeBtn = itemView.findViewById(R.id.minorCompleteBtn);
            taskName = itemView.findViewById(R.id.minorTaskTv);
            taskDesc = itemView.findViewById(R.id.minorDescriptionTv);
            taskPoints = itemView.findViewById(R.id.minorTaskPoints);
        }
    }
}
