package com.example.fitnessapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDBVariableWeightPopup extends AppCompatActivity {
    private EditText variableDisplayWeight;
    public int selectedDisplayWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public int selectDisplayWeightWindow(Context context, Integer currentDisplayWeight) {

        final Dialog displayWeightDialog = new Dialog(context);
        displayWeightDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        displayWeightDialog.setContentView(R.layout.activity_db_variable_weight_popup);
        int tempSelectedDisplayWeight = currentDisplayWeight;
        selectedDisplayWeight = currentDisplayWeight;

        variableDisplayWeight = displayWeightDialog.findViewById(R.id.displayWeightAmount);
        if (currentDisplayWeight > 0) {
            variableDisplayWeight.setText(Integer.toString(selectedDisplayWeight));
        }

        LinearLayout confirmDisplayWeight = displayWeightDialog.findViewById(R.id.layoutConfirmDisplayWeight);
        LinearLayout cancelDisplayWeight = displayWeightDialog.findViewById(R.id.layoutCancelDisplayWeight);

        confirmDisplayWeight.setOnClickListener(v -> {
            if (selectedDisplayWeight == 0){
                Toast.makeText(FoodDBVariableWeightPopup.this, "Display weight cannot be empty or equal to 0.", Toast.LENGTH_SHORT).show();
            }else {
                String variableFoodWeightToString = variableDisplayWeight.getText().toString();
                selectedDisplayWeight = Integer.parseInt(variableFoodWeightToString);
                displayWeightDialog.dismiss();
            }

        });
        cancelDisplayWeight.setOnClickListener(v -> {
            selectedDisplayWeight = tempSelectedDisplayWeight;
            displayWeightDialog.dismiss();
        });

        displayWeightDialog.show();
        displayWeightDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        displayWeightDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        displayWeightDialog.getWindow().setGravity(Gravity.CENTER);
        return selectedDisplayWeight;
    }

    public void actualCheckRadioButtonId(View view){
        switch (view.getId()) {
            case R.id.displayWeight100g:
                selectedDisplayWeight = 100;
                variableDisplayWeight.setText(Integer.toString(selectedDisplayWeight));
                break;
            case R.id.displayWeight50g:
                selectedDisplayWeight = 50;
                variableDisplayWeight.setText(Integer.toString(selectedDisplayWeight));
                break;
            case R.id.displayWeight10g:
                selectedDisplayWeight = 10;
                variableDisplayWeight.setText(Integer.toString(selectedDisplayWeight));
                break;
            case R.id.displayWeight1g:
                selectedDisplayWeight = 1;
                variableDisplayWeight.setText(Integer.toString(selectedDisplayWeight));
                break;
        }
    }
}
