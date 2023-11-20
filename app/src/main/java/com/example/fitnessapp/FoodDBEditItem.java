package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDBEditItem extends AppCompatActivity {
    private EditText itemName, itemCalories, itemFat, itemCarbs, itemProtein;
    private int idForEdit;
    private DatabaseHelper databaseHelper;
    private float editFoodCalories, editFoodFat, editFoodCarbs, editFoodProtein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_edit_item);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        itemName = findViewById(R.id.itemEditMealName);
        itemCalories = findViewById(R.id.itemEditMealCalories);
        itemFat = findViewById(R.id.itemEditMealFat);
        itemCarbs = findViewById(R.id.itemEditMealCarbs);
        itemProtein = findViewById(R.id.itemEditMealProtein);

        Intent intent = getIntent();
        String id = intent.getStringExtra("editId");
        String name = intent.getStringExtra("editName");
        String calories = intent.getStringExtra("editCalories");
        String fat = intent.getStringExtra("editFat");
        String carbs = intent.getStringExtra("editCarbs");
        String protein = intent.getStringExtra("editProtein");

        itemName.setText(name);
        itemCalories.setText(calories);
        itemFat.setText(fat);
        itemCarbs.setText(carbs);
        itemProtein.setText(protein);

        idForEdit = Integer.parseInt(id);

        Button submitEdit = findViewById(R.id.itemEditFoodToDB);
        submitEdit.setOnClickListener(v -> {
            Intent intent1 = new Intent(FoodDBEditItem.this, FoodDBItemPage.class);

            String editFoodName = itemName.getText().toString();
            intent1.putExtra("editFoodName", editFoodName);

            String editFoodCaloriesToString = itemCalories.getText().toString();
            editFoodCalories = Float.parseFloat(editFoodCaloriesToString);
            intent1.putExtra("editFoodCalories", editFoodCalories);

            String editFoodFatToString = itemFat.getText().toString();
            editFoodFat = Float.parseFloat(editFoodFatToString);
            intent1.putExtra("editFoodFat", editFoodFat);

            String editFoodCarbsToString = itemCarbs.getText().toString();
            editFoodCarbs = Float.parseFloat(editFoodCarbsToString);
            intent1.putExtra("editFoodCarbs", editFoodCarbs);

            String editFoodProteinToString = itemProtein.getText().toString();
            editFoodProtein = Float.parseFloat(editFoodProteinToString);
            intent1.putExtra("editFoodProtein", editFoodProtein);

            databaseHelper.editEntry(idForEdit, editFoodName, editFoodCalories, editFoodFat, editFoodCarbs, editFoodProtein);
            setResult(RESULT_OK, intent1);
            finish();
        });

        Button backEdit = findViewById(R.id.itemEditCancel);
        backEdit.setOnClickListener(v -> finish());
    }
}
