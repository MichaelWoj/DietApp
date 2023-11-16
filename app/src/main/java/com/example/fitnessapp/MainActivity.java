            package com.example.fitnessapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.gson.Gson;

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

import java.util.ArrayList;
import java.util.Arrays;

            public class MainActivity extends AppCompatActivity{
    private TextView setCalories, setFat, setCarbs, setProtein ;
    private EditText userTargetCalories, userTargetFat, userTargetCarbs, userTargetProtein;
    private Button foodDbBtn, manualFoodBtn, undoFoodBtn, resetAllBtn;
    private ArrayList<Float> addedFoodCaloriesList, addedFoodFatList, addedFoodCarbsList, addedFoodProteinList;

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

    public static final String undoCaloriesList = "undo_calorie_list";
    public static final String undoFatList = "undo_fat_list";
    public static final String undoCarbList = "undo_carbs_list";
    public static final String undoProteinList = "undo_protein_list";

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
                    addedFoodCaloriesList.add(foodCaloriesVal);

                    foodFatVal = intent.getFloatExtra("foodFat", 0f);
                    fatVal = fatVal + foodFatVal;
                    fatVal = (float) (Math.round(fatVal * 100.0) / 100.0);
                    addedFoodFatList.add(foodFatVal);

                    foodCarbsVal = intent.getFloatExtra("foodCarbs", 0f);
                    carbsVal = carbsVal + foodCarbsVal;
                    carbsVal = (float) (Math.round(carbsVal * 100.0) / 100.0);
                    addedFoodCarbsList.add(foodCarbsVal);

                    foodProteinVal = intent.getFloatExtra("foodProtein", 0f);
                    proteinVal = proteinVal + foodProteinVal;
                    proteinVal = (float) (Math.round(proteinVal * 100.0) / 100.0);
                    addedFoodProteinList.add(foodProteinVal);

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

        addedFoodCaloriesList = new ArrayList<>();
        addedFoodFatList = new ArrayList<>();
        addedFoodCarbsList = new ArrayList<>();
        addedFoodProteinList = new ArrayList<>();

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
        //Its set to false so it can't be the target EditTexts can't be edited
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

        foodDbBtn = (Button) findViewById(R.id.searchFood);
        foodDbBtn.setOnClickListener(new View.OnClickListener() {
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
                    // The values were put into variables to help with code readability
                    float calorieNum = addedFoodCaloriesList.get(addedFoodCaloriesList.size() -1);
                    float fatNum = addedFoodFatList.get(addedFoodFatList.size() -1);
                    float carbsNum = addedFoodCarbsList.get(addedFoodCarbsList.size() -1);
                    float proteinNum = addedFoodProteinList.get(addedFoodProteinList.size() -1);

                    int listIndex = addedFoodCaloriesList.size() -1;

                    foodCaloriesVal = (float) calorieNum;
                    caloriesVal = caloriesVal - foodCaloriesVal;
                    caloriesVal = (float) (Math.rint(caloriesVal * 100) / 100);
                    addedFoodCaloriesList.remove(listIndex);

                    foodFatVal = (float) fatNum;
                    fatVal = fatVal - foodFatVal;
                    fatVal = (float) (Math.rint(fatVal * 100) / 100);
                    addedFoodFatList.remove(listIndex);

                    foodCarbsVal = (float) carbsNum;
                    carbsVal = carbsVal - foodCarbsVal;
                    carbsVal = (float) (Math.rint(carbsVal * 100) / 100);
                    addedFoodCarbsList.remove(listIndex);

                    foodProteinVal = (float) proteinNum;
                    proteinVal = proteinVal - foodProteinVal;
                    proteinVal = (float) (Math.rint(proteinVal * 100) / 100);
                    addedFoodProteinList.remove(listIndex);

                    setValues();
                    saveSharedUndoPreferences();

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

                 addedFoodCaloriesList.clear();
                 addedFoodFatList.clear();
                 addedFoodCarbsList.clear();
                 addedFoodProteinList.clear();

                 setValues();
                 saveSharedPreferences();

            }
        });
    }
        private void saveSharedPreferences(){
            SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putFloat(savedValCalories, caloriesVal);
            editor.putFloat(savedValFat, fatVal);
            editor.putFloat(savedValCarbs, carbsVal);
            editor.putFloat(savedValProtein, proteinVal);

            editor.apply();
            saveSharedUndoPreferences();
        }

        private void saveSharedUndoPreferences(){
            SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            Gson gson = new Gson();
            String caloriesJson = gson.toJson(addedFoodCaloriesList);
            String fatJson = gson.toJson(addedFoodFatList);
            String carbsJson = gson.toJson(addedFoodCarbsList);
            String proteinJson = gson.toJson(addedFoodProteinList);

            editor.putString(undoCaloriesList, caloriesJson);
            editor.putString(undoFatList, fatJson);
            editor.putString(undoCarbList, carbsJson);
            editor.putString(undoProteinList, proteinJson);

            editor.apply();
        }

        private void saveSharedTargetPreferences(){
            SharedPreferences sharedTargetPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedTargetPreferences.edit();

            editor.putFloat(savedUserTargetCalories, userTargetCaloriesVal);
            editor.putFloat(savedUserTargetFat, userTargetFatVal);
            editor.putFloat(savedUserTargetCarbs, userTargetCarbsVal);
            editor.putFloat(savedUserTargetProtein, userTargetProteinVal);

            editor.apply();
        };

        private void loadData() {
            SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);

            caloriesVal = sharedPreferences.getFloat(savedValCalories,0f);
            fatVal = sharedPreferences.getFloat(savedValFat,0f);
            proteinVal = sharedPreferences.getFloat(savedValProtein,0f);
            carbsVal  = sharedPreferences.getFloat(savedValCarbs,0f);

            userTargetCaloriesVal = sharedPreferences.getFloat(savedUserTargetCalories, 0f);
            userTargetFatVal = sharedPreferences.getFloat(savedUserTargetFat, 0f);
            userTargetCarbsVal = sharedPreferences.getFloat(savedUserTargetCarbs, 0f);
            userTargetProteinVal = sharedPreferences.getFloat(savedUserTargetProtein, 0f);

            String caloriesJson = sharedPreferences.getString(undoCaloriesList, null);
            String fatJson = sharedPreferences.getString(undoFatList, null);
            String carbsJson = sharedPreferences.getString(undoCarbList, null);
            String proteinJson = sharedPreferences.getString(undoProteinList, null);

            undoJsonStringToFloatList(caloriesJson, addedFoodCaloriesList);
            undoJsonStringToFloatList(fatJson, addedFoodFatList);
            undoJsonStringToFloatList(carbsJson, addedFoodCarbsList);
            undoJsonStringToFloatList(proteinJson, addedFoodProteinList);
        }

        private void undoJsonStringToFloatList(String loadedJsonString, ArrayList<Float> targetFloatList){
            if(loadedJsonString.equals("[]")){
                loadedJsonString = null;
            }

            else if(loadedJsonString != null){
                loadedJsonString = loadedJsonString.replace("[", "").replace("]", "");
                ArrayList<String> inputStringList = new ArrayList<String>(Arrays.asList(loadedJsonString.split(",")));

                for (int i = 0; i < inputStringList.size(); ++i) {
                    float number = Float.parseFloat(inputStringList.get(i));
                    targetFloatList.add(number);
                    }
            }
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
