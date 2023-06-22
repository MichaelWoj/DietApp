package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ManuallyAdd extends AppCompatActivity {

    private EditText caloriesManual, proteinManual, fatManual, carbsManual;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually_add);

        caloriesManual = findViewById(R.id.manualMealCalories);
        proteinManual = findViewById(R.id.manualMealProtein);
        fatManual = findViewById(R.id.manualMealFat);
        carbsManual = findViewById(R.id.manualMealCarbs);

        submit = findViewById(R.id.manualAddFood);
        submit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    if (caloriesManual.getText().toString().isEmpty() || proteinManual.getText().toString().isEmpty() || fatManual.getText().toString().isEmpty() || carbsManual.getText().toString().isEmpty()){
                        Toast.makeText(ManuallyAdd.this, "Please insert all info", Toast.LENGTH_SHORT).show();
                    }else{

                        Intent intent = new Intent(ManuallyAdd.this, MainActivity.class);

                        String calM = caloriesManual.getText().toString();
                        double caloriesManualInt = Double.parseDouble(calM);
                        intent.putExtra("caloriesManual", caloriesManualInt);

                        String protM = proteinManual.getText().toString();
                        double proteinManualInt = Double.parseDouble(protM);
                        intent.putExtra("proteinManual", proteinManualInt);

                        String fatM = fatManual.getText().toString();
                        double fatManualInt = Double.parseDouble(fatM);
                        intent.putExtra("fatManual", fatManualInt);

                        String carbsM = carbsManual.getText().toString();
                        double carbsManualInt = Double.parseDouble(carbsM);
                        intent.putExtra("carbsManual", carbsManualInt);

                        setResult(RESULT_OK, intent);
                        finish();
                    }

        }
        });
    }
}