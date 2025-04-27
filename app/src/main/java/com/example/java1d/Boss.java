package com.example.java1d;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Boss implements Parcelable {
    private String bossId;
    private String bossName;
    private Integer bossLevel;
    private Integer bossGold;
    private Integer bossHp;
    private Integer bossCurrentHealth;
    private String bossReward;
    private String bossRemainingTime;

    public Boss(){}

    //Instantiation of Boss Class
    public Boss(String bossId, String bossName, Integer bossGold, Integer bossHp, String bossReward, String bossRemainingTime){
        this.bossId = bossId;
        this.bossName = bossName;
        this.bossGold = bossGold;
        this.bossHp = bossHp;
        this.bossCurrentHealth = bossHp;
        this.bossLevel = 1;
        this.bossReward = bossReward;
        this.bossRemainingTime = bossRemainingTime;
    }

    public String getBossId(){
        return bossId;
    }

    public String getBossName(){
        return bossName;
    }

    public Integer getBossHp(){
        return bossHp;
    }

    public String getBossRemainingTime(){
        return bossRemainingTime;
    }

    public Integer getBossCurrentHealth(){
        return bossCurrentHealth;
    }

    public void setBossCurrentHealth(Integer bossCurrentHealth){
        this.bossCurrentHealth = bossCurrentHealth;
    }

    public Integer getBossLevel(){
        return bossLevel;
    }

    public Integer getBossGold(){
        return bossGold;
    }

    public void setBossLevel(Integer bossLevel){
        this.bossLevel = bossLevel;
    }

    //Maps String keys with corresponding fields for Firebase Database
    public Map<String, Object> toMap(){
        Map<String, Object> bossBattleMap = new HashMap<>();
        bossBattleMap.put("boss_health", bossCurrentHealth);
        bossBattleMap.put("boss_level", bossLevel);
        return bossBattleMap;
    }


    protected Boss (Parcel in){
        bossId = in.readString();
        bossName = in.readString();
        bossGold = in.readInt();
        bossHp = in.readInt();
        bossReward = in.readString();
        bossRemainingTime = in.readString();
        bossCurrentHealth = in.readInt();
        bossLevel = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(bossId);
        dest.writeString(bossName);
        dest.writeInt(bossGold);
        dest.writeInt(bossHp);
        dest.writeString(bossReward);
        dest.writeString(bossRemainingTime);
        dest.writeInt(bossCurrentHealth);
        dest.writeInt(bossLevel);
    }

    public int describeContents() {return 0;}

    public static final Parcelable.Creator<Boss> CREATOR = new Parcelable.Creator<Boss>(){
        @Override
        public Boss createFromParcel(Parcel in) {return new Boss(in);}
        @Override
        public Boss[] newArray(int size) { return new Boss[size];}
    };
}
