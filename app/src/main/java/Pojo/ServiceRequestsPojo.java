package Pojo;

/**
 * Created by admin on 4/18/2017.
 */

public class ServiceRequestsPojo {

    String service_image;
    String service_name;
    String service_views_count;
    String service_id;
    String service_owner_id;
    String service_location;
    String days_old;
    String requester_name;
    String service_distance;
    boolean service_owner_online_status;
    String category;
    String subcategory;

    public ServiceRequestsPojo() {
    }


    public ServiceRequestsPojo(String service_image, String service_name, String service_views_count, String service_id, String service_owner_id, String service_location, String days_old, String requester_name, String service_distance, boolean service_owner_online_status, String category, String subcategory) {
        this.service_image = service_image;
        this.service_name = service_name;
        this.service_views_count = service_views_count;
        this.service_id = service_id;
        this.service_owner_id = service_owner_id;
        this.service_location = service_location;
        this.days_old = days_old;
        this.requester_name = requester_name;
        this.service_distance = service_distance;
        this.service_owner_online_status = service_owner_online_status;
        this.category = category;
        this.subcategory = subcategory;
    }

    public String getService_image() {
        return service_image;
    }

    public void setService_image(String service_image) {
        this.service_image = service_image;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_views_count() {
        return service_views_count;
    }

    public void setService_views_count(String service_views_count) {
        this.service_views_count = service_views_count;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_owner_id() {
        return service_owner_id;
    }

    public void setService_owner_id(String service_owner_id) {
        this.service_owner_id = service_owner_id;
    }

    public String getService_location() {
        return service_location;
    }

    public void setService_location(String service_location) {
        this.service_location = service_location;
    }

    public String getDays_old() {
        return days_old;
    }

    public void setDays_old(String days_old) {
        this.days_old = days_old;
    }

    public String getRequester_name() {
        return requester_name;
    }

    public void setRequester_name(String requester_name) {
        this.requester_name = requester_name;
    }

    public String getService_distance() {
        return service_distance;
    }

    public void setService_distance(String service_distance) {
        this.service_distance = service_distance;
    }

    public boolean isService_owner_online_status() {
        return service_owner_online_status;
    }

    public void setService_owner_online_status(boolean service_owner_online_status) {
        this.service_owner_online_status = service_owner_online_status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
}
