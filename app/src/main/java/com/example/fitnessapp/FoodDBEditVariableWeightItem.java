package com.example.fitnessapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDBEditVariableWeightItem extends AppCompatActivity {
    private EditText itemName, itemWeight, itemCalories, itemFat, itemCarbs, itemProtein, itemDisplayWeight;
    private int idForEdit, selectedDisplayWeight;
    private DatabaseHelper databaseHelper;

    private FoodDBAddItemVariableWeight foodDBAddItemVariableWeight;
    private float editFoodCalories, editFoodWeight, editFoodFat, editFoodCarbs, editFoodProtein;
    private String weight ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_edit_variable_weight_item);

        foodDBAddItemVariableWeight = new FoodDBAddItemVariableWeight();
        databaseHelper = new DatabaseHelper(getApplicationContext());

        itemName = findViewById(R.id.editVariableWeighItemtMealName);
        itemWeight = findViewById(R.id.editVariableWeightItemWeight);
        itemCalories = findViewById(R.id.editVariableWeighItemtMealCalories);
        itemFat = findViewById(R.id.editVariableWeighItemtMealFat);
        itemCarbs = findViewById(R.id.editVariableWeighItemtMealCarbs);
        itemProtein = findViewById(R.id.editVariableWeighItemtMealProtein);

        Button selectDisplayWeight = findViewById(R.id.editVariableWeightItemSelectDisplayWeight);
        selectDisplayWeight.setOnClickListener(view -> {
            selectDisplayWeightWindow();
        });

        Intent intent = getIntent();
        String id = intent.getStringExtra("editId");
        weight = intent.getStringExtra("editWeight");
        String name = intent.getStringExtra("editName");
        String calories = intent.getStringExtra("editCalories");
        String fat = intent.getStringExtra("editFat");
        String carbs = intent.getStringExtra("editCarbs");
        String protein = intent.getStringExtra("editProtein");

        itemName.setText(name);
        itemWeight.setText(weight);
        itemCalories.setText(calories);
        itemFat.setText(fat);
        itemCarbs.setText(carbs);
        itemProtein.setText(protein);

        selectedDisplayWeight = Integer.parseInt(weight);
        idForEdit = Integer.parseInt(id);

        Button submitEdit = findViewById(R.id.editVariableWeighItemtFoodToDB);
        submitEdit.setOnClickListener(v -> {
            Intent intentAddToOverallTotal = new Intent(FoodDBEditVariableWeightItem.this, FoodDBItemPage.class);

            String editFoodName = itemName.getText().toString();
            intentAddToOverallTotal.putExtra("editFoodName", editFoodName);

            String editFoodWeightToString = itemWeight.getText().toString();
            editFoodWeight = Integer.parseInt(editFoodWeightToString);

            String editFoodCaloriesToString = itemCalories.getText().toString();
            editFoodCalories = Float.parseFloat(editFoodCaloriesToString);

            String editFoodFatToString = itemFat.getText().toString();
            editFoodFat = Float.parseFloat(editFoodFatToString);

            String editFoodCarbsToString = itemCarbs.getText().toString();
            editFoodCarbs = Float.parseFloat(editFoodCarbsToString);

            String editFoodProteinToString = itemProtein.getText().toString();
            editFoodProtein = Float.parseFloat(editFoodProteinToString);

            float[] variableResultArray = foodDBAddItemVariableWeight.calculateNutrientsToTargetWeightVal(editFoodWeight, editFoodCalories, editFoodFat, editFoodCarbs, editFoodProtein, selectedDisplayWeight);

            editFoodWeight = variableResultArray[0];
            editFoodCalories = variableResultArray[1];
            editFoodFat = variableResultArray[2];
            editFoodCarbs = variableResultArray[3];
            editFoodProtein = variableResultArray[4];


            intentAddToOverallTotal.putExtra("editFoodWeight", editFoodWeight);
            intentAddToOverallTotal.putExtra("editFoodCalories", editFoodCalories);
            intentAddToOverallTotal.putExtra("editFoodFat", editFoodFat);
            intentAddToOverallTotal.putExtra("editFoodCarbs", editFoodCarbs);
            intentAddToOverallTotal.putExtra("editFoodProtein", editFoodProtein);

            databaseHelper.editEntry(idForEdit, editFoodName, editFoodCalories, editFoodFat, editFoodCarbs, editFoodProtein,selectedDisplayWeight);
            setResult(RESULT_OK, intentAddToOverallTotal);
            finish();
        });

        Button backEdit = findViewById(R.id.editVariableWeighItemtCancel);
        backEdit.setOnClickListener(v -> finish());


    }
    //Refactor this later to use the one in AddItemVariableWeight
    private void selectDisplayWeightWindow() {

        final Dialog displayWeightDialog = new Dialog(this);
        displayWeightDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        displayWeightDialog.setContentView(R.layout.activity_db_variable_weight_popup);
        itemDisplayWeight = findViewById(R.id.displayWeightAmount);
        itemDisplayWeight.setText(weight);

        LinearLayout confirmDisplayWeight = displayWeightDialog.findViewById(R.id.layoutConfirmDisplayWeight);
        LinearLayout cancelDisplayWeight = displayWeightDialog.findViewById(R.id.layoutCancelDisplayWeight);

        confirmDisplayWeight.setOnClickListener(v -> {
            String variableFoodWeightToString = itemDisplayWeight.getText().toString();
            selectedDisplayWeight = Integer.parseInt(variableFoodWeightToString);
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



}
