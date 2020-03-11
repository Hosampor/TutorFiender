package com.example.tutor.finder1.Helper;

/**
 * Created by Rana on 20-01-18.
 */

public class LocationViewholder {
    public String user_name;
    public String institute_name;
    public String uniquekey;
    public double latitude;
    public double longitude;


    public LocationViewholder() {
    }

    public LocationViewholder(String user_name, String institute_name, String uniquekey, double latitude, double longitude) {
        this.user_name = user_name;
        this.institute_name = institute_name;
        this.uniquekey = uniquekey;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getInstitute_name() {
        return institute_name;
    }

    public void setInstitute_name(String institute_name) {
        this.institute_name = institute_name;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
