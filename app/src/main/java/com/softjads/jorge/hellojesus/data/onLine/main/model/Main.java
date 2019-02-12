package com.softjads.jorge.hellojesus.data.onLine.main.model;

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
    private String title_bible;
    private String title_music;
    private String title_write;
    private String title_exercise;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle_bible() {
        return title_bible;
    }

    public void setTitle_bible(String title_bible) {
        this.title_bible = title_bible;
    }

    public String getTitle_music() {
        return title_music;
    }

    public void setTitle_music(String title_music) {
        this.title_music = title_music;
    }

    public String getTitle_write() {
        return title_write;
    }

    public void setTitle_write(String title_write) {
        this.title_write = title_write;
    }

    public String getTitle_exercise() {
        return title_exercise;
    }

    public void setTitle_exercise(String title_exercise) {
        this.title_exercise = title_exercise;
    }

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
