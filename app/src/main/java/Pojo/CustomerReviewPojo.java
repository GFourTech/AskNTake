package Pojo;

import java.io.Serializable;

/**
 * Created by on 27/05/2017.
 */

public class CustomerReviewPojo implements Serializable {

    double review;
    String name;
    String Comment;
    String date;


    private static final long serialVersionUID = 1L;

    public CustomerReviewPojo() {

    }

    public CustomerReviewPojo(double review, String name, String comment, String date) {
        this.review = review;
        this.name = name;
        Comment = comment;
        this.date = date;
    }


    public double getReview() {
        return review;
    }

    public void setReview(double review) {
        this.review = review;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
