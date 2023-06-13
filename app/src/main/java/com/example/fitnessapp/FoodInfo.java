    package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

    public class  FoodInfo extends AppCompatActivity {
        Button addFoodBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);

        int apiCaloriesVal = 119;
        TextView apiCalories = (TextView) findViewById(R.id.newMealCalories);
        apiCalories.setText(String.valueOf(apiCaloriesVal));

        int apiProteinVal = 24;
        TextView apiProtein = (TextView) findViewById(R.id.newMealProtein);
        apiProtein.setText(String.valueOf(apiProteinVal));

        int apiFatVal = 2;
        TextView apiFat = (TextView) findViewById(R.id.newMealFat);
        apiFat.setText(String.valueOf(apiFatVal));

        int apiCarbsVal = 0;
        TextView apiCarbs = (TextView) findViewById(R.id.newMealCarbs);
        apiCarbs.setText(String.valueOf(apiCarbsVal));

        addFoodBtn = (Button)findViewById(R.id.manualAddFood);
        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addingFood = new Intent(FoodInfo.this, MainActivity.class);
                addingFood.putExtra("caloriesNum", apiCaloriesVal);
                addingFood.putExtra("proteinNum", apiProteinVal);
                addingFood.putExtra("fatNum",apiFatVal);
                addingFood.putExtra("carbsNum", apiCaloriesVal);
                startActivity(addingFood);

            }
        });
    }
}