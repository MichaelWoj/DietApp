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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDBVariableWeightItemPage extends AppCompatActivity {

    private EditText variableFoodName, variableFoodWeight, variableFoodCalories, variableFoodFat, variableFoodCarbs, variableFoodProtein;
    private Float variableFoodWeightVal, variableFoodCaloriesVal, variableFoodFatVal, variableFoodCarbsVal, variableFoodProteinVal;
    private int selectedDisplayWeight;

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

        Button selectDisplayWeight = findViewById(R.id.variableItemSelectDisplayWeight);
        selectDisplayWeight.setOnClickListener(view -> {
            selectDisplayWeightWindow();
        });

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
    private void selectDisplayWeightWindow() {

        final Dialog displayWeightDialog = new Dialog(this);
        displayWeightDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        displayWeightDialog.setContentView(R.layout.activity_db_variable_weight_popup);

        LinearLayout confirmDisplayWeight = displayWeightDialog.findViewById(R.id.layoutConfirmDisplayWeight);
        LinearLayout cancelDisplayWeight = displayWeightDialog.findViewById(R.id.layoutCancelDisplayWeight);

        confirmDisplayWeight.setOnClickListener(v -> {
            displayWeightDialog.dismiss();
        });
        cancelDisplayWeight.setOnClickListener(v -> {
            selectedDisplayWeight = 0;
            displayWeightDialog.dismiss();
        });

        displayWeightDialog.show();
        displayWeightDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        displayWeightDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        displayWeightDialog.getWindow().setGravity(Gravity.CENTER);
    }

    public void checkRadioButtonId(View view){
        switch (view.getId()) {
            case R.id.displayWeight100g:
                selectedDisplayWeight = 100;
                break;
            case R.id.displayWeight50g:
                selectedDisplayWeight = 50;
                break;
            case R.id.displayWeight10g:
                selectedDisplayWeight = 10;
                break;
            case R.id.displayWeight1g:
                selectedDisplayWeight = 1;
                break;
        }
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
        // Need to make the arithematic symbol in the line below into a var to avoid code repetition
        variableFoodCaloriesVal = variableFoodCaloriesVal / 100;
        variableFoodCaloriesVal = (float) (Math.round(variableFoodCaloriesVal * 100.0) / 100.0);

        variableFoodFatVal = fat / conversionFactor;
        variableFoodFatVal = variableFoodFatVal / 100;
        variableFoodFatVal = (float) (Math.round(variableFoodFatVal * 100.0) / 100.0);

        variableFoodCarbsVal = carbs / conversionFactor;
        variableFoodCarbsVal = variableFoodCarbsVal / 100;
        variableFoodCarbsVal = (float) (Math.round(variableFoodCarbsVal * 100.0) / 100.0);

        variableFoodProteinVal = protein / conversionFactor;
        variableFoodProteinVal = variableFoodProteinVal / 100;
        variableFoodProteinVal = (float) (Math.round(variableFoodProteinVal * 100.0) / 100.0);
    }

}
