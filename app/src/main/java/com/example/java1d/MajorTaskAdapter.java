package com.example.java1d;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
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
    private DatabaseReference userTasksReference;


    private FragmentManager fragmentManager;

    public MajorTaskAdapter(List<ListTaskItem> listTasks, FragmentActivity context) {
        this.listTasks = listTasks;
        this.context = context;
        this.fragmentManager = context.getSupportFragmentManager();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_major_task_item, parent, false);

        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = getUserInfo();
        String userId = user.getUserId();
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
        if(taskItem.getTaskCompleted()){
            holder.completeBtn.setBackground(ContextCompat.getDrawable(holder.completeBtn.getContext(), android.R.drawable.checkbox_on_background));
            holder.cardView.setBackgroundColor(Color.parseColor("#E0D599"));
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.editBtn.setVisibility(View.GONE);
            holder.completeBtn.setEnabled(false);
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String taskId = (String) holder.completeBtn.getTag();
                    databaseReference.child(taskId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            notifyDataSetChanged();
                            Toast.makeText(view.getContext(), "Task successfully deleted",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });



        } else {
            holder.completeBtn.setBackground(ContextCompat.getDrawable(holder.completeBtn.getContext(), android.R.drawable.checkbox_off_background));
            holder.cardView.setBackgroundColor(Color.parseColor("#FDF0A8"));
            holder.deleteBtn.setVisibility(View.GONE);
            holder.editBtn.setVisibility(View.VISIBLE);
            holder.completeBtn.setEnabled(true);
            holder.completeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String taskId = (String) holder.completeBtn.getTag();
                    databaseReference.child(taskId).child("completed").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            taskItem.setTaskCompleted(true);
                            notifyDataSetChanged();
                            Integer task_actionPoints = taskItem.getTaskDifficulty();
                            Log.d("Firebase Data", "Updated task completed to true");
                            Toast.makeText(view.getContext(), "Great Job, you earned " + task_actionPoints.toString() + " actions points!", Toast.LENGTH_SHORT).show();
                            userTasksReference = FirebaseDatabase.getInstance().getReference("Users");
                            userTasksReference.child(userId).child("action_points").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Integer action_points = snapshot.getValue(Integer.class);
                                    action_points += task_actionPoints;
                                    userTasksReference.child(userId).child("action_points").setValue(action_points);
                                    user.setActionPoints(action_points);
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
        //can only edit task if task is not completed - done thru visibility of button
        holder.editBtn.setOnClickListener(v -> {
            // get taskid from complete button tag
            String taskId = (String) holder.completeBtn.getTag();
            //use newInstance function in updateTaskFragment to store the taskId and pass to the fragment
            UpdateTaskFragment fragment = UpdateTaskFragment.newInstance(taskId);
            //need fragmentManager to display the updateTaskFragment
            fragment.show(fragmentManager, "UpdateTaskFragment");

        });
    }

    public User getUserInfo(){
        return BackgroundService.getUserInfo();
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
        public ImageButton completeBtn;
        public Button deleteBtn;
        public CardView cardView;
        public ImageView descIcon;

        public ImageView editBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            textViewTask = itemView.findViewById(R.id.minorTaskTv);
            taskDesc = itemView.findViewById(R.id.minorDescriptionTv);
            taskDeadline = itemView.findViewById(R.id.taskDeadline);
            taskPoints = itemView.findViewById(R.id.minorTaskPoints);
            completeBtn = itemView.findViewById(R.id.minorCompleteBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            descIcon = itemView.findViewById(R.id.desc_icon);
            editBtn = itemView.findViewById(R.id.editTaskBtn);


        }
    }


}

