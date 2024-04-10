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
import java.util.List;

            public class MainActivity extends AppCompatActivity{
    private TextView setCalories, setFat, setCarbs, setProtein ;
    private EditText userTargetCalories, userTargetFat, userTargetCarbs, userTargetProtein;
    public ArrayList<Integer> addedFoodIDs;

    DatabaseHelper databaseHelper;

    ImageButton lockBtn;

    private boolean isLocked = true;

    // The reason for using Floats instead of Doubles is that sharedPreferences doesn't have a getDouble and so it can't be saved
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

    public static final String undoFoodIDList = "undo_food_id_list";

    private float foodCaloriesVal, foodFatVal, foodCarbsVal, foodProteinVal;


    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Intent intent = result.getData();
                if(intent != null){
                    foodCaloriesVal = intent.getFloatExtra("foodCalories", 0f);
                    caloriesVal = caloriesVal + foodCaloriesVal;
                    // Its first multiplied by 100, rounded and then divided by 100 because its its just rounded it'll get rid of the decimal places
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

                    addedFoodIDs.add(databaseHelper.getNewestEntry());

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
        databaseHelper = new DatabaseHelper(MainActivity.this);

        addedFoodIDs = new ArrayList<>();

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
        //Its set to false so the EditText fields for target values can't be changed unless the button for it is pressed
        setEditTextFalse();

        lockBtn=findViewById(R.id.lockButton);
        lockBtn.setOnClickListener(v -> {
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
        });

        Button foodDbBtn = findViewById(R.id.searchFood);
        foodDbBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FoodDBDisplay.class);
            startForResult.launch(intent);
        });

        Button manualFoodBtn = findViewById(R.id.manualAdd);
        manualFoodBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ManuallyAdd.class);
            startForResult.launch(intent);
        });

        Button calendarBtn = findViewById(R.id.calendarButton);
        calendarBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DietCalendar.class);
            startActivity(intent);
        });

        Button undoFoodBtn = findViewById(R.id.undoFood);
        undoFoodBtn.setOnClickListener(v -> {
            if (caloriesVal != 0 ){
                // The values were put into variables to help with code readability

                int foodIDArraySize = addedFoodIDs.size()-1;
                int foodID = addedFoodIDs.get(foodIDArraySize);
                List<String> entryNumber = databaseHelper.findEntry(foodID);

                float calorieNum = Float.parseFloat(entryNumber.get(0));
                float fatNum = Float.parseFloat(entryNumber.get(1));
                float carbsNum = Float.parseFloat(entryNumber.get(2));
                float proteinNum = Float.parseFloat(entryNumber.get(3));


                foodCaloriesVal = calorieNum;
                caloriesVal = caloriesVal - foodCaloriesVal;
                caloriesVal = (float) (Math.round(caloriesVal * 100) / 100);

                foodFatVal = fatNum;
                fatVal = fatVal - foodFatVal;
                fatVal = (float) (Math.round(fatVal * 100) / 100);

                foodCarbsVal = carbsNum;
                carbsVal = carbsVal - foodCarbsVal;
                carbsVal = (float) (Math.round(carbsVal * 100) / 100);

                foodProteinVal = proteinNum;
                proteinVal = proteinVal - foodProteinVal;
                proteinVal = (float) (Math.round(proteinVal * 100) / 100);

                //DO the DB remove somewhere here
                addedFoodIDs.remove(foodIDArraySize);

                setValues();
                saveSharedUndoPreferences();

            }
        });


        Button resetAllBtn = findViewById(R.id.resetAll);
        resetAllBtn.setOnClickListener(v -> {
             caloriesVal = 0;
             fatVal = 0;
             carbsVal = 0;
             proteinVal = 0;

             addedFoodIDs.clear();

             setValues();
             saveSharedPreferences();

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
            String foodIDs = gson.toJson(addedFoodIDs);

            editor.putString(undoFoodIDList, foodIDs);

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
        }

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

            String caloriesJson = sharedPreferences.getString(undoFoodIDList, null);

            undoJsonStringToIntList(caloriesJson, addedFoodIDs);

        }

        private void undoJsonStringToIntList(String loadedJsonString, ArrayList<Integer> targetFloatList){
            if(loadedJsonString == null){
                loadedJsonString = "";
            }
            else if(loadedJsonString.equals("[]")){
                loadedJsonString = "";
            }

            else if(loadedJsonString != null){
                loadedJsonString = loadedJsonString.replace("[", "").replace("]", "");
                ArrayList<String> inputStringList = new ArrayList(Arrays.asList(loadedJsonString.split(",")));

                for (int i = 0; i < inputStringList.size(); ++i) {
                    int number = Integer.parseInt(inputStringList.get(i));
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
            userTargetCalories.setText(String.valueOf(userTargetCaloriesVal));
            userTargetFat.setText(String.valueOf(userTargetFatVal));
            userTargetCarbs.setText(String.valueOf(userTargetCarbsVal));
            userTargetProtein.setText(String.valueOf(userTargetProteinVal));
        }

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
