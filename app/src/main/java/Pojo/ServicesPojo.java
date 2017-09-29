package Pojo;

/**
 * Created by G4 on 3/28/2017.
 */

public class ServicesPojo {
    private String service_name;
    private String provider_name;
    private String location;
    private String views;
    private String favorities;
    private String days_old;
    private String miles;

    public ServicesPojo(String service_name, String provider_name, String location, String views, String favorities, String days_old, String miles)
    {
        this.service_name = service_name;
        this.provider_name = provider_name;
        this.location = location;
        this.views = views;
        this.favorities = favorities;
        this.days_old = days_old;
        this.miles = miles;
    }


    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getFavorities() {
        return favorities;
    }

    public void setFavorities(String favorities) {
        this.favorities = favorities;
    }

    public String getDays_old() {
        return days_old;
    }

    public void setDays_old(String days_old) {
        this.days_old = days_old;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }


}
