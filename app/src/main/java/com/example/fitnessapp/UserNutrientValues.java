    package com.example.fitnessapp;

import android.app.Application;

public class UserNutrientValues extends Application {

    public static final String SET_VALUES = "setValues";

    public static double caloriesValSettings = 0;
    public static double proteinValSettings = 0;
    public static double fatValSettings = 0;
    public static double carbsValSettings = 0;

    public static double caloriesTargetVal = 0;
    public static double proteinTargetVal = 0;
    public static double fatTargetVal = 0;
    public static double carbsTargetVal = 0;


    public void getNutrientVals(double calories, double protein, double fat, double carbs){
        caloriesValSettings = calories;
        proteinValSettings = protein;
        fatValSettings = fat;
        carbsValSettings = carbs;
    }
    public Double returnCaloriesValSettings(){
        return caloriesValSettings;
    }

    public Double returnProteinValSettings(){
        return proteinValSettings;
    }

    public Double returnFatValSettings(){
        return fatValSettings;
    }

    public Double returnCarbsValSettings(){
        return carbsValSettings;
    }

}
