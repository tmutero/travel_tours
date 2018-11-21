package tansoft.travel_tours.domain;

import android.support.annotation.NonNull;

import java.util.Date;

public class Resort  {

    private String id;
    private String name;
    private String serviceType;
    private Double longitude;
    private Double latitude;
    private String city;
    private String contact;
    private String amount;
    private String imageString;
    private String distance;

    private String dateCreated;

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Resort() {
    }

    public Resort(String id, String name, String serviceType, String dateCreated) {
        this.id = id;
        this.name = name;
        this.serviceType = serviceType;
        this.dateCreated = dateCreated;
    }

    public Resort(String name, String contact, String serviceType, String id, Double latitude, Double longitude, String imageString, String distance) {
        this.name = name;
        this.contact = contact;
        this.serviceType = serviceType;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageString = imageString;
        this.distance = distance;
    }

    public Resort(String name, String contact, String serviceType, String id, Double latitude, Double longitude, String imageString, String distance, String amount) {
        this.name = name;
        this.contact = contact;
        this.serviceType = serviceType;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageString = imageString;
        this.distance = distance;
        this.amount=amount;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }





}
