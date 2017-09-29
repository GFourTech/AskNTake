package Pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 4/3/2017.
 */

public class FilterPojo implements Serializable{

    Double lat;
    Double lang;
    int range;
    boolean sortByPriceHtoL;
    boolean sortByPriceLtoH;
    boolean sortByTime;
    boolean sortByDistance;
    String productname;
    String priceFrom;
    String priceTo;
    String priceType;
    int timeRange;
    boolean invoice;
    boolean negotiable;
    boolean shipping;
    boolean markedAs;
    String distanceIn;
    ArrayList<String> categories;



    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLang() {
        return lang;
    }

    public void setLang(Double lang) {
        this.lang = lang;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public boolean isSortByPriceHtoL() {
        return sortByPriceHtoL;
    }

    public void setSortByPriceHtoL(boolean sortByPriceHtoL) {
        this.sortByPriceHtoL = sortByPriceHtoL;
    }

    public boolean isSortByPriceLtoH() {
        return sortByPriceLtoH;
    }

    public void setSortByPriceLtoH(boolean sortByPriceLtoH) {
        this.sortByPriceLtoH = sortByPriceLtoH;
    }

    public boolean isSortByTime() {
        return sortByTime;
    }

    public void setSortByTime(boolean sortByTime) {
        this.sortByTime = sortByTime;
    }

    public boolean isSortByDistance() {
        return sortByDistance;
    }

    public void setSortByDistance(boolean sortByDistance) {
        this.sortByDistance = sortByDistance;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(String priceFrom) {
        this.priceFrom = priceFrom;
    }

    public String getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(String priceTo) {
        this.priceTo = priceTo;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public int getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(int timeRange) {
        this.timeRange = timeRange;
    }

    public boolean isInvoice() {
        return invoice;
    }

    public void setInvoice(boolean invoice) {
        this.invoice = invoice;
    }

    public boolean isNegotiable() {
        return negotiable;
    }

    public void setNegotiable(boolean negotiable) {
        this.negotiable = negotiable;
    }

    public boolean isShipping() {
        return shipping;
    }

    public void setShipping(boolean shipping) {
        this.shipping = shipping;
    }

    public boolean isMarkedAs() {
        return markedAs;
    }

    public void setMarkedAs(boolean markedAs) {
        this.markedAs = markedAs;
    }

    public String getDistanceIn() {
        return distanceIn;
    }

    public void setDistanceIn(String distanceIn) {
        this.distanceIn = distanceIn;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }



}
