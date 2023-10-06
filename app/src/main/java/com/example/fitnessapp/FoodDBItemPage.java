package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDBItemPage extends AppCompatActivity {

    private TextView nameDB, caloriesDB, fatDB, carbsDB, proteinDB;
    private Button submit, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_db_item_page_layout);
        nameDB = findViewById(R.id.itemMealName);
        caloriesDB = findViewById(R.id.itemMealCalories);
        fatDB = findViewById(R.id.itemMealFat);
        carbsDB = findViewById(R.id.itemMealCarbs);
        proteinDB = findViewById(R.id.itemMealProtein);

        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        String calories = intent.getStringExtra("Calories");
        String fat = intent.getStringExtra("Fat");
        String carbs = intent.getStringExtra("Carbs");
        String protein = intent.getStringExtra("Protein");

        nameDB.setText(name);
        caloriesDB.setText(calories);
        fatDB.setText(fat);
        carbsDB.setText(carbs);
        proteinDB.setText(protein);

        submit = findViewById(R.id.itemAddFood);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back = findViewById(R.id.itemCancel);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
