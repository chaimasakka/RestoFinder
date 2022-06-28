package com.myapp.restofinder.models;

import java.io.Serializable;
import java.util.Map;

public class Recipe implements Serializable {
    private String id;
    private String name;
    private double price;
    private String description;

    public Recipe(String name, double price, String description, String id) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.id = id;
    }

    public Recipe(Map<String, String> map) {
        this.name = map.get("name");
        this.price = Double.parseDouble(map.get("price"));
        this.description = map.get("description");
        this.id = map.get("id");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
