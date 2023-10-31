            package com.example.fitnessapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

    public class MainActivity extends AppCompatActivity{
    private TextView setCalories, setFat, setCarbs, setProtein ;
    private EditText userTargetCalories, userTargetFat, userTargetCarbs, userTargetProtein;
    private Button addFoodBtn, manualFoodBtn, undoFoodBtn, resetAllBtn;

    ImageButton lockBtn;
    private boolean isLocked = true;

    private float caloriesVal = 0f;
    private float fatVal = 0f;
    private float carbsVal = 0f;
    private float proteinVal = 0f;

    private float userTargetCaloriesVal = 0f;
    private float userTargetFatVal = 0f;
    private float userTargetCarbsVal = 0f;
    private float userTargetProteinVal = 0f;

    private float userTargetNutrientVal = 0f;

    public static final String savedValCalories = "calories";
    public static final String savedValFat = "fat";
    public static final String savedValCarbs = "carbs";
    public static final String savedValProtein = "protein";

    public static final String savedUserTargetCalories = "target_calories";
    public static final String savedUserTargetFat = "target_fat";
    public static final String savedUserTargetCarbs = "target_carbs";
    public static final String savedUserTargetProtein = "target_protein";

    public static final String undoLastCalories = "undo_last_calories";
    public static final String undoLastFat = "undo_last_fat";
    public static final String undoLastCarbs = "undo_last_carbs";
    public static final String undoLastProtein = "undo_last_protein";

    private float foodCaloriesVal, foodFatVal, foodCarbsVal, foodProteinVal;


    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Intent intent = result.getData();
                if(intent != null){
                    foodCaloriesVal = intent.getFloatExtra("foodCalories", 0f);
                    caloriesVal = caloriesVal + foodCaloriesVal;
                    caloriesVal = (float) (Math.round(caloriesVal * 100.0) / 100.0);

                    foodFatVal = intent.getFloatExtra("foodFat", 0f);
                    fatVal = fatVal + foodFatVal;
                    fatVal = (float) (Math.round(fatVal * 100.0) / 100.0);

                    foodCarbsVal = intent.getFloatExtra("foodCarbs", 0f);
                    carbsVal = carbsVal + foodCarbsVal;
                    carbsVal = (float) (Math.round(carbsVal * 100.0) / 100.0);

                    foodProteinVal = intent.getFloatExtra("foodProtein", 0f);
                    proteinVal = proteinVal + foodProteinVal;
                    proteinVal = (float) (Math.round(proteinVal * 100.0) / 100.0);

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
        setFat = findViewById(R.id.fat);
        setCarbs =  findViewById(R.id.carbs);
        setProtein = findViewById(R.id.protein);

        setValues();

        userTargetCalories = findViewById(R.id.targetCalories);
        userTargetFat = findViewById(R.id.targetFat);
        userTargetCarbs = findViewById(R.id.targetCarbs);
        userTargetProtein = findViewById(R.id.targetProtein);

        setTargetValues();
        setEditTextFalse();

        lockBtn=findViewById(R.id.lockButton);
        lockBtn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(isLocked){

                    isLocked=false;
                    lockBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_open_lock));

                    userTargetCalories.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    userTargetFat.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    userTargetCarbs.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    userTargetProtein.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    setEditTextTrue();

                }else{

                    isLocked=true;
                    lockBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock));

                    setTargetNutrients(userTargetCalories);
                    userTargetCaloriesVal = getUserTargetNutrientVal();

                    setTargetNutrients(userTargetFat);
                    userTargetFatVal = getUserTargetNutrientVal();

                    setTargetNutrients(userTargetCarbs);
                    userTargetCarbsVal = getUserTargetNutrientVal();

                    setTargetNutrients(userTargetProtein);
                    userTargetProteinVal = getUserTargetNutrientVal();

                    setEditTextFalse();
                    saveSharedTargetPreferences();
                }
            }
        });

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
                    foodCaloriesVal = (float) (Math.floor(foodCaloriesVal * 100) / 100);
                    caloriesVal = caloriesVal - foodCaloriesVal;
                    caloriesVal = (float) (Math.floor(caloriesVal * 100) / 100);

                    foodFatVal = (float) (Math.floor(foodFatVal * 100) / 100);
                    fatVal = fatVal - foodFatVal;
                    fatVal = (float) (Math.floor(fatVal * 100) / 100);

                    foodCarbsVal = (float) (Math.floor(foodCarbsVal * 100) / 100);
                    carbsVal = carbsVal - foodCarbsVal;
                    carbsVal = (float) (Math.floor(carbsVal * 100) / 100);

                    foodProteinVal = (float) (Math.floor(foodProteinVal * 100) / 100);
                    proteinVal = proteinVal - foodProteinVal;
                    proteinVal = (float) (Math.floor(proteinVal * 100) / 100);

                    setValues();
                    saveSharedPreferences();

                    foodCaloriesVal = 0;
                    foodFatVal = 0;
                    foodCarbsVal = 0;
                    foodProteinVal = 0;
                };
            }
        });


        resetAllBtn = (Button) findViewById(R.id.resetAll);
        resetAllBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                 caloriesVal = 0;
                 fatVal = 0;
                 carbsVal = 0;
                 proteinVal = 0;

                 setValues();
                 saveSharedPreferences();

            }
        });
    }
        public void saveSharedPreferences(){
            SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putFloat(savedValCalories, caloriesVal);
            editor.putFloat(savedValFat, fatVal);
            editor.putFloat(savedValCarbs, carbsVal);
            editor.putFloat(savedValProtein, proteinVal);

            editor.putFloat(undoLastCalories, foodCaloriesVal);
            editor.putFloat(undoLastFat, foodFatVal);
            editor.putFloat(undoLastCarbs, foodCarbsVal);
            editor.putFloat(undoLastProtein, foodProteinVal);

            editor.apply();
        }

        public void saveSharedTargetPreferences(){
            SharedPreferences sharedTargetPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedTargetPreferences.edit();

            editor.putFloat(savedUserTargetCalories, userTargetCaloriesVal);
            editor.putFloat(savedUserTargetFat, userTargetFatVal);
            editor.putFloat(savedUserTargetCarbs, userTargetCarbsVal);
            editor.putFloat(savedUserTargetProtein, userTargetProteinVal);

            editor.apply();
        };

        public void loadData() {
            SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);

            caloriesVal = sharedPreferences.getFloat(savedValCalories,0f);
            fatVal = sharedPreferences.getFloat(savedValFat,0f);
            proteinVal = sharedPreferences.getFloat(savedValProtein,0f);
            carbsVal  = sharedPreferences.getFloat(savedValCarbs,0f);

            userTargetCaloriesVal = sharedPreferences.getFloat(savedUserTargetCalories, 0f);
            userTargetFatVal = sharedPreferences.getFloat(savedUserTargetFat, 0f);
            userTargetCarbsVal = sharedPreferences.getFloat(savedUserTargetCarbs, 0f);
            userTargetProteinVal = sharedPreferences.getFloat(savedUserTargetProtein, 0f);

            foodCaloriesVal = sharedPreferences.getFloat(undoLastCalories,0f);
            foodFatVal = sharedPreferences.getFloat(undoLastFat,0f);
            foodCarbsVal = sharedPreferences.getFloat(undoLastCarbs,0f);
            foodProteinVal = sharedPreferences.getFloat(undoLastProtein,0f);
        }
        private void setValues() {
            setCalories.setText(String.valueOf(caloriesVal));
            setFat.setText(String.valueOf(fatVal));
            setCarbs.setText(String.valueOf(carbsVal));
            setProtein.setText(String.valueOf(proteinVal));
        }
        private void setTargetValues(){
            userTargetCalories.setText(String.valueOf(userTargetCaloriesVal), TextView.BufferType.EDITABLE);
            userTargetFat.setText(String.valueOf(userTargetFatVal), TextView.BufferType.EDITABLE);
            userTargetCarbs.setText(String.valueOf(userTargetCarbsVal), TextView.BufferType.EDITABLE);
            userTargetProtein.setText(String.valueOf(userTargetProteinVal), TextView.BufferType.EDITABLE);
        }

        @SuppressLint("NewApi")
        public void setTargetNutrients(EditText userTargetNutrient){
            userTargetNutrient.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.transparent));
            String userTargetNutrientToString = userTargetNutrient.getText().toString();
            if (userTargetNutrientToString.equals("")){
                userTargetNutrientToString="0";
            }
            userTargetNutrientVal = Float.parseFloat(userTargetNutrientToString);
        }

        private float getUserTargetNutrientVal(){
            return userTargetNutrientVal;
        }

        private void setEditTextFalse(){
            userTargetCalories.setEnabled(false);
            userTargetFat.setEnabled(false);
            userTargetCarbs.setEnabled(false);
            userTargetProtein.setEnabled(false);
        }
        private void setEditTextTrue(){
            userTargetCalories.setEnabled(true);
            userTargetFat.setEnabled(true);
            userTargetCarbs.setEnabled(true);
            userTargetProtein.setEnabled(true);
        }
    }
