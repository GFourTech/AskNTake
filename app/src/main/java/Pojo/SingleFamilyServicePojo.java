package Pojo;

/**
 * Created by on 05/05/2017.
 */

public class SingleFamilyServicePojo {

    String available_from_date;
    String no_bedrooms;
    String no_bathrooms;
    String totalsq_feet;
    String price;
    String price_type;
    String id;

    public SingleFamilyServicePojo() {

    }

    public SingleFamilyServicePojo(String available_from_date, String no_bedrooms, String no_bathrooms, String totalsq_feet, String price, String price_type, String id) {
        this.available_from_date = available_from_date;
        this.no_bedrooms = no_bedrooms;
        this.no_bathrooms = no_bathrooms;
        this.totalsq_feet = totalsq_feet;
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

    public String getTotalsq_feet() {
        return totalsq_feet;
    }

    public void setTotalsq_feet(String totalsq_feet) {
        this.totalsq_feet = totalsq_feet;
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
