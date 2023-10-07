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

    private float caloriesVal = 0f;
    private float proteinVal = 0f;
    private float fatVal = 0f;
    private float carbsVal = 0f;

    public static final String savedValCalories = "calories";
    public static final String savedValProtein = "protein";
    public static final String savedValFat = "fat";
    public static final String savedValCarbs = "carbs";

    private float caloriesManualVal;
    private float proteinManualVal;
    private float fatManualVal;
    private float carbsManualVal;

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Intent intent = result.getData();
                if(intent != null){
                    caloriesManualVal = intent.getFloatExtra("caloriesManual", 0f);
                    caloriesVal = caloriesVal + caloriesManualVal;
                    caloriesVal = (float) (Math.round(caloriesVal * 100.0) / 100.0);

                    proteinManualVal = intent.getFloatExtra("proteinManual", 0f);
                    proteinVal = proteinVal + proteinManualVal;
                    proteinVal = (float) (Math.round(proteinVal * 100.0) / 100.0);

                    fatManualVal = intent.getFloatExtra("fatManual", 0f);
                    fatVal = fatVal + fatManualVal;
                    fatVal = (float) (Math.round(fatVal * 100.0) / 100.0);

                    carbsManualVal = intent.getFloatExtra("carbsManual", 0f);
                    carbsVal = carbsVal + carbsManualVal;
                    carbsVal = (float) (Math.round(carbsVal * 100.0) / 100.0);

                    setValues();
                    saveSharedPreferences();
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        setCalories = findViewById(R.id.calories);
        setCalories.setText(String.valueOf(caloriesVal));

        setProtein = findViewById(R.id.protein);
        setProtein.setText(String.valueOf(proteinVal));

        setFat = findViewById(R.id.fat);
        setFat.setText(String.valueOf(fatVal));

        setCarbs =  findViewById(R.id.carbs);
        setCarbs.setText(String.valueOf(carbsVal));

        addFoodBtn = (Button) findViewById(R.id.searchFood);
        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FoodDBDisplay.class);
                startForResult.launch(intent);
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
                    caloriesManualVal = (float) (Math.floor(caloriesManualVal * 100) / 100);
                    caloriesVal = caloriesVal - caloriesManualVal;
                    caloriesVal = (float) (Math.floor(caloriesVal * 100) / 100);

                    proteinManualVal = (float) (Math.floor(proteinManualVal * 100) / 100);
                    proteinVal = proteinVal - proteinManualVal;
                    proteinVal = (float) (Math.floor(proteinVal * 100) / 100);

                    fatManualVal = (float) (Math.floor(fatManualVal * 100) / 100);
                    fatVal = fatVal - fatManualVal;
                    fatVal = (float) (Math.floor(fatVal * 100) / 100);

                    carbsManualVal = (float) (Math.floor(carbsManualVal * 100) / 100);
                    carbsVal = carbsVal - carbsManualVal;
                    carbsVal = (float) (Math.floor(carbsVal * 100) / 100);

                    setValues();
                    saveSharedPreferences();

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
                 saveSharedPreferences();

            }
        });
    }
        public void saveSharedPreferences(){
            SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putFloat(savedValCalories, caloriesVal);
            editor.putFloat(savedValProtein, proteinVal);
            editor.putFloat(savedValFat, fatVal);
            editor.putFloat(savedValCarbs, carbsVal);

            editor.apply();
        }

        public void loadData() {
            SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
            caloriesVal = sharedPreferences.getFloat(savedValCalories,0f);
            proteinVal = sharedPreferences.getFloat(savedValProtein,0f);
            fatVal = sharedPreferences.getFloat(savedValFat,0f);
            carbsVal  = sharedPreferences.getFloat(savedValCarbs,0f);
        }
        private void setValues() {
            setCalories.setText(String.valueOf(caloriesVal));
            setProtein.setText(String.valueOf(proteinVal));
            setFat.setText(String.valueOf(fatVal));
            setCarbs.setText(String.valueOf(carbsVal));
        }

    }
