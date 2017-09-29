package Pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class ServicesFiteringDataPojo implements Serializable {

    private static final long serialVersionUID = 1L;
    String service_name;
    ArrayList<String> categoriesList;
    ArrayList<String> subCategoriesList;
    ArrayList<String> subcategoriesPositions;
    String KilomitersOrMiles;
    int range;
    String listed_with_in;
    String category;
    String rating;


    public ServicesFiteringDataPojo() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ServicesFiteringDataPojo(String service_name,
                                    ArrayList<String> categoriesList,
                                    ArrayList<String> subCategoriesList,
                                    String KilomitersOrMiles,
                                    int range,
                                    String listed_with_in, ArrayList<String> subcategoriesPositions,
                                    String rating
    ) {
        super();
        this.categoriesList = categoriesList;
        this.subCategoriesList = subCategoriesList;

        this.range = range;
        this.KilomitersOrMiles = KilomitersOrMiles;
        this.listed_with_in = listed_with_in;
        this.service_name = service_name;
        this.subcategoriesPositions = subcategoriesPositions;
        this.rating=rating;

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public ArrayList<String> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(ArrayList<String> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public ArrayList<String> getSubCategoriesList() {
        return subCategoriesList;
    }

    public void setSubCategoriesList(ArrayList<String> subCategoriesList) {
        this.subCategoriesList = subCategoriesList;
    }

    public String getKilomitersOrMiles() {
        return KilomitersOrMiles;
    }

    public void setKilomitersOrMiles(String kilomitersOrMiles) {
        KilomitersOrMiles = kilomitersOrMiles;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getListed_with_in() {
        return listed_with_in;
    }

    public void setListed_with_in(String listed_with_in) {
        this.listed_with_in = listed_with_in;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getSubcategoriesPositions() {
        return subcategoriesPositions;
    }

    public void setSubcategoriesPositions(ArrayList<String> subcategoriesPositions) {
        this.subcategoriesPositions = subcategoriesPositions;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}

