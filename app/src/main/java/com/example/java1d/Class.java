package com.example.java1d;

import java.util.Base64;

public class Class {
    private String class_name;
    private String traits;
    private Base64 hero_sprite;
    private Object skills;


    public Class(){}

    public String getClass_name(){
        return class_name;
    }

    public void setClass_name(String class_name){
        this.class_name = class_name;
    }
    public String getTraits(){
        return traits;
    }

    public void setTraits(String traits){
        this.traits = traits;
    }

    public Object getSkills(){
        return skills;
    }

    public void setSkills(Object skills){
        this.skills = skills;
    }

    public Base64 getHero_sprite(){
        return hero_sprite;
    }

    public void setHero_sprite(Base64 sprite){
        hero_sprite = sprite;
    }

    @Override
    public String toString(){
        return "Class{" + "class= " + class_name + ", traits= " + traits + "}";
    }
}
