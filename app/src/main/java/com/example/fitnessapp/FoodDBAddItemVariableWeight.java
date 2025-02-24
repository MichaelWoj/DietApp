    package com.example.fitnessapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDBAddItemVariableWeight extends AppCompatActivity {

    private EditText variableFoodName, variableFoodWeight, variableFoodCalories, variableFoodFat, variableFoodCarbs, variableFoodProtein, variableDisplayWeight;
    private Float variableFoodWeightVal, variableFoodCaloriesVal, variableFoodFatVal, variableFoodCarbsVal, variableFoodProteinVal;
    private int selectedDisplayWeight;
    private FoodDBVariableWeightPopup foodDBVariableWeightPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_add_item_variable_weight);
        foodDBVariableWeightPopup = new FoodDBVariableWeightPopup();


        variableFoodName = findViewById(R.id.variableItemAddMealName);
        variableFoodWeight = findViewById(R.id.variableItemWeight);
        variableFoodCalories = findViewById(R.id.variableItemAddMealCalories);
        variableFoodFat = findViewById(R.id.variableItemAddMealFat);
        variableFoodCarbs = findViewById(R.id.variableItemAddMealCarbs);
        variableFoodProtein = findViewById(R.id.variableItemAddMealProtein);

        Button selectDisplayWeight = findViewById(R.id.variableItemSelectDisplayWeight);
        selectDisplayWeight.setOnClickListener(view -> {
            foodDBVariableWeightPopup.selectDisplayWeightWindow(this, selectedDisplayWeight);
        });

        Button submit = findViewById(R.id.variableItemAddFoodToDB);
        submit.setOnClickListener(view -> {
            selectedDisplayWeight = foodDBVariableWeightPopup.loadSavedDisplayWeight(this);
            foodDBVariableWeightPopup.resetSelectedDisplayWeight(this);

            if (variableFoodName.getText().toString().isEmpty() || variableFoodCalories.getText().toString().isEmpty() || variableFoodFat.getText().toString().isEmpty() || variableFoodCarbs.getText().toString().isEmpty() || variableFoodProtein.getText().toString().isEmpty() || selectedDisplayWeight == 0) {
                Toast.makeText(FoodDBAddItemVariableWeight.this, "Please insert all info", Toast.LENGTH_SHORT).show();
            } else {

                Intent intent = new Intent(FoodDBAddItemVariableWeight.this, FoodDBDisplay.class);

                String variableFoodNameToString = variableFoodName.getText().toString();

                String variableFoodWeightToString = variableFoodWeight.getText().toString();
                variableFoodWeightVal = Float.parseFloat(variableFoodWeightToString);

                String variableFoodCaloriesToString = variableFoodCalories.getText().toString();
                variableFoodCaloriesVal = Float.parseFloat(variableFoodCaloriesToString);

                String variableFoodFatToString = variableFoodFat.getText().toString();
                variableFoodFatVal = Float.parseFloat(variableFoodFatToString);

                String variableFoodCarbsToString = variableFoodCarbs.getText().toString();
                variableFoodCarbsVal = Float.parseFloat(variableFoodCarbsToString);

                String variableFoodProteinToString = variableFoodProtein.getText().toString();
                variableFoodProteinVal = Float.parseFloat(variableFoodProteinToString);

                if(selectedDisplayWeight == 0){
                    selectedDisplayWeight = Math.round(variableFoodWeightVal);
                }

                float [] variableResultArray = calculateNutrientsToTargetWeightVal(variableFoodWeightVal, variableFoodCaloriesVal, variableFoodFatVal, variableFoodCarbsVal, variableFoodProteinVal, selectedDisplayWeight);

                variableFoodCaloriesVal = variableResultArray[0];
                variableFoodFatVal = variableResultArray[1];
                variableFoodCarbsVal = variableResultArray[2];
                variableFoodProteinVal = variableResultArray[3];

                FoodModel foodModel;

                try {
                    foodModel = new FoodModel(-1, variableFoodNameToString, variableFoodCaloriesVal, variableFoodFatVal, variableFoodCarbsVal, variableFoodProteinVal, selectedDisplayWeight, 1);
                } catch (Exception e) {
                    foodModel = new FoodModel(-1, "Error", 0f, 0f, 0f, 0f,0, 1);
                }

                DatabaseHelper dataBaseHelper = new DatabaseHelper(FoodDBAddItemVariableWeight.this);
                dataBaseHelper.addOne(foodModel);

                setResult(RESULT_OK, intent);
                finish();
            }

        });
        Button back = findViewById(R.id.variableItemAddCancel);
        back.setOnClickListener(v -> {
            foodDBVariableWeightPopup.resetSelectedDisplayWeight(this);
            Intent intent = new Intent(FoodDBAddItemVariableWeight.this, FoodDBDisplay.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            finish();
        });
    }

    public void checkRadioButtonId(View view){
        foodDBVariableWeightPopup.actualCheckRadioButtonId(view);
    }

    public float[] calculateNutrientsToTargetWeightVal(float foodWeight, float calories, float fat, float carbs, float protein, float targetWeight) {
        float conversionFactor;
        if (foodWeight < 100) {
            conversionFactor = 100 / foodWeight;
        } else {
            conversionFactor = foodWeight / 100;
        }
        conversionFactor = (float) (Math.round(conversionFactor * 100.0) / 100.0);

        float calculateVariableFoodCaloriesVal = oneGramOfSpecificNutrient(conversionFactor, calories);
        calculateVariableFoodCaloriesVal = calculateVariableFoodCaloriesVal * targetWeight;
        calculateVariableFoodCaloriesVal = (float) (Math.round(calculateVariableFoodCaloriesVal * 100.0) / 100.0);

        float calculateVariableFoodFatVal = oneGramOfSpecificNutrient(conversionFactor, fat);
        calculateVariableFoodFatVal = calculateVariableFoodFatVal * targetWeight;
        calculateVariableFoodFatVal = (float) (Math.round(calculateVariableFoodFatVal * 100.0) / 100.0);

        float calculateVariableFoodCarbsVal = oneGramOfSpecificNutrient(conversionFactor, carbs);
        calculateVariableFoodCarbsVal = calculateVariableFoodCarbsVal * targetWeight;
        calculateVariableFoodCarbsVal = (float) (Math.round(calculateVariableFoodCarbsVal * 100.0) / 100.0);

        float calculateVariableFoodProteinVal = oneGramOfSpecificNutrient(conversionFactor, protein);
        calculateVariableFoodProteinVal = calculateVariableFoodProteinVal * targetWeight;
        calculateVariableFoodProteinVal = (float) (Math.round(calculateVariableFoodProteinVal * 100.0) / 100.0);

        return new float[] {calculateVariableFoodCaloriesVal, calculateVariableFoodFatVal, calculateVariableFoodCarbsVal, calculateVariableFoodProteinVal};
    }
    private float oneGramOfSpecificNutrient(float factor, float nutrient){
        float tempVariableFoodNutrientVal = 0;

        if(factor > 1) {
            tempVariableFoodNutrientVal = nutrient * factor;
        }
        else if (factor < 1) {
            tempVariableFoodNutrientVal = nutrient / factor;
        }
        else{
            tempVariableFoodNutrientVal = nutrient;
        }
        tempVariableFoodNutrientVal = tempVariableFoodNutrientVal / 100;
        tempVariableFoodNutrientVal = (float) (Math.round(tempVariableFoodNutrientVal * 1000.0) / 1000.0);

        return tempVariableFoodNutrientVal;
    }

}
