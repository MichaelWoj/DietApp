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

    private EditText nameManual, caloriesManual, fatManual, carbsManual, proteinManual;
    private Button submit;
    private Switch db_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually_add);

        nameManual = findViewById(R.id.manualMealName);
        caloriesManual = findViewById(R.id.manualMealCalories);
        fatManual = findViewById(R.id.manualMealFat);
        carbsManual = findViewById(R.id.manualMealCarbs);
        proteinManual = findViewById(R.id.manualMealProtein);
        db_switch = findViewById(R.id.saveToDBSwitch);

        submit = findViewById(R.id.manualAddFood);
        submit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    if (caloriesManual.getText().toString().isEmpty() || fatManual.getText().toString().isEmpty() || carbsManual.getText().toString().isEmpty() || proteinManual.getText().toString().isEmpty()){
                        Toast.makeText(ManuallyAdd.this, "Please insert all info", Toast.LENGTH_SHORT).show();
                    }else{

                        Intent intent = new Intent(ManuallyAdd.this, MainActivity.class);

                        String nameM = nameManual.getText().toString();

                        String calM = caloriesManual.getText().toString();
                        double caloriesManualVal = Double.parseDouble(calM);
                        intent.putExtra("caloriesManual", caloriesManualVal);

                        String fatM = fatManual.getText().toString();
                        double fatManualVal = Double.parseDouble(fatM);
                        intent.putExtra("fatManual", fatManualVal);

                        String carbsM = carbsManual.getText().toString();
                        double carbsManualVal = Double.parseDouble(carbsM);
                        intent.putExtra("carbsManual", carbsManualVal);

                        String protM = proteinManual.getText().toString();
                        double proteinManualVal = Double.parseDouble(protM);
                        intent.putExtra("proteinManual", proteinManualVal);

                        if(db_switch.isChecked()){

                            FoodModel foodModel;

                            try{
                                foodModel = new FoodModel(-1, nameM, caloriesManualVal, fatManualVal, carbsManualVal,proteinManualVal);
                                Toast.makeText(ManuallyAdd.this, foodModel.toString(), Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e){
                                Toast.makeText(ManuallyAdd.this, "Error adding food to the database", Toast.LENGTH_SHORT).show();
                                foodModel = new FoodModel(-1,"Error",0.0,0.0,0.0,0.0);
                            }

                            DataBaseHelper dataBaseHelper = new DataBaseHelper(ManuallyAdd.this);
                            boolean success = dataBaseHelper.addOne(foodModel);
                            Toast.makeText(ManuallyAdd.this, "Success = " + success, Toast.LENGTH_SHORT).show();
                        }

                        setResult(RESULT_OK, intent);
                        finish();
                    }

        }
        });
    }
}