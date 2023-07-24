package com.example.fitnessapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String FOOD_TABLE = "FOOD_TABLE";
    public static final String COLUMN_FOOD_NAME = "FOOD_NAME";
    public static final String COLUMN_FOOD_CALORIES = "FOOD_CALORIES";
    public static final String COLUMN_FOOD_FAT = "FOOD_FAT";
    public static final String COLUMN_FOOD_CARBS = "FOOD_CARBS";
    public static final String COLUMN_FOOD_PROTEIN = "FOOD_PROTEIN";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "food.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + FOOD_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FOOD_NAME + " TEXT, " + COLUMN_FOOD_CALORIES + " DOUBLE, " + COLUMN_FOOD_CARBS + " DOUBLE, " + COLUMN_FOOD_FAT + " DOUBLE, " + COLUMN_FOOD_PROTEIN + " DOUBLE)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + FOOD_TABLE);
        onCreate(db);
    }

    public boolean addOne(FoodModel foodModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FOOD_NAME, foodModel.getName());
        cv.put(COLUMN_FOOD_CALORIES, foodModel.getCalories());
        cv.put(COLUMN_FOOD_FAT, foodModel.getFat());
        cv.put(COLUMN_FOOD_CARBS, foodModel.getCarbs());
        cv.put(COLUMN_FOOD_PROTEIN, foodModel.getProtein());


        long insert = db.insert(FOOD_TABLE, null, cv);
        if (insert == -1)
            return false;

        else
            return true;

    };
    public List<FoodModel> getAllFoods(){
        List<FoodModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + FOOD_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do{
                int foodID = cursor.getInt(0);
                String foodName = cursor.getString(1);
                double foodCalories = cursor.getDouble(2);
                double foodFat = cursor.getDouble(3);
                double foodCarbs = cursor.getDouble(4);
                double foodProtein = cursor.getDouble(5);

                FoodModel newFood = new FoodModel(foodID, foodName, foodCalories, foodFat, foodCarbs, foodProtein);
                returnList.add(newFood);

            }while (cursor.moveToNext());
        }
        else {

        }
        cursor.close();
        db.close();
        return returnList;
    }
}
