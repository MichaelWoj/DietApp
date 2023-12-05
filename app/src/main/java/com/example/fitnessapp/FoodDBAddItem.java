package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDBAddItem extends AppCompatActivity {

    private EditText foodName, foodCalories, foodFat, foodCarbs, foodProtein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_db_save_food);

        foodName = findViewById(R.id.itemAddMealName);
        foodCalories = findViewById(R.id.itemAddMealCalories);
        foodFat = findViewById(R.id.itemAddMealFat);
        foodCarbs = findViewById(R.id.itemAddMealCarbs);
        foodProtein = findViewById(R.id.itemAddMealProtein);

        Button submit = findViewById(R.id.itemAddFoodToDB);
        submit.setOnClickListener(view -> {
            if (foodName.getText().toString().isEmpty() || foodCalories.getText().toString().isEmpty() || foodFat.getText().toString().isEmpty() || foodCarbs.getText().toString().isEmpty() || foodProtein.getText().toString().isEmpty()) {
                Toast.makeText(FoodDBAddItem.this, "Please insert all info", Toast.LENGTH_SHORT).show();
            } else {

                Intent intent = new Intent(FoodDBAddItem.this, FoodDBDisplay.class);

                String foodNameToString = foodName.getText().toString();

                String foodCaloriesToString = foodCalories.getText().toString();
                float foodCaloriesVal = Float.parseFloat(foodCaloriesToString);

                String foodFatToString = foodFat.getText().toString();
                float foodFatVal = Float.parseFloat(foodFatToString);

                String foodCarbsToString = foodCarbs.getText().toString();
                float foodCarbsVal = Float.parseFloat(foodCarbsToString);

                String foodProteinToString = foodProtein.getText().toString();
                float foodProteinVal = Float.parseFloat(foodProteinToString);

                FoodModel foodModel;

                try {
                    foodModel = new FoodModel(-1, foodNameToString, foodCaloriesVal, foodFatVal, foodCarbsVal, foodProteinVal,0);
                } catch (Exception e) {
                    foodModel = new FoodModel(-1, "Error", 0f, 0f, 0f, 0f,0);
                }

                DatabaseHelper dataBaseHelper = new DatabaseHelper(FoodDBAddItem.this);
                dataBaseHelper.addOne(foodModel);


                setResult(RESULT_OK, intent);
                finish();
            }

        });
        Button back = findViewById(R.id.itemAddCancel);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(FoodDBAddItem.this, FoodDBDisplay.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            finish();
        });
    }
}
