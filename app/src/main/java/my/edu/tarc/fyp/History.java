package my.edu.tarc.fyp;

import java.time.LocalDateTime;

public class History { //Transaction

    private String transID, transMethod, comments, promoCode, transAppID;
    private double rating, transAmount;
    private LocalDateTime transDateTime;

    public History() {
    }

    public History(String transID, double transAmount, LocalDateTime transDateTime) {
        this.transID = transID;
        this.transAmount = transAmount;
        this.transDateTime = transDateTime;
    }

    public History(String transID, String transMethod, String comments, String promoCode, String transAppID, double rating, double transAmount, LocalDateTime transDateTime) {
        this.transID = transID;
        this.transMethod = transMethod;
        this.comments = comments;
        this.promoCode = promoCode;
        this.transAppID = transAppID;
        this.rating = rating;
        this.transAmount = transAmount;
        this.transDateTime = transDateTime;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public String getTransMethod() {
        return transMethod;
    }

    public void setTransMethod(String transMethod) {
        this.transMethod = transMethod;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getTransAppID() {
        return transAppID;
    }

    public void setTransAppID(String transAppID) {
        this.transAppID = transAppID;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(double transAmount) {
        this.transAmount = transAmount;
    }

    public LocalDateTime getTransDateTime() {
        return transDateTime;
    }

    public void setTransDateTime(LocalDateTime transDateTime) {
        this.transDateTime = transDateTime;
    }
}
