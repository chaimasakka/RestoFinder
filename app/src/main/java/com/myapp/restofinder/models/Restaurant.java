package com.myapp.restofinder.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Restaurant implements Serializable {

    private String id;
    private String name;
    private String description;
    private String phone;
    private List<Recipe> recipes = new ArrayList<>();

    public Restaurant() {
    }

    public Restaurant(String id, String name, String description, String phone, List<Recipe> recipes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.recipes = recipes;
    }

    public Restaurant(Map<String, Object> map) {
        this.id = map.get("id").toString();
        this.name = map.get("name").toString();
        this.description = map.get("description").toString();
        this.phone = map.get("phone").toString();
        if (map.containsKey("recipes"))
            recipes.addAll((List<Recipe>) map.get("recipes"));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
