package tansoft.travel_tours.domain;

public class Resort {


    private String name;
    private  String serviceType;

    private Double longitude;
    private  Double latitude;

    private String city;
    private  String contact;

    private String amount;
    private  int image;

    public Resort() {
    }

    public Resort(String name, String contact, String amount, int image, Double latitude, Double longitude) {
        this.name = name;
        this.contact = contact;
        this.amount = amount;
        this.image = image;
        this.latitude=latitude;
        this.longitude=longitude;
    }


    public Resort(String name, String serviceType, Double longitude, Double latitude, String city, String contact, String amount, int image) {
        this.name = name;
        this.serviceType = serviceType;
        this.longitude = longitude;
        this.latitude = latitude;
        this.city = city;
        this.contact = contact;
        this.amount = amount;
        this.image = image;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
