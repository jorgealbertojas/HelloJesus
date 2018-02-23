package com.example.jorge.hellojesus.data.onLine.topic.model;

import java.util.List;

/**
 * Created by jorge on 21/02/2018.
 */

public class Content {
    private int id_content;
    private String content_english;
    private String content_portuguese;
    private int time;
    private String corret_option;
    private List<String> stringList;


    public int getId_content() {
        return id_content;
    }

    public void setId_content(int id_content) {
        this.id_content = id_content;
    }

    public String getContent_english() {
        return content_english;
    }

    public void setContent_english(String content_english) {
        this.content_english = content_english;
    }

    public String getContent_portuguese() {
        return content_portuguese;
    }

    public void setContent_portuguese(String content_portuguese) {
        this.content_portuguese = content_portuguese;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getCorret_option() {
        return corret_option;
    }

    public void setCorret_option(String corret_option) {
        this.corret_option = corret_option;
    }
}

