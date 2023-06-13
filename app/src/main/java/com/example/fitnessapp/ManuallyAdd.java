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
        caloriesManual.setInputType(InputType.TYPE_CLASS_NUMBER);

        proteinManual = findViewById(R.id.manualMealProtein);
        proteinManual.setInputType(InputType.TYPE_CLASS_NUMBER);

        fatManual = findViewById(R.id.manualMealFat);
        fatManual.setInputType(InputType.TYPE_CLASS_NUMBER);

        carbsManual = findViewById(R.id.manualMealCarbs);
        carbsManual.setInputType(InputType.TYPE_CLASS_NUMBER);

        submit = findViewById(R.id.manualAddFood);
        submit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    if (caloriesManual.getText().toString().equals("") || proteinManual.getText().toString().equals("") || fatManual.getText().toString().equals("") || carbsManual.getText().toString().equals("")){
                        Toast.makeText(ManuallyAdd.this, "Please insert all info", Toast.LENGTH_SHORT).show();
                    }else{

                        Intent intent = new Intent(ManuallyAdd.this, MainActivity.class);

                        String calM = caloriesManual.getText().toString();
                        int caloriesManualInt = Integer.parseInt(calM);
                        intent.putExtra("caloriesManual", caloriesManualInt);

                        String protM = proteinManual.getText().toString();
                        int proteinManualInt = Integer.parseInt(protM);
                        intent.putExtra("proteinManual", proteinManualInt);

                        String fatM = fatManual.getText().toString();
                        int fatManualInt = Integer.parseInt(fatM);
                        intent.putExtra("fatManual", fatManualInt);

                        String carbsM = fatManual.getText().toString();
                        int carbsManualInt = Integer.parseInt(carbsM);
                        intent.putExtra("carbsManual", carbsManualInt);

                        finish();
                    }

        }
        });
    }
}