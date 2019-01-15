package com.fitnessapp.client.Utils;

public class Routine {
    public String name;
    public Integer duration;

    public Routine(String name, Integer duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName(){
        return this.name;
    }

    public Integer getDuration(){
        return this.duration;
    }
}
