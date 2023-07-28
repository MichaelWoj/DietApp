package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EnterFood extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> foodName, foodCaloriesNum, foodFatNum, foodCarbsNum, foodProteinNum;
    DataBaseHelper dataBaseHelper;
    FdbRecycleViewAdapter adapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_food);
        dataBaseHelper = new DataBaseHelper(EnterFood.this);

        foodName = new ArrayList<>();
        foodCaloriesNum = new ArrayList<>();
        foodFatNum = new ArrayList<>();
        foodCarbsNum = new ArrayList<>();
        foodProteinNum = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewFoodList);
        adapter = new FdbRecycleViewAdapter(this, foodName, foodCaloriesNum, foodFatNum, foodCarbsNum, foodProteinNum);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayData();
    }

    private void displayData() {
        Cursor cursor = (Cursor) dataBaseHelper.getAllFoods();
        if(cursor.getCount()==0){
            Toast.makeText(EnterFood.this, "No Entry Found", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            while (cursor.moveToNext()){
                foodName.add(cursor.getString(1));
                foodCaloriesNum.add(cursor.getString(2));
                foodFatNum.add(cursor.getString(3));
                foodCarbsNum.add(cursor.getString(4));
                foodProteinNum.add(cursor.getString(5));

            }
        }
    }
}