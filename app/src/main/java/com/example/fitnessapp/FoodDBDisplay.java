package com.example.fitnessapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class FoodDBDisplay extends AppCompatActivity implements RecyclerViewInterface {

    private ArrayList<String> foodID, foodNameDB, foodCaloriesNum, foodFatNum, foodCarbsNum, foodProteinNum, foodDisplayWeightNum, foodSaveType;
    private DatabaseHelper dataBaseHelper;
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

        searchView.setOnClickListener(v -> searchView.onActionViewExpanded());

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
        foodDisplayWeightNum = new ArrayList<>();
        foodSaveType = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewFoodList);
        adapter = new FoodDBRecycleViewAdapter(this,foodNameDB, foodCaloriesNum, foodFatNum, foodCarbsNum, foodProteinNum, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadSortData();
        displayData(sortType);

        Button addBtn = findViewById(R.id.dbAddFood);
        addBtn.setOnClickListener(v -> {
            showAddItemChoice();
        });

        Button cancelBtn = findViewById(R.id.dbCancel);
        cancelBtn.setOnClickListener(v -> finish());

        ImageButton sort = findViewById(R.id.dbSort);
        sort.setOnClickListener(v -> showSortDialog());
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = null;
        if(foodSaveType.get(position).equals("0")){
            intent = new Intent(FoodDBDisplay.this, FoodDBItemPage.class);
        } else if (foodSaveType.get(position).equals("1")) {
            intent = new Intent(FoodDBDisplay.this, FoodDBVariableWeightItemPage.class);
        }
        intent.putExtra("ID", foodID.get(position));
        intent.putExtra("Name", foodNameDB.get(position));
        intent.putExtra("Calories", foodCaloriesNum.get(position));
        intent.putExtra("Fat", foodFatNum.get(position));
        intent.putExtra("Carbs", foodCarbsNum.get(position));
        intent.putExtra("Protein", foodProteinNum.get(position));
        intent.putExtra("DisplayWeight", foodDisplayWeightNum.get(position));

        startForMainActivityResult.launch(intent);
    }
// Filters the DB entries for the searched for entry and puts all to lower so it ignores case
    private void fileList(String searchText) {
        ArrayList<String> filteredList = new ArrayList<>();
        for(String item : foodNameDB){
            if (item.toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filteredList(filteredList);
    }

    private void displayData(int sortType) {
        Cursor cursor =  dataBaseHelper.getAllFoods(sortType);
        if(cursor.getCount()==0){
            Toast.makeText(FoodDBDisplay.this, "No Entry Found", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursor.moveToNext()){
                foodID.add(cursor.getString(0));
                foodNameDB.add(cursor.getString(1));
                foodCaloriesNum.add(cursor.getString(2));
                foodFatNum.add(cursor.getString(3));
                foodCarbsNum.add(cursor.getString(4));
                foodProteinNum.add(cursor.getString(5));
                foodDisplayWeightNum.add(cursor.getString(6));
                foodSaveType.add(cursor.getString(7));

            }
        }
    }

    private void clearRecycleView(){
        foodNameDB.clear();
        foodCaloriesNum.clear();
        foodFatNum.clear();
        foodCarbsNum.clear();
        foodProteinNum.clear();
        foodDisplayWeightNum.clear();
        foodSaveType.clear();
    }

    //This is used in some cases instead of updateRecyclerView as updateRV had issues with ID's when a food was added until FoodDBDisplay was restarted
    private void recreateDisplay(){
        this.recreate();
    }
    
    private void updateRecyclerView(){
        clearRecycleView();
        adapter.notifyDataSetChanged();
        displayData(sortType);
    }

    ActivityResultLauncher<Intent> startForRefresh = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                recreateDisplay();
            }
        }
    });

    ActivityResultLauncher<Intent> startForMainActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Intent intentFromItemPage = result.getData();
                Bundle bundledIntentFromItemPage = intentFromItemPage.getExtras();

                Intent intent = new Intent(FoodDBDisplay.this, MainActivity.class).putExtras(bundledIntentFromItemPage);

                setResult(RESULT_OK, intent);
                finish();

            }
            else if(result.getResultCode() == Activity.RESULT_CANCELED){
                recreateDisplay();
            }
        }
    });
    //Saves the sort type the user used so it stays between activities and app launches
    private void sortSharedPreferences() {
        SharedPreferences sortSharedPreferences = getSharedPreferences("SORT_SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor sortEditor = sortSharedPreferences.edit();

        sortEditor.putInt(savedSearchType, sortType);
        sortEditor.apply();
    }

    public void loadSortData() {
        SharedPreferences sortSharedPreferences = getSharedPreferences("SORT_SHARED_PREFS", MODE_PRIVATE);

        sortType = sortSharedPreferences.getInt(savedSearchType,1);
    }

    private void showAddItemChoice(){
        final Dialog confirmationDialog = new Dialog(this);
        confirmationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmationDialog.setContentView(R.layout.activity_add_item_popup);

        LinearLayout setWeightAdd = confirmationDialog.findViewById(R.id.layoutSetItemAdd);
        LinearLayout variableWeightAdd = confirmationDialog.findViewById(R.id.layoutVariableWeighItemAdd);

        setWeightAdd.setOnClickListener(v -> {
            Intent intent = new Intent(FoodDBDisplay.this, FoodDBAddItem.class);
            startForRefresh.launch(intent);
            confirmationDialog.dismiss();
        });
        variableWeightAdd.setOnClickListener(v -> {
            Intent intent = new Intent(FoodDBDisplay.this, FoodDBAddItemVariableWeight.class);
            startForRefresh.launch(intent);
            confirmationDialog.dismiss();
        });

        confirmationDialog.show();
        confirmationDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        confirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmationDialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void showSortDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_db_sort_popup);

        LinearLayout dateSortLayout = dialog.findViewById(R.id.layoutSortEntryDate);
        LinearLayout alphabeticSortLayout = dialog.findViewById(R.id.layoutSortAlphabetic);
        LinearLayout calorieSortLayout = dialog.findViewById(R.id.layoutSortCalories);
        LinearLayout fatSortLayout = dialog.findViewById(R.id.layoutSortFat);
        LinearLayout carbsSortLayout = dialog.findViewById(R.id.layoutSortCarbs);
        LinearLayout proteinSortLayout = dialog.findViewById(R.id.layoutSortProtein);


        dateSortLayout.setOnClickListener(v -> {
            showSortAscOrDescDialog(1, 2);
            dialog.dismiss();

        });

        alphabeticSortLayout.setOnClickListener(v -> {
            showSortAscOrDescDialog(3, 4);
            dialog.dismiss();
        });

        calorieSortLayout.setOnClickListener(v -> {
            showSortAscOrDescDialog(5, 6);
            dialog.dismiss();

        });

        fatSortLayout.setOnClickListener(v -> {
            showSortAscOrDescDialog(7, 8);
            dialog.dismiss();
        });

        carbsSortLayout.setOnClickListener(v -> {
            showSortAscOrDescDialog(9, 10);
            dialog.dismiss();

        });

        proteinSortLayout.setOnClickListener(v -> {
            showSortAscOrDescDialog(11, 12);
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void showSortAscOrDescDialog(Integer ascSort, Integer descSort) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_db_sort_asc_or_desc_popup);

        LinearLayout ascSortLayout = dialog.findViewById(R.id.layoutSortAsc);
        LinearLayout descSortLayout = dialog.findViewById(R.id.layoutSortDesc);


        ascSortLayout.setOnClickListener(v -> {
            sortType = ascSort;
            sortSharedPreferences();
            updateRecyclerView();
            dialog.dismiss();
        });

        descSortLayout.setOnClickListener(v -> {
            sortType = descSort;
            sortSharedPreferences();
            updateRecyclerView();
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}