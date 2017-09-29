package Pojo;

/**
 * Created by on 05/05/2017.
 */

public class RoomSharingServicePojo {

    String available_from_date;
    String accommadates;
    String no_bedrooms;
    String no_bathrooms;
    String gender;
    String price;
    String price_type;
    String id;

    public RoomSharingServicePojo() {

    }

    public RoomSharingServicePojo(String available_from_date, String accommadates, String no_bedrooms, String no_bathrooms, String gender, String price, String price_type, String id) {
        this.available_from_date = available_from_date;
        this.accommadates = accommadates;
        this.no_bedrooms = no_bedrooms;
        this.no_bathrooms = no_bathrooms;
        this.gender = gender;
        this.price = price;
        this.price_type = price_type;
        this.id = id;
    }


    public String getAvailable_from_date() {
        return available_from_date;
    }

    public void setAvailable_from_date(String available_from_date) {
        this.available_from_date = available_from_date;
    }

    public String getAccommadates() {
        return accommadates;
    }

    public void setAccommadates(String accommadates) {
        this.accommadates = accommadates;
    }

    public String getNo_bedrooms() {
        return no_bedrooms;
    }

    public void setNo_bedrooms(String no_bedrooms) {
        this.no_bedrooms = no_bedrooms;
    }

    public String getNo_bathrooms() {
        return no_bathrooms;
    }

    public void setNo_bathrooms(String no_bathrooms) {
        this.no_bathrooms = no_bathrooms;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_type() {
        return price_type;
    }

    public void setPrice_type(String price_type) {
        this.price_type = price_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
