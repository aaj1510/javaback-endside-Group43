package com.example.java1d;

public class ListTaskItem {
    private String taskId;
    private String taskNumber;
    private String taskName;
    private String taskDesc;
    private Boolean taskCompleted;
    private Integer taskDifficulty;
    private String taskEndDate;
    private String taskEndTime;

    public ListTaskItem(String taskId, String taskName, String taskDesc, String taskEndDate, String taskEndTime, Boolean taskCompleted, Integer taskDifficulty){
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.taskEndDate = taskEndDate;
        this.taskEndTime = taskEndTime;
        this.taskCompleted = taskCompleted;
        this.taskDifficulty = taskDifficulty;

    }

    public ListTaskItem(String taskNumber ,String taskName, String taskDesc, Integer taskDifficulty, Boolean taskCompleted){
        this.taskNumber = taskNumber;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.taskDifficulty = taskDifficulty;
        this.taskCompleted = taskCompleted;
    }

    public ListTaskItem() {
    }

    public String getTaskName(){
        return taskName;
    }
    public String getTaskDesc() {
        return taskDesc;
    }
    public String getTaskId() {return taskId;}
    public String getTaskEndDate() {return taskEndDate;}
    public String getTaskEndTime() {return taskEndTime;}
    public Boolean getTaskCompleted() {return taskCompleted;}
    public void setTaskCompleted(Boolean value) {this.taskCompleted = value;};
    public Integer getTaskDifficulty() {return taskDifficulty;}
    public String getTaskNumber() {return taskNumber;}
}
