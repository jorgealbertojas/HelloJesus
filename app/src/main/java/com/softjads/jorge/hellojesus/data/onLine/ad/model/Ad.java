package com.softjads.jorge.hellojesus.data.onLine.ad.model;

import java.io.Serializable;

public class Ad implements Serializable {

    private int id;
    private String name;
    private String link;
    private String url_Image;
    private String text;
    private String description;
    private String star_date;
    private String expiration_date;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUrl_Image() {
        return url_Image;
    }

    public void setUrl_Image(String url_Image) {
        this.url_Image = url_Image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStar_date() {
        return star_date;
    }

    public void setStar_date(String star_date) {
        this.star_date = star_date;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }
}
