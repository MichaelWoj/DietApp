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
    private Button submit, back;
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

                    if (nameManual.getText().toString().isEmpty()||caloriesManual.getText().toString().isEmpty() || fatManual.getText().toString().isEmpty() || carbsManual.getText().toString().isEmpty() || proteinManual.getText().toString().isEmpty()){
                        Toast.makeText(ManuallyAdd.this, "Please insert all info", Toast.LENGTH_SHORT).show();
                    }else{

                        Intent intent = new Intent(ManuallyAdd.this, MainActivity.class);

                        String nameM = nameManual.getText().toString();

                        String calM = caloriesManual.getText().toString();
                        float caloriesManualVal = Float.parseFloat(calM);
                        intent.putExtra("caloriesManual", caloriesManualVal);

                        String fatM = fatManual.getText().toString();
                        float fatManualVal = Float.parseFloat(fatM);
                        intent.putExtra("fatManual", fatManualVal);

                        String carbsM = carbsManual.getText().toString();
                        float carbsManualVal = Float.parseFloat(carbsM);
                        intent.putExtra("carbsManual", carbsManualVal);

                        String protM = proteinManual.getText().toString();
                        float proteinManualVal = Float.parseFloat(protM);
                        intent.putExtra("proteinManual", proteinManualVal);

                        if(db_switch.isChecked()){

                            FoodModel foodModel;

                            try{
                                foodModel = new FoodModel(-1, nameM, caloriesManualVal, fatManualVal, carbsManualVal,proteinManualVal);
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