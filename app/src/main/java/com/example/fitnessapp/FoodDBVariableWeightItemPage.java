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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class FoodDBVariableWeightItemPage extends AppCompatActivity {
    private TextView nameDB, caloriesDB, fatDB, carbsDB, proteinDB;
    private EditText itemWeight;
    private int entryID, foodDisplayWeight;
    private float foodCaloriesVal, foodFatVal, foodCarbsVal, foodProteinVal, foodTargetWeightVal;
    private String entryIDString, itemSetDisplayWeight;
    FoodDBAddItemVariableWeight foodDBAddItemVariableWeight;
    FoodDBItemPage foodDBItemPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_variable_weight_item_page);
        foodDBAddItemVariableWeight = new FoodDBAddItemVariableWeight();
        foodDBItemPage = new FoodDBItemPage();

        nameDB = findViewById(R.id.variableWeightItemMealName);
        caloriesDB = findViewById(R.id.variableWeightItemMealCalories);
        fatDB = findViewById(R.id.variableWeightItemMealFat);
        carbsDB = findViewById(R.id.variableWeightItemMealCarbs);
        proteinDB = findViewById(R.id.variableWeightItemMealProtein);
        itemWeight = findViewById(R.id.variableItemWeight);

        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        String itemSetName = intent.getStringExtra("Name");
        String itemSetCalories = intent.getStringExtra("Calories");
        String itemSetFat = intent.getStringExtra("Fat");
        String itemSetCarbs = intent.getStringExtra("Carbs");
        String itemSetProtein = intent.getStringExtra("Protein");
        itemSetDisplayWeight = intent.getStringExtra("DisplayWeight");

        entryID = Integer.parseInt(id);
        entryIDString = id;

        setText(itemSetName, itemSetCalories, itemSetFat, itemSetCarbs, itemSetProtein);

        Button submit = findViewById(R.id.variableWeightItemAddFood);
        submit.setOnClickListener(v -> {
            String foodTargetWeight = itemWeight.getText().toString();
            if ( foodTargetWeight.isEmpty() || foodTargetWeight.equals("0")) {
                Toast.makeText(FoodDBVariableWeightItemPage.this, "Please insert all info", Toast.LENGTH_SHORT).show();
            } else {
                Intent intentAddToOverallTotal = new Intent(FoodDBVariableWeightItemPage.this, MainActivity.class);

                //The line currently does nothing but is here for an upcoming feature
                String foodNameToString = nameDB.getText().toString();

                foodDisplayWeight = Integer.parseInt(itemSetDisplayWeight);

                String foodCaloriesToString = caloriesDB.getText().toString();
                foodCaloriesVal = Float.parseFloat(foodCaloriesToString);

                String foodFatToString = fatDB.getText().toString();
                foodFatVal = Float.parseFloat(foodFatToString);

                String foodCarbsToString = carbsDB.getText().toString();
                foodCarbsVal = Float.parseFloat(foodCarbsToString);

                String foodProteinToString = proteinDB.getText().toString();
                foodProteinVal = Float.parseFloat(foodProteinToString);

                foodTargetWeightVal = Float.parseFloat(foodTargetWeight);
                float[] variableResultArray = foodDBAddItemVariableWeight.calculateNutrientsToTargetWeightVal(foodDisplayWeight, foodCaloriesVal, foodFatVal, foodCarbsVal, foodProteinVal, foodTargetWeightVal);

                foodCaloriesVal = variableResultArray[0];
                foodFatVal = variableResultArray[1];
                foodCarbsVal = variableResultArray[2];
                foodTargetWeightVal = variableResultArray[3];

                intentAddToOverallTotal.putExtra("foodCalories", foodCaloriesVal);
                intentAddToOverallTotal.putExtra("foodFat", foodFatVal);
                intentAddToOverallTotal.putExtra("foodCarbs", foodCarbsVal);
                intentAddToOverallTotal.putExtra("foodProtein", foodProteinVal);

                setResult(RESULT_OK, intentAddToOverallTotal);
                finish();
            }
        });

        ImageButton settings = findViewById(R.id.variableWeightItemSettingsBtn);
        settings.setOnClickListener(v -> showDialog());

        Button back = findViewById(R.id.variableWeightItemCancel);
        back.setOnClickListener(v -> finish());
    }

    private void setText(String name, String calories, String fat, String carbs, String protein){
        nameDB.setText(name);
        caloriesDB.setText(calories);
        fatDB.setText(fat);
        carbsDB.setText(carbs);
        proteinDB.setText(protein);
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_db_settings_popup);

        LinearLayout editLayout = dialog.findViewById(R.id.layoutSettingsEdit);
        LinearLayout deleteLayout = dialog.findViewById(R.id.layoutSettingsDelete);


        editLayout.setOnClickListener(v -> {
            Intent intent = new Intent(FoodDBVariableWeightItemPage.this, FoodDBEditVariableWeightItem.class);

            intent.putExtra("editId",entryIDString);

            String editFoodNameToString = nameDB.getText().toString();
            intent.putExtra("editName", editFoodNameToString);

            String editFoodCaloriesToString = caloriesDB.getText().toString();
            intent.putExtra("editCalories", editFoodCaloriesToString);

            String editFoodFatToString = fatDB.getText().toString();
            intent.putExtra("editFat", editFoodFatToString);

            String editFoodCarbsToString = carbsDB.getText().toString();
            intent.putExtra("editCarbs", editFoodCarbsToString);

            String editFoodProteinToString = proteinDB.getText().toString();
            intent.putExtra("editProtein", editFoodProteinToString);

            String editFoodWeightToString = itemSetDisplayWeight.toString();
            intent.putExtra("editWeight",editFoodWeightToString);

            startForRefreshItemPage.launch(intent);
            dialog.dismiss();

        });

        deleteLayout.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(FoodDBVariableWeightItemPage.this, FoodDBDisplay.class);
            foodDBItemPage.deleteConfirmationWindow(intent, entryID, this);
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    ActivityResultLauncher<Intent> startForRefreshItemPage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Intent intent = result.getData();

                String newName = intent.getStringExtra("editFoodName");

                Float newCalories = intent.getFloatExtra("editFoodCalories", foodCaloriesVal);
                String newCaloriesString = newCalories.toString();

                Float newFat = intent.getFloatExtra("editFoodFat", foodFatVal);
                String newFatString = newFat.toString();

                Float newCarbs = intent.getFloatExtra("editFoodCarbs", foodCarbsVal);
                String newCarbsString = newCarbs.toString();

                Float newProtein = intent.getFloatExtra("editFoodProtein", foodProteinVal);
                String newProteinString = newProtein.toString();

                setText(newName, newCaloriesString, newFatString, newCarbsString, newProteinString);
            }
        }
    });
}

