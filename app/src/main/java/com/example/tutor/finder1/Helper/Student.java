package com.example.tutor.finder1.Helper;

public class Student {
    String name;
    String institute_name;
    String phone;
    String days;
    String time;
    String image;
    String email;
    String password;
    String class_name;

    public Student() {
    }

    public Student(String name, String institute_name, String phone, String days, String time, String image, String email, String password, String class_name) {
        this.name = name;
        this.institute_name = institute_name;
        this.phone = phone;
        this.days = days;
        this.time = time;
        this.image = image;
        this.email = email;
        this.password = password;
        this.class_name = class_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitute_name() {
        return institute_name;
    }

    public void setInstitute_name(String institute_name) {
        this.institute_name = institute_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}
