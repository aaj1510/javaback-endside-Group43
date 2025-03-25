package com.example.java1d;

public class User {
    private int userId;
    public String username;  // Added username
    public String email;
    public String className;
    public int actionPoints;
    public String password;
    public int totalBossesDefeated;
    public int totalDamageDealt;
    private int gold;
    private Integer bossId; // Foreign key can be null, hence Integer

    // Constructor for creating a new User
    public User(String username, String email, String password,String className,int actionPoints, int totalBossesDefeated,int totalDamageDealt, int gold,Integer bossId) {
        this.username = username;
        this.email = email;
        this.className = className;
        this.password = password;
        this.actionPoints = actionPoints;
        this.totalBossesDefeated = totalBossesDefeated;
        this.totalDamageDealt = totalDamageDealt;
        this.gold = gold;
        this.bossId = bossId;
    }

    public User(String username, String email, String password, String className) {
        this.username = username;
        this.email = email;
        this.className = className;
    }

    // Getters and Setters for each field
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getActionPoints() {
        return actionPoints;
    }

    public void setActionPoints(int actionPoints) {
        this.actionPoints = actionPoints;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTotalBossesDefeated() {
        return totalBossesDefeated;
    }

    public void setTotalBossesDefeated(int totalBossesDefeated) {
        this.totalBossesDefeated = totalBossesDefeated;
    }

    public int getTotalDamageDealt() {
        return totalDamageDealt;
    }

    public void setTotalDamageDealt(int totalDamageDealt) {
        this.totalDamageDealt = totalDamageDealt;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public Integer getBossId() {
        return bossId;
    }

    public void setBossId(Integer bossId) {
        this.bossId = bossId;
    }



}
