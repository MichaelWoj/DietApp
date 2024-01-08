package com.example.fitnessapp;

public class FoodModel {
    private int id;
    private String name;
    private Float calories;
    private Float fat;
    private Float carbs;
    private Float protein;
    private int displayWeight;
    private int saveType;

    public FoodModel(int id, String name, Float calories, Float fat, Float carbs, Float protein, int displayWeight, int saveType) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.fat = fat;
        this.carbs = carbs;
        this.protein = protein;
        this.displayWeight = displayWeight;
        this.saveType = saveType;
    }
    @Override
    public String toString() {
        return "FoodModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", calories=" + calories +
                ", fat=" + fat +
                ", carbs=" + carbs +
                ", protein=" + protein +
                ", saveType="+saveType+
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getCalories() {
        return calories;
    }

    public void setCalories(Float calories) {
        this.calories = calories;
    }

    public Float getFat() {
        return fat;
    }

    public void setFat(Float fat) {
        this.fat = fat;
    }

    public Float getCarbs() {
        return carbs;
    }

    public void setCarbs(Float carbs) {
        this.carbs = carbs;
    }

    public Float getProtein() {
        return protein;
    }

    public void setProtein(Float protein) {
        this.protein = protein;
    }

    public int getDisplayWeight() {return displayWeight;}

    public void setDisplayWeight(int displayWeight){this.displayWeight = displayWeight;}

    public int getSaveType(){
        return saveType;
    }

    public void setSaveType(int saveType){
        this.saveType = saveType;
    }

}
