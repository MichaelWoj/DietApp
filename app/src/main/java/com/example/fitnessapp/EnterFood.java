package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class EnterFood extends AppCompatActivity {

     ListView lv_foodList;
     ArrayAdapter foodArrayAdapter;
     DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_food);

        lv_foodList = findViewById(R.id.lv_foodList);

        dataBaseHelper = new DataBaseHelper(EnterFood.this);

        ShowFoodOnRecycleView();


    }

    private void ShowFoodOnRecycleView() {
        foodArrayAdapter = new ArrayAdapter<FoodModel>(EnterFood.this, android.R.layout.simple_list_item_1, dataBaseHelper.getAllFoods());
        lv_foodList.setAdapter(foodArrayAdapter);
    }
}