package Pojo;

/**
 * Created by admin on 4/18/2017.
 */

public class ItemServicePojo {

    String service_image;
    String service_name;
    String service_views_count;
    String service_favorites;
    String service_id;
    String service_owner_id;
    String service_location;
    String days_old;
    String provider_name;
    String rating;
    String service_distance;
    String service_distance_type;

    boolean service_owner_online_status;
    String reviewsCount;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getDays_old() {
        return days_old;
    }

    public void setDays_old(String days_old) {
        this.days_old = days_old;
    }


    public String getService_location() {
        return service_location;
    }

    public void setService_location(String service_location) {
        this.service_location = service_location;
    }


    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_image() {
        return service_image;
    }

    public void setService_image(String service_image) {
        this.service_image = service_image;
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

    public String getService_favorites() {
        return service_favorites;
    }

    public void setService_favorites(String service_favorites) {
        this.service_favorites = service_favorites;
    }

    public String getService_distance() {
        return service_distance;
    }

    public void setService_distance(String service_distance) {
        this.service_distance = service_distance;
    }

    public String getService_distance_type() {
        return service_distance_type;
    }

    public void setService_distance_type(String service_distance_type) {
        this.service_distance_type = service_distance_type;
    }

    public boolean isService_owner_online_status() {
        return service_owner_online_status;
    }

    public void setService_owner_online_status(boolean service_owner_online_status) {
        this.service_owner_online_status = service_owner_online_status;
    }

    public String getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(String reviewsCount) {
        this.reviewsCount = reviewsCount;
    }
}
