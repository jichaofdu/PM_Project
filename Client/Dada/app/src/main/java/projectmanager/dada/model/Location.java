package projectmanager.dada.model;

public class Location {

    private int    locationId;
    private double longitude;
    private double latitude;
    private String description;

    public Location(int locationId, double longitude, double latitude, String description) {
        this.locationId = locationId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
    }

    public Location() {}


    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
