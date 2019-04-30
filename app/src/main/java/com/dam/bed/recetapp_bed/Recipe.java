package com.dam.bed.recetapp_bed;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private String name;
    private String description;
    private String dietType;
    private ArrayList<Ingredient> ingredients;

    public Recipe(String name, String description, String dietType, ArrayList<Ingredient> ingredients) {
        this.name = name;
        this.description = description;
        this.dietType = dietType;
        this.ingredients = new ArrayList<>(ingredients);
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

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
