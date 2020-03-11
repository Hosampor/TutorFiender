package com.example.tutor.finder1.Helper;

public class EngagedPerson {
    String image;
    String name;
    String date;
    String taka;

    public EngagedPerson() {
    }

    public EngagedPerson(String image, String name, String date, String taka) {
        this.image = image;
        this.name = name;
        this.date = date;
        this.taka = taka;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTaka() {
        return taka;
    }

    public void setTaka(String taka) {
        this.taka = taka;
    }
}
