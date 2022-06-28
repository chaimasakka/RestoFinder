package com.myapp.restofinder.models;

import java.util.Map;

public class Review {
    private String id;
    private String comment;
    private String name;
    private double rating;

    public Review(String id, String comment, String name, double rating) {
        this.id = id;
        this.comment = comment;
        this.name = name;
        this.rating = rating;
    }

    public Review() {
    }

    public Review(Map<String, String> map) {
        this.id = map.get("id");
        this.comment = map.get("comment");
        this.name = map.get("name");
        this.rating = Double.parseDouble(map.get("rating"));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
