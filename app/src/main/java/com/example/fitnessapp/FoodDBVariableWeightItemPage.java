    package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDBVariableWeightItemPage extends AppCompatActivity {

    private EditText variableFoodName, variableFoodWeight, variableFoodCalories, variableFoodFat, variableFoodCarbs, variableFoodProtein;
    private Float variableFoodWeightVal, variableFoodCaloriesVal, variableFoodFatVal, variableFoodCarbsVal, variableFoodProteinVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_variable_weight_item_page);

        variableFoodName = findViewById(R.id.variableItemAddMealName);
        variableFoodWeight = findViewById(R.id.variableItemWeight);
        variableFoodCalories = findViewById(R.id.variableItemAddMealCalories);
        variableFoodFat = findViewById(R.id.variableItemAddMealFat);
        variableFoodCarbs = findViewById(R.id.variableItemAddMealCarbs);
        variableFoodProtein = findViewById(R.id.variableItemAddMealProtein);

        Button submit = findViewById(R.id.variableItemAddFoodToDB);
        submit.setOnClickListener(view -> {
            if (variableFoodName.getText().toString().isEmpty() || variableFoodCalories.getText().toString().isEmpty() || variableFoodFat.getText().toString().isEmpty() || variableFoodCarbs.getText().toString().isEmpty() || variableFoodProtein.getText().toString().isEmpty()) {
                Toast.makeText(FoodDBVariableWeightItemPage.this, "Please insert all info", Toast.LENGTH_SHORT).show();
            } else {

                Intent intent = new Intent(FoodDBVariableWeightItemPage.this, FoodDBDisplay.class);

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

                oneGramNutrient(variableFoodWeightVal, variableFoodCaloriesVal, variableFoodFatVal, variableFoodCarbsVal, variableFoodProteinVal);

                FoodModel foodModel;

                try {
                    foodModel = new FoodModel(-1, variableFoodNameToString, variableFoodCaloriesVal, variableFoodFatVal, variableFoodCarbsVal, variableFoodProteinVal,1);
                } catch (Exception e) {
                    foodModel = new FoodModel(-1, "Error", 0f, 0f, 0f, 0f,1);
                }

                DatabaseHelper dataBaseHelper = new DatabaseHelper(FoodDBVariableWeightItemPage.this);
                dataBaseHelper.addOne(foodModel);

                setResult(RESULT_OK, intent);
                finish();
            }

        });
        Button back = findViewById(R.id.variableItemAddCancel);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(FoodDBVariableWeightItemPage.this, FoodDBDisplay.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            finish();
        });
    }

    public void oneGramNutrient(float weight, float calories, float fat, float carbs, float protein) {
        float conversionFactor;
        if (weight < 100) {
            conversionFactor = 100 / weight;
        } else {
            conversionFactor = weight / 100;
        }
        conversionFactor = (float) (Math.round(conversionFactor * 100.0) / 100.0);

        variableFoodCaloriesVal = calories / conversionFactor;
        variableFoodCaloriesVal = variableFoodCaloriesVal / 100;
        variableFoodCaloriesVal = (float) (Math.round(variableFoodCaloriesVal * 100.0) / 100.0);

        variableFoodFatVal = fat / conversionFactor;
        variableFoodCaloriesVal = variableFoodCaloriesVal / 100;
        variableFoodFatVal = (float) (Math.round(variableFoodFatVal * 100.0) / 100.0);

        variableFoodCarbsVal = carbs / conversionFactor;
        variableFoodCaloriesVal = variableFoodCaloriesVal / 100;
        variableFoodCarbsVal = (float) (Math.round(variableFoodCarbsVal * 100.0) / 100.0);

        variableFoodProteinVal = protein / conversionFactor;
        variableFoodCaloriesVal = variableFoodCaloriesVal / 100;
        variableFoodProteinVal = (float) (Math.round(variableFoodProteinVal * 100.0) / 100.0);
    }

}
