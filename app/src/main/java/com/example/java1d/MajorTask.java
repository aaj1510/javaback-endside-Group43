package com.example.java1d;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class MajorTask {
    private String taskId;
    private String userId;
    private String taskName;
    private String taskDescription;
    private LocalDate endDate;
    private LocalTime endTime;
    private Integer difficulty;
    private Boolean completed;

    public MajorTask(){}

    public MajorTask(String userId, String taskId, String taskName, String taskDescription, LocalDate endDate, LocalTime endTime, Integer difficulty){
        this.userId = userId;
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.endDate = endDate;
        this.endTime = endTime;
        this.difficulty = difficulty;
        this.completed = false;
    }

    public String getTaskId(){
        return taskId;
    }

    public String getTaskName(){
        return taskName;
    }

    public void setTaskName(String taskName){
        this.taskName = taskName;
    }

    public String getTaskDescription(){
        return taskDescription;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> majorTaskMap = new HashMap<>();
        majorTaskMap.put("user_id", userId);
        majorTaskMap.put("task_name", taskName);
        majorTaskMap.put("task_description", taskDescription);
        majorTaskMap.put("end_date", endDate);
        majorTaskMap.put("end_time",endTime);
        majorTaskMap.put("difficulty", difficulty);
        majorTaskMap.put("completed", completed);
        return majorTaskMap;
    }
}
