    package com.example.fitnessapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class FoodDBItemPage extends AppCompatActivity {

    private TextView nameDB, caloriesDB, fatDB, carbsDB, proteinDB;
    private DatabaseHelper databaseHelper;
    private int entryID;
    private float foodCaloriesVal, foodFatVal, foodCarbsVal, foodProteinVal;
    private String entryIDString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_db_item_page_layout);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        nameDB = findViewById(R.id.itemMealName);
        caloriesDB = findViewById(R.id.itemMealCalories);
        fatDB = findViewById(R.id.itemMealFat);
        carbsDB = findViewById(R.id.itemMealCarbs);
        proteinDB = findViewById(R.id.itemMealProtein);

        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        String itemSetName = intent.getStringExtra("Name");
        String itemSetCalories = intent.getStringExtra("Calories");
        String itemSetFat = intent.getStringExtra("Fat");
        String itemSetCarbs = intent.getStringExtra("Carbs");
        String itemSetProtein = intent.getStringExtra("Protein");

        entryID = Integer.parseInt(id);
        entryIDString = id;

        setText(itemSetName, itemSetCalories, itemSetFat, itemSetCarbs, itemSetProtein);

        Button submit = findViewById(R.id.itemAddFood);
        submit.setOnClickListener(v -> {
        Intent intentAddToOverallTotal = new Intent(FoodDBItemPage.this, MainActivity.class);

        //The line currently does nothing but is here for an upcoming feature
        String foodNameToString = nameDB.getText().toString();

        String foodCaloriesToString = caloriesDB.getText().toString();
        foodCaloriesVal = Float.parseFloat(foodCaloriesToString);
        intentAddToOverallTotal.putExtra("foodCalories", foodCaloriesVal);

        String foodFatToString = fatDB.getText().toString();
        foodFatVal = Float.parseFloat(foodFatToString);
        intentAddToOverallTotal.putExtra("foodFat", foodFatVal);

        String foodCarbsToString = carbsDB.getText().toString();
        foodCarbsVal = Float.parseFloat(foodCarbsToString);
        intentAddToOverallTotal.putExtra("foodCarbs", foodCarbsVal);

        String foodProteinToString = proteinDB.getText().toString();
        foodProteinVal = Float.parseFloat(foodProteinToString);
        intentAddToOverallTotal.putExtra("foodProtein", foodProteinVal);

        setResult(RESULT_OK, intentAddToOverallTotal);
        finish();
        });

        ImageButton settings = findViewById(R.id.itemSettingsBtn);
        settings.setOnClickListener(v -> showDialog());

        Button back = findViewById(R.id.itemCancel);
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
            Intent intent = new Intent(FoodDBItemPage.this, FoodDBEditItem.class);

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

            startForRefreshItemPage.launch(intent);
            dialog.dismiss();

        });

        deleteLayout.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(FoodDBItemPage.this, FoodDBDisplay.class);
            deleteConfirmationWindow(intent, entryID, this);
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

    public void deleteConfirmationWindow(Intent intent, Integer idOfEntry, Context context) {

        final Dialog confirmationDialog = new Dialog(context);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        confirmationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmationDialog.setContentView(R.layout.activity_db_settings_delete_popup);

        LinearLayout confirmDelete = confirmationDialog.findViewById(R.id.layoutConfirmDelete);
        LinearLayout confirmCancel = confirmationDialog.findViewById(R.id.layoutConfirmCancel);

        confirmDelete.setOnClickListener(v -> {
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            databaseHelper.deleteEntry(idOfEntry);
            confirmationDialog.dismiss();
            finish();
        });
        confirmCancel.setOnClickListener(v -> confirmationDialog.dismiss());

        confirmationDialog.show();
        confirmationDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        confirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmationDialog.getWindow().setGravity(Gravity.CENTER);
    }
}
