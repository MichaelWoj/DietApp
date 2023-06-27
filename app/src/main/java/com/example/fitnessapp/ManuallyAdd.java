package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class ManuallyAdd extends AppCompatActivity {

    private EditText caloriesManual, proteinManual, fatManual, carbsManual;
    private Button submit;
    private Switch db_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually_add);

        caloriesManual = findViewById(R.id.manualMealCalories);
        proteinManual = findViewById(R.id.manualMealProtein);
        fatManual = findViewById(R.id.manualMealFat);
        carbsManual = findViewById(R.id.manualMealCarbs);
        db_switch = findViewById(R.id.saveToDBSwitch);

        submit = findViewById(R.id.manualAddFood);
        submit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    if (caloriesManual.getText().toString().isEmpty() || proteinManual.getText().toString().isEmpty() || fatManual.getText().toString().isEmpty() || carbsManual.getText().toString().isEmpty()){
                        Toast.makeText(ManuallyAdd.this, "Please insert all info", Toast.LENGTH_SHORT).show();
                    }else{

                        Intent intent = new Intent(ManuallyAdd.this, MainActivity.class);

                        String calM = caloriesManual.getText().toString();
                        double caloriesManualVal = Double.parseDouble(calM);
                        intent.putExtra("caloriesManual", caloriesManualVal);

                        String protM = proteinManual.getText().toString();
                        double proteinManualVal = Double.parseDouble(protM);
                        intent.putExtra("proteinManual", proteinManualVal);

                        String fatM = fatManual.getText().toString();
                        double fatManualVal = Double.parseDouble(fatM);
                        intent.putExtra("fatManual", fatManualVal);

                        String carbsM = carbsManual.getText().toString();
                        double carbsManualVal = Double.parseDouble(carbsM);
                        intent.putExtra("carbsManual", carbsManualVal);

                        if(db_switch.isChecked()){
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(ManuallyAdd.this);

                        }

                        setResult(RESULT_OK, intent);
                        finish();
                    }

        }
        });
    }
}