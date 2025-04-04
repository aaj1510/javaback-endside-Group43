package com.example.java1d;

public class AchievementsList {
    private String rankUsername;
    private String rankCriteria;
    private Integer rankCriteriaValue;
    public AchievementsList(){
    }
    public AchievementsList(String rankUsername, String rankCriteria, Integer rankCriteriaValue){
        this.rankUsername = rankUsername;
        this.rankCriteria = rankCriteria;
        this.rankCriteriaValue = rankCriteriaValue;
    }
    public String getUsername(){
        return rankUsername;
    }
    public String getRankBy(){
        return rankCriteria + ": " + rankCriteriaValue;
    }
}
