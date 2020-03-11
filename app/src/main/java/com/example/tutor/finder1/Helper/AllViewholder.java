package com.example.tutor.finder1.Helper;

/**
 * Created by Rana on 20-01-18.
 */

public class AllViewholder {

    String name;
    String institute_name;
    String subject;
    String gender;
    String age;
    String interested_area;
    String interested_class;
    String interested_subject;
    String housing_name;
    String contact;
    String email;
    String image;

    public AllViewholder() {
    }

    public AllViewholder(String name, String institute_name, String subject,
                         String gender, String age, String interested_area, String interested_class,
                         String interested_subject, String housing_name, String contact, String email, String image) {
        this.name = name;
        this.institute_name = institute_name;
        this.subject = subject;
        this.gender = gender;
        this.age = age;
        this.interested_area = interested_area;
        this.interested_class = interested_class;
        this.interested_subject = interested_subject;
        this.housing_name = housing_name;
        this.contact = contact;
        this.email = email;
        this.image = image;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getInterested_area() {
        return interested_area;
    }

    public void setInterested_area(String interested_area) {
        this.interested_area = interested_area;
    }

    public String getInterested_class() {
        return interested_class;
    }

    public void setInterested_class(String interested_class) {
        this.interested_class = interested_class;
    }

    public String getInterested_subject() {
        return interested_subject;
    }

    public void setInterested_subject(String interested_subject) {
        this.interested_subject = interested_subject;
    }

    public String getHousing_name() {
        return housing_name;
    }

    public void setHousing_name(String housing_name) {
        this.housing_name = housing_name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
