        package com.example.fitnessapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

    public class MainActivity extends AppCompatActivity{
    private TextView setCalories, setProtein, setFat, setCarbs ;
    private Button addFoodBtn, manualFoodBtn, undoFoodBtn, resetAllBtn;

    private double caloriesVal = 0;
    private double proteinVal = 0;
    private double fatVal = 0;
    private double carbsVal = 0;

    private double caloriesManualVal = 0;
    private double proteinManualVal = 0;
    private double fatManualVal = 0;
    private double carbsManualVal = 0;

    private UserNutrientValues values;

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Intent intent = result.getData();
                if(intent != null){
                    caloriesManualVal = intent.getDoubleExtra("caloriesManual", 0);
                    caloriesVal = caloriesVal + caloriesManualVal;
                    caloriesVal = Math.round(caloriesVal * 100.0) / 100.0;

                    proteinManualVal = intent.getDoubleExtra("proteinManual", 0);
                    proteinVal = proteinVal + proteinManualVal;
                    proteinVal = Math.round(proteinVal * 100.0) / 100.0;

                    fatManualVal = intent.getDoubleExtra("fatManual", 0);
                    fatVal = fatVal + fatManualVal;
                    fatVal = Math.round(fatVal * 100.0) / 100.0;

                    carbsManualVal = intent.getDoubleExtra("carbsManual", 0);
                    carbsVal = carbsVal + carbsManualVal;
                    carbsVal = Math.round(carbsVal * 100.0) / 100.0;

                    setValues();
                    values.getNutrientVals(caloriesVal, proteinVal, fatVal, carbsVal);
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        values = new UserNutrientValues();

        setCalories = findViewById(R.id.calories);
        setCalories.setText(String.valueOf(UserNutrientValues.caloriesValSettings));

        setProtein = findViewById(R.id.protein);
        setProtein.setText(String.valueOf(UserNutrientValues.proteinValSettings));

        setFat = findViewById(R.id.fat);
        setFat.setText(String.valueOf(UserNutrientValues.fatValSettings));

        setCarbs =  findViewById(R.id.carbs);
        setCarbs.setText(String.valueOf(UserNutrientValues.carbsValSettings));

        addFoodBtn = (Button) findViewById(R.id.searchFood);
        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FoodDBDisplay.class);
                startActivity(intent);
            }
        });

        manualFoodBtn = (Button) findViewById(R.id.manualAdd);
        manualFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManuallyAdd.class);
                startForResult.launch(intent);
            }
        });

        undoFoodBtn = (Button) findViewById(R.id.undoFood);
        undoFoodBtn.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                if (caloriesVal != 0 ){
                    caloriesVal = caloriesVal - caloriesManualVal;
                    caloriesVal = Math.floor(caloriesVal * 100) / 100;

                    proteinVal = proteinVal - proteinManualVal;
                    proteinVal = Math.floor(proteinVal * 100) / 100;

                    fatVal = fatVal - fatManualVal;
                    fatVal = Math.floor(fatVal * 100) / 100;

                    carbsVal = carbsVal - carbsManualVal;
                    carbsVal = Math.floor(carbsVal * 100) / 100;

                    setValues();

                    caloriesManualVal = 0;
                    proteinManualVal = 0;
                    fatManualVal = 0;
                    carbsManualVal = 0;
                };
            }
        });


        resetAllBtn = (Button) findViewById(R.id.resetAll);
        resetAllBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                 caloriesVal = 0;
                 proteinVal = 0;
                 fatVal = 0;
                 carbsVal = 0;

                 setValues();
                 //values.getNutrientVals(caloriesVal, proteinVal, fatVal, carbsVal);

            }
        });
    }
        public void saveSharedPreferences(){
            SharedPreferences sharedPreferences = getSharedPreferences("SetValues", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();


            editor.apply();
        }
        private void setValues() {
            setCalories.setText(String.valueOf(caloriesVal));
            setProtein.setText(String.valueOf(proteinVal));
            setFat.setText(String.valueOf(fatVal));
            setCarbs.setText(String.valueOf(carbsVal));
        }

    }
