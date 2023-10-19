package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDBEditItem extends AppCompatActivity {
    private EditText itemName, itemCalories, itemfat, itemcarbs, itemprotein;
    private int idForEdit;
    private DatabaseHelper databaseHelper;
    private Button submitEdit, backEdit;
    private String editFoodName;
    private float editFoodCalories, editFoodFat, editFoodCarbs, editFoodProtein;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_edit_item);

        itemName = findViewById(R.id.itemEditMealName);
        itemCalories = findViewById(R.id.itemEditMealCalories);
        itemfat = findViewById(R.id.itemEditMealFat);
        itemcarbs = findViewById(R.id.itemEditMealCarbs);
        itemprotein = findViewById(R.id.itemEditMealProtein);

        Intent intent = getIntent();
        String id = intent.getStringExtra("editId");
        String name = intent.getStringExtra("editName");
        String calories = intent.getStringExtra("editCalories");
        String fat = intent.getStringExtra("editFat");
        String carbs = intent.getStringExtra("editCarbs");
        String protein = intent.getStringExtra("editProtein");

        itemName.setText(name);
        itemCalories.setText(calories);
        itemfat.setText(fat);
        itemcarbs.setText(carbs);
        itemprotein.setText(protein);

        idForEdit = Integer.parseInt(id);
        editFoodName = name;
        editFoodCalories = Float.parseFloat(calories);
        editFoodFat = Float.parseFloat(fat);
        editFoodCarbs = Float.parseFloat(carbs);
        editFoodProtein = Float.parseFloat(protein);

        submitEdit = findViewById(R.id.itemEditFoodToDB);
        submitEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.editEntry(idForEdit, editFoodName, editFoodCalories, editFoodFat, editFoodCarbs, editFoodProtein);
                finish();
            }
        });

        backEdit = findViewById(R.id.itemEditCancel);
        backEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
