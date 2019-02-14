package com.example.imyasfinal;

public class ArtistPorfolio {
    private String Name;
    private String Image;
    private String Location;
    private String CurrentTime;
    private String CurrentDate;
    private String Description;
    private String Price;
    private String ArtistID;

    public ArtistPorfolio() {
    }

    public ArtistPorfolio(String name, String image, String description, String price, String artistID, String location, String currentDate, String currentTime) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        ArtistID = artistID;
        Location = location;
        CurrentDate = currentDate;
        CurrentTime = currentTime;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(String currentTime) {
        CurrentTime = currentTime;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getArtistID() {
        return ArtistID;
    }

    public void setArtistID(String artistID) {
        ArtistID = artistID;
    }
}
