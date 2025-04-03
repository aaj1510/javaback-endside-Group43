package com.example.java1d;

public class ListTaskItem {
    private String userId;
    private String taskId;
    private String taskName;
    private String taskDesc;
    private Boolean taskCompleted;
    private Integer taskDifficulty;

    public ListTaskItem(String userId, String taskId, String taskName, String taskDesc, Boolean taskCompleted, Integer taskDifficulty){
        this.userId = userId;
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.taskCompleted = taskCompleted;
         this.taskDifficulty = taskDifficulty;

    }

    public ListTaskItem(String taskName, String taskDesc){
        this.taskName = taskName;
        this.taskDesc = taskDesc;
    }

    public ListTaskItem() {
        // Default constructor is required for Firebase
    }

    public String getUserId() {return userId;}
    public String getTaskName(){
        return taskName;
    }
    public String getTaskDesc() {
        return taskDesc;
    }
    public String getTaskId() {return taskId;}
    public Boolean getTaskCompleted() {return taskCompleted;}
    public Integer getTaskDifficulty(){
        return taskDifficulty;
    }
}
