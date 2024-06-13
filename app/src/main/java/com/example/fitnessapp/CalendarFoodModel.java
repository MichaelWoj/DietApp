package com.example.fitnessapp;

public class CalendarFoodModel {
    private int id;
    private String name, date, time;
    private Float calories, fat, carbs, protein, weight;

    public CalendarFoodModel(int id, String name, Float calories, Float fat, Float carbs, Float protein, Float weight, String date, String time) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.fat = fat;
        this.carbs = carbs;
        this.protein = protein;
        this.weight = weight;
        this.date = date;
        this.time = time;
    }
    @Override
    public String toString() {
        return "CalendarFoodModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", calories=" + calories +
                ", fat=" + fat +
                ", carbs=" + carbs +
                ", protein=" + protein +
                ", date="+date+
                ", time="+time+
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

    public Float getWeight() {return weight;}

    public void setWeight(Float weight){this.weight = weight;}

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

}
