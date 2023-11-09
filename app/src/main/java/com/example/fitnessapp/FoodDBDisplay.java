package com.example.fitnessapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FoodDBDisplay extends AppCompatActivity implements RecyclerViewInterface {

    private RecyclerView recyclerView;
    private ArrayList<String> foodID, foodNameDB, foodCaloriesNum, foodFatNum, foodCarbsNum, foodProteinNum;
    private DatabaseHelper dataBaseHelper;
    private Button addBtn, cancelBtn;
    private ImageButton sort;
    private SearchView searchView;

    private int sortType;

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
        loadSortData();
        displayData(sortType);

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

        sort = findViewById(R.id.dbSort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();

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
                displayData(sortType);
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

    private void sortSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("SORT_SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor sortEditor = sharedPreferences.edit();

        sortEditor.putInt(savedSearchType, sortType);
        sortEditor.apply();
    }

    public void loadSortData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);

        sortType = sharedPreferences.getInt(savedSearchType,1);
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_db_sort_popup);

        LinearLayout dateSortLayout = dialog.findViewById(R.id.layoutSortEntryDate);
        LinearLayout alphabeticSortLayout = dialog.findViewById(R.id.layoutSortAlphabetic);


        dateSortLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        alphabeticSortLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}