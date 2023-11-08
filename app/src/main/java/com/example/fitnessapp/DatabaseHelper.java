package com.example.fitnessapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String FOOD_TABLE = "FOOD_TABLE";
    public static final String COLUMN_FOOD_NAME = "FOOD_NAME";
    public static final String COLUMN_FOOD_CALORIES = "FOOD_CALORIES";
    public static final String COLUMN_FOOD_FAT = "FOOD_FAT";
    public static final String COLUMN_FOOD_CARBS = "FOOD_CARBS";
    public static final String COLUMN_FOOD_PROTEIN = "FOOD_PROTEIN";

    private String queryString;

    public DatabaseHelper(@Nullable Context context) {
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

    public Cursor getAllFoods(int sortType){

        switch (sortType){
            case 1:
                queryString = "SELECT * FROM " +FOOD_TABLE;
                break;
            case 2:
                queryString = "SELECT * FROM "+FOOD_TABLE+" ORDER BY ID DESC";
                break;
            case 3:
                queryString = "SELECT * FROM "+FOOD_TABLE+" ORDER BY "+COLUMN_FOOD_NAME+" ASC";
                break;
            case 4:
                queryString = "SELECT * FROM "+FOOD_TABLE+" ORDER BY "+COLUMN_FOOD_NAME+" DESC";
                break;
            case 5:
                queryString = "SELECT * FROM "+FOOD_TABLE+" ORDER BY "+COLUMN_FOOD_CALORIES+" ASC";
                break;
            case 6:
                queryString = "SELECT * FROM "+FOOD_TABLE+" ORDER BY "+COLUMN_FOOD_CALORIES+" DESC";
                break;
            case 7:
                queryString = "SELECT * FROM "+FOOD_TABLE+" ORDER BY "+COLUMN_FOOD_CARBS+" ASC";
                break;
            case 8:
                queryString = "SELECT * FROM "+FOOD_TABLE+" ORDER BY "+COLUMN_FOOD_CARBS+" DESC";
                break;
            case 9:
                queryString = "SELECT * FROM "+FOOD_TABLE+" ORDER BY "+COLUMN_FOOD_FAT+" ASC";
                break;
            case 10:
                queryString = "SELECT * FROM "+FOOD_TABLE+" ORDER BY "+COLUMN_FOOD_FAT+" DESC";
                break;
            case 11:
                queryString = "SELECT * FROM "+FOOD_TABLE+" ORDER BY "+COLUMN_FOOD_PROTEIN+" ASC";
                break;
            case 12:
                queryString = "SELECT * FROM "+FOOD_TABLE+" ORDER BY "+COLUMN_FOOD_PROTEIN+" DESC";
                break;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        return cursor;
    }

    public void editEntry(int id, String name, float calories, float protein, float fat, float carbs){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FOOD_NAME, name);
        values.put(COLUMN_FOOD_CALORIES, calories);
        values.put(COLUMN_FOOD_PROTEIN, protein);
        values.put(COLUMN_FOOD_FAT, fat);
        values.put(COLUMN_FOOD_CARBS,carbs);

        db.update(FOOD_TABLE, values, "id = "+ id, null);
    }

    public void deleteEntry(int foodId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FOOD_TABLE,"id = "+ foodId,null);

    }
}
