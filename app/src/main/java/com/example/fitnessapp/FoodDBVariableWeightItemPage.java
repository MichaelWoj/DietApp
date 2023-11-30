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
        variableFoodCalories = findViewById(R.id.itemAddMealCalories);
        variableFoodFat = findViewById(R.id.itemAddMealFat);
        variableFoodCarbs = findViewById(R.id.itemAddMealCarbs);
        variableFoodProtein = findViewById(R.id.itemAddMealProtein);

        Button submit = findViewById(R.id.itemAddFoodToDB);
        submit.setOnClickListener(view -> {
            if (variableFoodName.getText().toString().isEmpty() || variableFoodCalories.getText().toString().isEmpty() || variableFoodFat.getText().toString().isEmpty() || variableFoodCarbs.getText().toString().isEmpty() || variableFoodProtein.getText().toString().isEmpty()) {
                Toast.makeText(FoodDBVariableWeightItemPage.this, "Please insert all info", Toast.LENGTH_SHORT).show();
            } else {

                Intent intent = new Intent(FoodDBVariableWeightItemPage.this, FoodDBDisplay.class);

                String variableFoodNameToString = variableFoodName.getText().toString();

                String variableFoodWeightToString = variableFoodCalories.getText().toString();
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
                    foodModel = new FoodModel(-1, variableFoodNameToString, variableFoodCaloriesVal, variableFoodFatVal, variableFoodCarbsVal, variableFoodProteinVal,true);
                } catch (Exception e) {
                    foodModel = new FoodModel(-1, "Error", 0f, 0f, 0f, 0f,true);
                }

                DatabaseHelper dataBaseHelper = new DatabaseHelper(FoodDBVariableWeightItemPage.this);
                dataBaseHelper.addOne(foodModel);

                setResult(RESULT_OK, intent);
                finish();
            }

        });
        Button back = findViewById(R.id.itemAddCancel);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(FoodDBVariableWeightItemPage.this, FoodDBDisplay.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            finish();
        });
    }

    public void oneGramNutrient(float weight, float calories, float fat, float carbs, float protein) {
        double conversionFactor;
        if (weight < 100) {
            conversionFactor = 100 / weight;
        } else {
            conversionFactor = weight / 100;
        }
        conversionFactor = conversionFactor / 100;

        variableFoodCaloriesVal = (float) (calories * conversionFactor);
        variableFoodFatVal = (float) (fat * conversionFactor);
        variableFoodCarbsVal = (float) (carbs * conversionFactor);
        variableFoodProteinVal = (float) (protein * conversionFactor);
    }

}
