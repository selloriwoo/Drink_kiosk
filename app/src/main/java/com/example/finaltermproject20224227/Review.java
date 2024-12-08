package com.example.finaltermproject20224227;

import java.io.Serializable;

public class Review implements Serializable {
    private int id;
    private String reviewText;
    private float rating;
    private int drinkItemId;
    private String drinkItemName;
    private String drinkItemPic;

    public Review(int id, String reviewText, float rating, int drinkItemId, String drinkItemName, String drinkItemPic) {
        this.id = id;
        this.reviewText = reviewText;
        this.rating = rating;
        this.drinkItemId = drinkItemId;
        this.drinkItemName = drinkItemName;
        this.drinkItemPic = drinkItemPic;
    }

    public int getId() {
        return id;
    }

    public String getReviewText() {
        return reviewText;
    }

    public float getRating() {
        return rating;
    }

    public int getDrinkItemId() {
        return drinkItemId;
    }

    public String getDrinkItemName() {
        return drinkItemName;
    }

    public String getDrinkItemPic() {
        return drinkItemPic;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", reviewText='" + reviewText + '\'' +
                ", rating=" + rating +
                ", drinkItemId=" + drinkItemId +
                ", drinkItemName='" + drinkItemName + '\'' +
                ", drinkItemPic='" + drinkItemPic + '\'' +
                '}';
    }
}