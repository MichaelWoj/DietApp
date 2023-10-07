package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class ManuallyAdd extends AppCompatActivity {

    private EditText foodName, foodCalories, foodFat, foodCarbs, foodProtein;
    private Button submit, back;
    private Switch db_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually_add);

        foodName = findViewById(R.id.manualMealName);
        foodCalories = findViewById(R.id.manualMealCalories);
        foodFat = findViewById(R.id.manualMealFat);
        foodCarbs = findViewById(R.id.manualMealCarbs);
        foodProtein = findViewById(R.id.manualMealProtein);
        db_switch = findViewById(R.id.saveToDBSwitch);

        submit = findViewById(R.id.manualAddFood);
        submit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    if (foodName.getText().toString().isEmpty()||foodCalories.getText().toString().isEmpty() || foodFat.getText().toString().isEmpty() || foodCarbs.getText().toString().isEmpty() || foodProtein.getText().toString().isEmpty()){
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
                                foodModel = new FoodModel(-1, foodNameToString, foodCaloriesVal, foodFatVal, foodCarbsVal,foodProteinVal);
                            }
                            catch (Exception e){
                                foodModel = new FoodModel(-1,"Error",0f,0f,0f,0f);
                            }

                            DataBaseHelper dataBaseHelper = new DataBaseHelper(ManuallyAdd.this);
                            boolean success = dataBaseHelper.addOne(foodModel);
                        }

                        setResult(RESULT_OK, intent);
                        finish();
                    }

        }
        });
        back = findViewById(R.id.manualCancel);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}