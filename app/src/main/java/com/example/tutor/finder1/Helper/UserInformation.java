package com.example.tutor.finder1.Helper;

public class UserInformation {
    String name;
    String image;
    String institute_name;
    String date;
    String amount;
    String mobile;



    public UserInformation() {
    }

    public UserInformation(String name, String image, String institute_name, String date, String amount, String mobile) {
        this.name = name;
        this.image = image;
        this.institute_name = institute_name;
        this.date = date;
        this.amount = amount;
        this.mobile = mobile;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInstitute_name() {
        return institute_name;
    }

    public void setInstitute_name(String institute_name) {
        this.institute_name = institute_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
