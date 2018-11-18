package ghostl.com.photofeed.entities;

public class Photo {

    private int id;

    private Boolean publishedByME;

    private String url;
    private String email;
    private double latitude;
    private double longitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getPublishedByME() {
        return publishedByME;
    }

    public void setPublishedByME(Boolean publishedByME) {
        this.publishedByME = publishedByME;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
