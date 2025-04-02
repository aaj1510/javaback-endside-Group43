package com.example.java1d;

public class ListTaskItem {

    private String taskName;
    private String taskDesc;
    private int taskDifficulty;

    public ListTaskItem(String taskName, String taskDesc){
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        // this.taskDifficulty = taskDifficulty;

    }

    public ListTaskItem() {
        // Default constructor is required for Firebase
    }

    public String getTaskName(){
        return taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public int getTaskDifficulty(){
        return taskDifficulty;
    }
}
