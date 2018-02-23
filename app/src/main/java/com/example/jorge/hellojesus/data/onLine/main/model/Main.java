package com.example.jorge.hellojesus.data.onLine.main.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jorge on 22/02/2018.
 */

public class Main implements Serializable {
    private int id;
    private String name;
    private String time;
    private String description;
    private List<Integer> topics;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescripition() {
        return description;
    }

    public void setDescripition(String descripition) {
        this.description = descripition;
    }

    public List<Integer> getTopics() {
        return topics;
    }

    public void setTopics(List<Integer> topics) {
        this.topics = topics;
    }
}
