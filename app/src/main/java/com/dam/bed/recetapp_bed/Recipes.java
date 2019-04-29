package com.dam.bed.recetapp_bed;

public class Recipes {

    private String name;
    private String description;
    private String dietType;
    private Ingredients[] ingredients;

    public Recipes(String name, String description, String dietType, Ingredients[] ingredients) {
        this.name = name;
        this.description = description;
        this.dietType = dietType;
        this.ingredients = ingredients;
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

    public Ingredients[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredients[] ingredients) {
        this.ingredients = ingredients;
    }
}
