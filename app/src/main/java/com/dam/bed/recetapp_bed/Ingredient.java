package com.dam.bed.recetapp_bed;

public class Ingredient {

    private String name;
    private int kcal;

    public Ingredient(String name, int kcal) {
        this.name = name;
        this.kcal = kcal;
    }

    public Ingredient() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", kcal=" + kcal +
                '}';
    }
}
