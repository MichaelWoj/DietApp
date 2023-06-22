package com.example.fitnessapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

    public class MainActivity extends AppCompatActivity{
    private TextView setCalories, setProtein, setFat, setCarbs ;
    private Button addFoodBtn, manualFoodBtn, undoFoodBtn, resetAllBtn;

    double caloriesVal = 0;
    double proteinVal = 0;
    double fatVal = 0;
    double carbsVal = 0;

    double caloriesManualInt = 0;
    double proteinManualInt = 0;
    double fatManualInt = 0;
    double carbsManualInt = 0;


    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Intent intent = result.getData();
                if(intent != null){
                    caloriesManualInt = intent.getDoubleExtra("caloriesManual", 0);
                    caloriesVal = caloriesVal + caloriesManualInt;

                    proteinManualInt = intent.getDoubleExtra("proteinManual", 0);
                    proteinVal = proteinVal + proteinManualInt;

                    fatManualInt = intent.getDoubleExtra("fatManual", 0);
                    fatVal = fatVal + fatManualInt;

                    carbsManualInt = intent.getDoubleExtra("carbsManual", 0);
                    carbsVal = carbsVal + carbsManualInt;

                    setValues();
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Intent intent = new Intent(MainActivity.this, EnterFood.class);
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
                caloriesVal = caloriesVal- caloriesManualInt;
                proteinVal = proteinVal- proteinManualInt;
                fatVal = fatVal- fatManualInt;
                carbsVal = carbsVal- carbsManualInt;

                setValues();

                caloriesManualInt = 0;
                proteinManualInt = 0;
                fatManualInt = 0;
                carbsManualInt = 0;
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

            }
        });
    }

        private void setValues() {
            setCalories.setText(String.valueOf(caloriesVal));
            setProtein.setText(String.valueOf(proteinVal));
            setFat.setText(String.valueOf(fatVal));
            setCarbs.setText(String.valueOf(carbsVal));
        }

    }
