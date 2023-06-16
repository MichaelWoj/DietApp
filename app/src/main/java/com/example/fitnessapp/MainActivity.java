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

    int caloriesVal = 0;
    int proteinVal = 0;
    int fatVal = 0;
    int carbsVal = 0;

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Intent intent = result.getData();
                if(intent != null){
                    int caloriesManualInt = intent.getIntExtra("caloriesManual", 0);
                    setCalories.setText(String.valueOf(caloriesManualInt));
                    int proteinManualInt = intent.getIntExtra("proteinManual", 0);
                    setProtein.setText(String.valueOf(proteinManualInt));
                    int fatManualInt = intent.getIntExtra("fatManual", 0);
                    setFat.setText(String.valueOf(caloriesManualInt));
                    int carbsManualInt = intent.getIntExtra("carbsManual", 0);
                    setCarbs.setText(String.valueOf(carbsManualInt));
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
                openFoodSearch();
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
    }
    public void openFoodSearch(){
        Intent intent = new Intent(this, EnterFood.class);
        startActivity(intent);
    }
    public void openManualAdd(){
        Intent intent = new Intent(this, ManuallyAdd.class);
        startActivity(intent);
    }
}
