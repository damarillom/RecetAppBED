package com.dam.bed.recetapp_bed;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Recipe {

    private String name;
    private String description;
    private String type;
    private ArrayList<String> ingredients;
    private String img;

    public Recipe() {
    }

    public Recipe(String name, String description, String dietType, ArrayList<String> ingredients, String image) {
        this.name = name;
        this.description = description;
        this.type = dietType;
        this.ingredients = new ArrayList<>(ingredients);
        this.img = image;
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

    public String getType() {
        return type;
    }

    public void setType(String dietType) {
        this.type = dietType;
    }

    public ArrayList<String> getIngredients() { return ingredients; }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String image) {
        this.img = image;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dietType='" + type + '\'' +
                ", ingredients=" + ingredients +
                ", image=" + img +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        return name != null ? name.equals(recipe.name) : recipe.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
