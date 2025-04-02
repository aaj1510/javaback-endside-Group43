package com.example.java1d;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

    private List<ListTaskItem> listTasks;
    private Context context;


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

        // Initially set the description to be hidden
        holder.taskDesc.setVisibility(View.GONE);

        // Set a click listener on the itemView (the entire card)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of the task description
                if (holder.taskDesc.getVisibility() == View.GONE) {
                    holder.taskDesc.setVisibility(View.VISIBLE);  // Show the description
                } else {
                    holder.taskDesc.setVisibility(View.GONE);     // Hide the description
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listTasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewTask;
        public TextView taskDesc;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTask = itemView.findViewById(R.id.taskTv);
            taskDesc = itemView.findViewById(R.id.descriptionTv);

        }
    }


}

