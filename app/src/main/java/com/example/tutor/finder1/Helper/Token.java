package com.example.tutor.finder1.Helper;

/**
 * Created by Rana on 04-02-18.
 */

public class Token {
    String name;
    String time;

    public Token() {
    }

    public Token(String name, String time) {
        this.name = name;
        this.time = time;
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
}
