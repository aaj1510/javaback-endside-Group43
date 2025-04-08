package com.example.java1d;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class User implements Parcelable {
    private String userId;
    private String username;
    private String email;
    private String heroClass;
    private Integer gold;
    private Integer actionPoints;
    private Integer totalBossDefeated;
    private Integer totalDamageDealt;
    private String lastLoginDate;
    private Integer rank; //for leaderboard

    public User(){}

    //When creating a new user
    public User(String userId, String username, String email){
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.heroClass = "NIL";
        this.gold = 0;
        this.actionPoints = 0;
        this.totalBossDefeated = 0;
        this.totalDamageDealt = 0;

    }


    //for leaderboard details:
    // will change action_pts to totalBossDefeated later, using action_points for debugging
    public User(String username, String heroClass,  Integer actionPoints,Integer rank ){
        this.username = username;
        this.heroClass = heroClass;
        this.actionPoints = actionPoints;
        this.rank = this.rank;
    }

    //When user logins
    public User(String uid, String username, String email, String heroClass, Integer gold, Integer actionPoints, Integer totalBossDefeated, Integer totalDamageDealt, String lastLoginDate){
        this.userId = uid;
        this.username = username;
        this.email = email;
        this.heroClass = heroClass;
        this.gold = gold;
        this.actionPoints = actionPoints;
        this.totalBossDefeated = totalBossDefeated;
        this.totalDamageDealt = totalDamageDealt;
        this.lastLoginDate = lastLoginDate;
    }

    public String getUserId(){
        return userId;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getHeroClass(){
        return heroClass;
    }

    public void setHeroClass(String heroClass){
        this.heroClass = heroClass;
    }

    public Integer getGold(){
        return gold;
    }

    public void setGold(Integer gold){
        this.gold = gold;
    }

    public Integer getActionPoints(){
        return actionPoints;
    }

    public void setActionPoints(Integer actionPoints){
        this.actionPoints = actionPoints;
    }

    public Integer getTotalBossDefeated(){
        return totalBossDefeated;
    }

    public void setTotalBossDefeated(Integer boss_defeated){
        this.totalBossDefeated = boss_defeated;
    }

    public Integer getTotalDamageDealt(){
        return totalDamageDealt;
    }

    public void setTotalDamageDealt(Integer totalDamageDealt){
        this.totalDamageDealt = totalDamageDealt;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("uid",userId);
        userMap.put("username", username);
        userMap.put("email", email);
        userMap.put("class", heroClass);
        userMap.put("gold", gold);
        userMap.put("action_points", actionPoints);
        userMap.put("total_boss_defeated", totalBossDefeated);
        userMap.put("total_damage_dealt", totalDamageDealt);
        userMap.put("last_login_date", lastLoginDate);
        return userMap;
    }

    public Map<String, Object> attackBossMap(){
        Map<String, Object> attackBossMap = new HashMap<>();
        attackBossMap.put("action_points", actionPoints);
        attackBossMap.put("total_damage_dealt", totalDamageDealt);
        return attackBossMap;
    }

    protected User(Parcel in){
        userId = in.readString();
        username = in.readString();
        email = in.readString();
        heroClass = in.readString();
        gold = in.readInt();
        actionPoints = in.readInt();
        totalBossDefeated = in.readInt();
        totalDamageDealt = in.readInt();
        lastLoginDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(heroClass);
        dest.writeInt(gold);
        dest.writeInt(actionPoints);
        dest.writeInt(totalBossDefeated);
        dest.writeInt(totalDamageDealt);
        dest.writeString(lastLoginDate);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>(){
        @Override
        public User createFromParcel(Parcel in){
            return new User(in);
        }

        @Override
        public User[] newArray(int size){
            return new User[size];
        }
    };
}
