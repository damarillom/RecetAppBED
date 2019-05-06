package com.dam.bed.recetapp_bed;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private String name;
    private String description;
    private String dietType;
    private ArrayList<String> ingredients;
    private int image;

    public Recipe() {
    }

    public Recipe(String name, String description, String dietType, ArrayList<String> ingredients, int image) {
        this.name = name;
        this.description = description;
        this.dietType = dietType;
        this.ingredients = new ArrayList<>(ingredients);
        this.image = image;
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

    public String getDietType() {
        return dietType;
    }

    public void setDietType(String dietType) {
        this.dietType = dietType;
    }

    public ArrayList<String> getIngredients() { return ingredients; }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public int getImage() { return image; }

    public void setImage(int image) { this.image = image; }
}
