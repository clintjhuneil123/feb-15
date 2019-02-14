package com.example.imyasfinal;

import java.util.List;

public class Request {
    private String Location;
    private String People;
//    private String Rates;
    private String artistId;
    private String clientId;
    private String requestId;
    private String currentdate;
    private String status;
    private String currenttime;

    public Request() {
    }
//    String rates,
    public Request(String location, String people,  String currentdate, String currenttime, String status, String clientId, String artistId, String requestId) {
        Location = location;
        People = people;
//        Rates = rates;
        this.currentdate = currentdate;
        this.status = status;
        this.currenttime = currenttime;
        this.artistId = artistId;
        this.clientId = clientId;
        this.requestId = requestId;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

//    public String getRates() {
//        return Rates;
//    }
//
//    public void setRates(String rates) {
//        Rates = rates;
//    }

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
