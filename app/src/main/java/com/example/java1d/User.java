package com.example.java1d;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class User implements Parcelable {
    private String uid;
    private String username;
    private String email;
    private String hero_class;
    private Integer gold;
    private Integer action_points;
    private Integer total_boss_defeated;
    private Integer total_damage_dealt;
    private String last_login_date;

    private Integer rank; //for leaderboard

    public User(){}

    //When creating a new user
    public User(String uid, String username, String email){
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.hero_class = "NIL";
        this.gold = 0;
        this.action_points = 0;
        this.total_boss_defeated = 0;
        this.total_damage_dealt = 0;

    }


    //for leaderboard details:
    // will change action_pts to total_boss_defeated later, using action_points for debugging
    public User(String username, String hero_class,  Integer total_damage_dealt,Integer rank ){
        this.username = username;
        this.hero_class = hero_class;
        this.total_damage_dealt = total_damage_dealt;
        this.rank = this.rank;
    }

    //When user logins
    public User(String uid, String username, String email, String hero_class, Integer gold, Integer action_points, Integer total_boss_defeated, Integer total_damage_dealt, String last_login_date){
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.hero_class = hero_class;
        this.gold = gold;
        this.action_points = action_points;
        this.total_boss_defeated = total_boss_defeated;
        this.total_damage_dealt = total_damage_dealt;
        this.last_login_date = last_login_date;
    }

    public String getUid(){
        return uid;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getHero_class(){
        return hero_class;
    }

    public void setHero_class(String hero_class){
        this.hero_class = hero_class;
    }

    public Integer getGold(){
        return gold;
    }

    public void setGold(Integer gold){
        this.gold = gold;
    }

    public Integer getAction_points(){
        return action_points;
    }

    public void setAction_points(Integer action_points){
        this.action_points = action_points;
    }

    public Integer getTotal_boss_defeated(){
        return total_boss_defeated;
    }

    public void setTotal_boss_defeated(Integer boss_defeated){
        this.total_boss_defeated = boss_defeated;
    }

    public Integer getTotal_damage_dealt(){
        return total_damage_dealt;
    }

    public void setTotal_damage_dealt(Integer total_damage_dealt){
        this.total_damage_dealt = total_damage_dealt;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("uid",uid);
        userMap.put("username", username);
        userMap.put("email", email);
        userMap.put("class", hero_class);
        userMap.put("gold", gold);
        userMap.put("action_points", action_points);
        userMap.put("total_boss_defeated", total_boss_defeated);
        userMap.put("total_damage_dealt", total_damage_dealt);
        userMap.put("last_login_date", last_login_date);
        return userMap;
    }

    protected User(Parcel in){
        uid = in.readString();
        username = in.readString();
        email = in.readString();
        hero_class = in.readString();
        gold = in.readInt();
        action_points = in.readInt();
        total_boss_defeated = in.readInt();
        total_damage_dealt = in.readInt();
        last_login_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(hero_class);
        dest.writeInt(gold);
        dest.writeInt(action_points);
        dest.writeInt(total_boss_defeated);
        dest.writeInt(total_damage_dealt);
        dest.writeString(last_login_date);
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
