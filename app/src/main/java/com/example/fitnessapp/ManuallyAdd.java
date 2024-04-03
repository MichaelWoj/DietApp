package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ManuallyAdd extends AppCompatActivity {

    private EditText foodName, foodCalories, foodFat, foodCarbs, foodProtein;
    private TextView tvMealName;
    private DietCalendar dietCalendar;
    private Switch db_switch;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually_add);

        dietCalendar = new DietCalendar();

        tvMealName = findViewById(R.id.manualMealNameTV);
        foodName = findViewById(R.id.manualMealName);
        foodCalories = findViewById(R.id.manualMealCalories);
        foodFat = findViewById(R.id.manualMealFat);
        foodCarbs = findViewById(R.id.manualMealCarbs);
        foodProtein = findViewById(R.id.manualMealProtein);
        db_switch = findViewById(R.id.saveToDBSwitch);
        Button submit = findViewById(R.id.manualAddFood);

        submit.setOnClickListener(view -> {

            if ( foodName.getText().toString().isEmpty()||foodCalories.getText().toString().isEmpty() || foodFat.getText().toString().isEmpty() || foodCarbs.getText().toString().isEmpty() || foodProtein.getText().toString().isEmpty()) {
                Toast.makeText(ManuallyAdd.this, "Please insert all info", Toast.LENGTH_SHORT).show();
            }else{

                Intent intent = new Intent(ManuallyAdd.this, MainActivity.class);

                String foodNameToString = foodName.getText().toString();

                String foodCaloriesToString = foodCalories.getText().toString();
                float foodCaloriesVal = Float.parseFloat(foodCaloriesToString);
                intent.putExtra("foodCalories", foodCaloriesVal);

                String foodFatToString = foodFat.getText().toString();
                float foodFatVal = Float.parseFloat(foodFatToString);
                intent.putExtra("foodFat", foodFatVal);

                String foodCarbsToString = foodCarbs.getText().toString();
                float foodCarbsVal = Float.parseFloat(foodCarbsToString);
                intent.putExtra("foodCarbs", foodCarbsVal);

                String foodProteinToString = foodProtein.getText().toString();
                float foodProteinVal = Float.parseFloat(foodProteinToString);
                intent.putExtra("foodProtein", foodProteinVal);


                if(db_switch.isChecked()){

                    FoodModel foodModel;

                    try{
                        foodModel = new FoodModel(-1, foodNameToString, foodCaloriesVal, foodFatVal, foodCarbsVal, foodProteinVal, 0,0);
                    }
                    catch (Exception e){
                        foodModel = new FoodModel(-1,"Error",0f,0f,0f,0f, 0,0);
                    }

                    DatabaseHelper dataBaseHelper = new DatabaseHelper(ManuallyAdd.this);
                    boolean success = dataBaseHelper.addOne(foodModel);
                }

                CalendarFoodModel calendarFoodModel;

                String date = dietCalendar.getCurrentDate();
                String time = dietCalendar.getCurrentTime();

                try {
                    calendarFoodModel = new CalendarFoodModel(-1, foodNameToString, foodCaloriesVal, foodFatVal, foodCarbsVal, foodProteinVal, 0f, date, time);
                } catch (Exception e) {
                    calendarFoodModel = new CalendarFoodModel(-1, "Error", 0f, 0f, 0f, 0f, 0f,"Error", "Error");
                }

                DatabaseHelper dataBaseHelperForCalendar = new DatabaseHelper(ManuallyAdd.this);
                dataBaseHelperForCalendar.calendarAddOne(calendarFoodModel);

                setResult(RESULT_OK, intent);
                finish();
            }

});
        Button back = findViewById(R.id.manualCancel);
        back.setOnClickListener(v -> finish());
    }
}