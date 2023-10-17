    package com.example.fitnessapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDBItemPage extends AppCompatActivity {

    private TextView nameDB, caloriesDB, fatDB, carbsDB, proteinDB;
    private Button submit, back;
    private ImageButton settings;
    private DatabaseHelper databaseHelper;

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
            public void onClick(View v){
            Intent intent = new Intent(FoodDBItemPage.this, MainActivity.class);

            String foodNameToString = nameDB.getText().toString();

            String foodCaloriesToString = caloriesDB.getText().toString();
            float foodCaloriesVal = Float.parseFloat(foodCaloriesToString);
            intent.putExtra("foodCalories", foodCaloriesVal);

            String foodFatToString = fatDB.getText().toString();
            float foodFatVal = Float.parseFloat(foodFatToString);
            intent.putExtra("foodFat", foodFatVal);

            String foodCarbsToString = carbsDB.getText().toString();
            float foodCarbsVal = Float.parseFloat(foodCarbsToString);
            intent.putExtra("foodCarbs", foodCarbsVal);

            String foodProteinToString = proteinDB.getText().toString();
            float foodProteinVal = Float.parseFloat(foodProteinToString);
            intent.putExtra("foodProtein", foodProteinVal);

            setResult(RESULT_OK, intent);
            finish();
            }
        });

        settings = findViewById(R.id.settingsBtn);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();

            }
        });
        back = findViewById(R.id.itemCancel);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDBItemPage.this, FoodDBDisplay.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                finish();
                startActivity(intent);
            }
        });
    }
    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_db_settings_popup);

        LinearLayout editLayout = dialog.findViewById(R.id.layoutEdit);
        LinearLayout deleteLayout = dialog.findViewById(R.id.layoutDelete);


        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}
