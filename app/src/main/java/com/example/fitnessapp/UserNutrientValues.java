    package com.example.fitnessapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

    public class UserNutrientValues extends AppCompatActivity {

    public static final String SET_VALUES = "setValues";

    public static final String caloriesValSettings = "0";
    public static final String proteinValSettings = "0";
    public static final String fatValSettings = "0";
    public static final String carbsValSettings = "0";

    public static final String caloriesTargetVal = "0";
    public static final String proteinTargetVal = "0";
    public static final String fatTargetVal = "0";
    public static final String carbsTargetVal = "0";


    public void getNutrientVals(float calories, float protein, float fat, float carbs){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putFloat(caloriesValSettings, calories);
        editor.putFloat(proteinValSettings, protein);
        editor.putFloat(fatValSettings, fat);
        editor.putFloat(carbsValSettings, carbs);

        editor.apply();
    }
    public String returnCaloriesValSettings(){
        return caloriesValSettings;
    }

    public String returnProteinValSettings(){
        return proteinValSettings;
    }

    public String returnFatValSettings(){
        return fatValSettings;
    }

    public String returnCarbsValSettings(){
        return carbsValSettings;
    }

}
