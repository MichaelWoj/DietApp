package com.example.fitnessapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FoodDBDisplay extends AppCompatActivity implements RecyclerViewInterface {

    private RecyclerView recyclerView;
    private ArrayList<String> foodID, foodNameDB, foodCaloriesNum, foodFatNum, foodCarbsNum, foodProteinNum;
    private DatabaseHelper dataBaseHelper;
    private Button addBtn, cancelBtn;
    private SearchView searchView;

    public static final String savedSearchType = "search_type";
    public FoodDBRecycleViewAdapter adapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_food);

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        searchView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fileList(newText);
                return true;
            }
        });

        dataBaseHelper = new DatabaseHelper(FoodDBDisplay.this);

        foodID = new ArrayList<>();
        foodNameDB = new ArrayList<>();
        foodCaloriesNum = new ArrayList<>();
        foodFatNum = new ArrayList<>();
        foodCarbsNum = new ArrayList<>();
        foodProteinNum = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewFoodList);
        adapter = new FoodDBRecycleViewAdapter(this,foodNameDB, foodCaloriesNum, foodFatNum, foodCarbsNum, foodProteinNum, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayData(1);

        addBtn = (Button) findViewById(R.id.dbAddFood);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDBDisplay.this, FoodDBAddItem.class);

                startForRefresh.launch(intent);
            }
        });

        cancelBtn = (Button) findViewById(R.id.dbCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fileList(String searchText) {
        ArrayList<String> filteredList = new ArrayList<>();
        for(String item : foodNameDB){
            if (item.toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(item);
            };
        }
        adapter.filteredList(filteredList);
    }

    // In order to avoid errors, the variable name of displayData is also the default sort method name.
    private void displayData(int sortType) {
        Cursor cursor = (Cursor) dataBaseHelper.getAllFoods(sortType);
        if(cursor.getCount()==0){
            Toast.makeText(FoodDBDisplay.this, "No Entry Found", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            while (cursor.moveToNext()){
                foodID.add(cursor.getString(0));
                foodNameDB.add(cursor.getString(1));
                foodCaloriesNum.add(cursor.getString(2));
                foodFatNum.add(cursor.getString(3));
                foodCarbsNum.add(cursor.getString(4));
                foodProteinNum.add(cursor.getString(5));

            }
        }
    }
    ActivityResultLauncher<Intent> startForRefresh = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                adapter.notifyDataSetChanged();
                //displayData();
            }
        }
    });

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(FoodDBDisplay.this, FoodDBItemPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra("ID", foodID.get(position));
        intent.putExtra("Name", foodNameDB.get(position));
        intent.putExtra("Calories", foodCaloriesNum.get(position));
        intent.putExtra("Fat", foodFatNum.get(position));
        intent.putExtra("Carbs", foodCarbsNum.get(position));
        intent.putExtra("Protein", foodProteinNum.get(position));

        startActivity(intent);
    }
}