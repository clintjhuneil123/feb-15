package com.example.imyasfinal;

import java.util.List;

public class Request {
    private String Location;
    private String People;
    private String Rates;
    private String currentdate;
    private String status;
    private String currenttime;

    public Request() {
    }

    public Request(String location, String people, String rates, String currentdate, String currenttime, String status) {
        Location = location;
        People = people;
        Rates = rates;
        this.currentdate = currentdate;
        this.status = status;
        this.currenttime = currenttime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPeople() {
        return People;
    }

    public void setPeople(String people) {
        People = people;
    }

    public String getRates() {
        return Rates;
    }

    public void setRates(String rates) {
        Rates = rates;
    }

    public String getCurrentdate() {
        return currentdate;
    }

    public void setCurrentdate(String currentdate) {
        this.currentdate = currentdate;
    }

//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }
}
