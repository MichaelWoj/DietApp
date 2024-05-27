package com.example.fitnessapp;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String FOOD_TABLE = "FOOD_TABLE";
    public static final String COLUMN_FOOD_ID = "ID";
    public static final String COLUMN_FOOD_NAME = "FOOD_NAME";
    public static final String COLUMN_FOOD_CALORIES = "FOOD_CALORIES";
    public static final String COLUMN_FOOD_FAT = "FOOD_FAT";
    public static final String COLUMN_FOOD_CARBS = "FOOD_CARBS";
    public static final String COLUMN_FOOD_PROTEIN = "FOOD_PROTEIN";
    public static final String COLUMN_FOOD_DISPLAY_WEIGHT = "FOOD_DISPLAY_WEIGHT";
    public static final String COLUMN_FOOD_VARIABLE_SAVE_TYPE = "FOOD_VARIABLE_SAVE_TYPE";

    public static final String CALENDAR_FOOD_TABLE = "CALENDAR_FOOD_TABLE";
    public static final String CALENDAR_COLUMN_FOOD_ID = "CALENDAR_ID";
    public static final String CALENDAR_COLUMN_FOOD_NAME = "CALENDAR_FOOD_NAME";
    public static final String CALENDAR_COLUMN_FOOD_CALORIES = "CALENDAR_FOOD_CALORIES";
    public static final String CALENDAR_COLUMN_FOOD_FAT = "CALENDAR_FOOD_FAT";
    public static final String CALENDAR_COLUMN_FOOD_CARBS = "CALENDAR_FOOD_CARBS";
    public static final String CALENDAR_COLUMN_FOOD_PROTEIN = "CALENDAR_FOOD_PROTEIN";
    public static final String CALENDAR_COLUMN_FOOD_WEIGHT = "CALENDAR_FOOD_WEIGHT";
    public static final String CALENDAR_DATE_ADDED = "CALENDAR_DATE_ADDED";
    public static final String CALENDAR_TIME_ADDED = "CALENDAR_TIME_ADDED";

    public static final String CALENDAR_WEIGHT_TABLE = "CALENDAR_WEIGHT_TABLE";
    public static final String CALENDAR_WEIGHT_ID = "CALENDAR_WEIGHT_ID";
    public static final String CALENDAR_WEIGHT_NUMBER = "CALENDAR_WEIGHT_NUMBER";
    public static final String CALENDAR_WEIGHT_DATE_ENTERED = "CALENDAR_WEIGHT_DATE_ENTERED";

    private static final int DATABASE_VERSION = 3;
    private String queryString;


    public DatabaseHelper(@Nullable Context context) {
        super(context, "food.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createFoodStorageTableStatement = "CREATE TABLE " + FOOD_TABLE + " (" + COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FOOD_NAME + " TEXT, " + COLUMN_FOOD_CALORIES + " DOUBLE, " + COLUMN_FOOD_FAT + " DOUBLE, " + COLUMN_FOOD_CARBS + " DOUBLE, " + COLUMN_FOOD_PROTEIN + " DOUBLE, " + COLUMN_FOOD_DISPLAY_WEIGHT + " INTEGER, " + COLUMN_FOOD_VARIABLE_SAVE_TYPE + " INTEGER)";
        db.execSQL(createFoodStorageTableStatement);

        String createCalendarFoodStorageTableStatement = "CREATE TABLE " + CALENDAR_FOOD_TABLE + " (" + CALENDAR_COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CALENDAR_COLUMN_FOOD_NAME + " TEXT, " + CALENDAR_COLUMN_FOOD_CALORIES + " DOUBLE, " + CALENDAR_COLUMN_FOOD_FAT + " DOUBLE, " + CALENDAR_COLUMN_FOOD_CARBS + " DOUBLE, " + CALENDAR_COLUMN_FOOD_PROTEIN + " DOUBLE, " + CALENDAR_COLUMN_FOOD_WEIGHT + " DOUBLE," + CALENDAR_DATE_ADDED + " TEXT, " + CALENDAR_TIME_ADDED + " TEXT)";
        db.execSQL(createCalendarFoodStorageTableStatement);

        String createCalendarWeightStorageTableStatement = "CREATE TABLE " + CALENDAR_WEIGHT_TABLE + " (" + CALENDAR_WEIGHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CALENDAR_WEIGHT_NUMBER + " DOUBLE, " + CALENDAR_WEIGHT_DATE_ENTERED  + " TEXT UNIQUE)";
        db.execSQL(createCalendarWeightStorageTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            while(oldVersion < newVersion){
                if(oldVersion == 1){
                    db.execSQL("ALTER TABLE " + FOOD_TABLE + " ADD COLUMN " + COLUMN_FOOD_DISPLAY_WEIGHT + " INTEGER;");
                    db.execSQL("ALTER TABLE " + FOOD_TABLE + " ADD COLUMN " + COLUMN_FOOD_VARIABLE_SAVE_TYPE + " INTEGER;");
                    db.execSQL("UPDATE " + FOOD_TABLE + " SET " + COLUMN_FOOD_VARIABLE_SAVE_TYPE + " = 0;");
                    oldVersion = oldVersion + 1;
                } else if (oldVersion == 2) {
                    db.execSQL("CREATE TABLE " + CALENDAR_FOOD_TABLE + " (" + CALENDAR_COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CALENDAR_COLUMN_FOOD_NAME + " TEXT, " + CALENDAR_COLUMN_FOOD_CALORIES + " DOUBLE, " + CALENDAR_COLUMN_FOOD_FAT + " DOUBLE, " + CALENDAR_COLUMN_FOOD_CARBS + " DOUBLE, " + CALENDAR_COLUMN_FOOD_PROTEIN + " DOUBLE, " + CALENDAR_COLUMN_FOOD_WEIGHT + " DOUBLE," + CALENDAR_DATE_ADDED + " TEXT, " + CALENDAR_TIME_ADDED + " TEXT)");
                    db.execSQL("CREATE TABLE " + CALENDAR_WEIGHT_TABLE + " (" + CALENDAR_WEIGHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CALENDAR_WEIGHT_NUMBER + " DOUBLE, " + CALENDAR_WEIGHT_DATE_ENTERED  + " TEXT UNIQUE)");
                    oldVersion = oldVersion + 1;
                }
            }
        }
    }

    //Everything below is for the " FOOD_TABLE " table in the DB
    public boolean addOne(FoodModel foodModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FOOD_NAME, foodModel.getName());
        cv.put(COLUMN_FOOD_CALORIES, foodModel.getCalories());
        cv.put(COLUMN_FOOD_FAT, foodModel.getFat());
        cv.put(COLUMN_FOOD_CARBS, foodModel.getCarbs());
        cv.put(COLUMN_FOOD_PROTEIN, foodModel.getProtein());
        cv.put(COLUMN_FOOD_DISPLAY_WEIGHT, foodModel.getDisplayWeight());
        cv.put(COLUMN_FOOD_VARIABLE_SAVE_TYPE, foodModel.getSaveType());


        long insert = db.insert(FOOD_TABLE, null, cv);
        if (insert == -1)
            return false;

        else
            return true;

    }

    public Cursor getAllFoods(int sortType) {

        switch (sortType) {
            case 1:
                queryString = "SELECT * FROM " + FOOD_TABLE + " ORDER BY " + COLUMN_FOOD_ID + " ASC";
                break;
            case 2:
                queryString = "SELECT * FROM " + FOOD_TABLE + " ORDER BY " + COLUMN_FOOD_ID + " DESC";
                break;
            case 3:
                queryString = "SELECT * FROM " + FOOD_TABLE + " ORDER BY " + COLUMN_FOOD_NAME + " ASC";
                break;
            case 4:
                queryString = "SELECT * FROM " + FOOD_TABLE + " ORDER BY " + COLUMN_FOOD_NAME + " DESC";
                break;
            case 5:
                queryString = "SELECT * FROM " + FOOD_TABLE + " ORDER BY " + COLUMN_FOOD_CALORIES + " ASC";
                break;
            case 6:
                queryString = "SELECT * FROM " + FOOD_TABLE + " ORDER BY " + COLUMN_FOOD_CALORIES + " DESC";
                break;
            case 7:
                queryString = "SELECT * FROM " + FOOD_TABLE + " ORDER BY " + COLUMN_FOOD_FAT + " ASC";
                break;
            case 8:
                queryString = "SELECT * FROM " + FOOD_TABLE + " ORDER BY " + COLUMN_FOOD_FAT + " DESC";
                break;
            case 9:
                queryString = "SELECT * FROM " + FOOD_TABLE + " ORDER BY " + COLUMN_FOOD_CARBS + " ASC";
                break;
            case 10:
                queryString = "SELECT * FROM " + FOOD_TABLE + " ORDER BY " + COLUMN_FOOD_CARBS + " DESC";
                break;
            case 11:
                queryString = "SELECT * FROM " + FOOD_TABLE + " ORDER BY " + COLUMN_FOOD_PROTEIN + " ASC";
                break;
            case 12:
                queryString = "SELECT * FROM " + FOOD_TABLE + " ORDER BY " + COLUMN_FOOD_PROTEIN + " DESC";
                break;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        return cursor;
    }

    public void editEntry(int id, String name, float calories, float fat, float carbs, float protein, int displayWeight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FOOD_NAME, name);
        values.put(COLUMN_FOOD_CALORIES, calories);
        values.put(COLUMN_FOOD_FAT, fat);
        values.put(COLUMN_FOOD_CARBS, carbs);
        values.put(COLUMN_FOOD_PROTEIN, protein);
        values.put(COLUMN_FOOD_DISPLAY_WEIGHT, displayWeight);

        db.update(FOOD_TABLE, values, "id = " + id, null);
    }

    public void deleteEntry(int foodId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FOOD_TABLE, "id = " + foodId, null);

    }

    //Methods for Calendar DB's
    public boolean calendarAddOne(CalendarFoodModel calendarFoodModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CALENDAR_COLUMN_FOOD_NAME, calendarFoodModel.getName());
        cv.put(CALENDAR_COLUMN_FOOD_CALORIES, calendarFoodModel.getCalories());
        cv.put(CALENDAR_COLUMN_FOOD_FAT, calendarFoodModel.getFat());
        cv.put(CALENDAR_COLUMN_FOOD_CARBS, calendarFoodModel.getCarbs());
        cv.put(CALENDAR_COLUMN_FOOD_PROTEIN, calendarFoodModel.getProtein());
        cv.put(CALENDAR_COLUMN_FOOD_WEIGHT, calendarFoodModel.getWeight());
        cv.put(CALENDAR_DATE_ADDED, calendarFoodModel.getDate());
        cv.put(CALENDAR_TIME_ADDED, calendarFoodModel.getTime());


        long insert = db.insert(CALENDAR_FOOD_TABLE, null, cv);
        if (insert == -1)
            return false;

        else
            return true;
    }

    public Cursor getFoodFromDate(String dateAdded) {

        queryString = "SELECT * FROM " + CALENDAR_FOOD_TABLE + " WHERE " + CALENDAR_DATE_ADDED + " = '" + dateAdded + "'" + "ORDER BY " + CALENDAR_TIME_ADDED + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        return cursor;
    }

    public int getNewestEntry() {
        SQLiteDatabase db = this.getWritableDatabase();

        queryString = "SELECT " + CALENDAR_COLUMN_FOOD_ID + " FROM " + CALENDAR_FOOD_TABLE + " WHERE " + CALENDAR_COLUMN_FOOD_ID + " = (SELECT MAX(" + CALENDAR_COLUMN_FOOD_ID + ") FROM " + CALENDAR_FOOD_TABLE + ")";
        Cursor cursor = db.rawQuery(queryString, null);

        int id = 0;

        if (cursor != null && cursor.moveToFirst()) {
            // Check if the column exists in the cursor
            int columnIndex = cursor.getColumnIndex(CALENDAR_COLUMN_FOOD_ID);
            if (columnIndex != -1) {
                // Retrieve the integer value from the cursor
                id = cursor.getInt(columnIndex);
            }
            cursor.close();
        }

        return id;
    }

    public List<String> findEntry(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        queryString = "SELECT * FROM " + CALENDAR_FOOD_TABLE + " WHERE " + CALENDAR_COLUMN_FOOD_ID + " = " + id;
        Cursor cursor = db.rawQuery(queryString, null);

        List<String> entryNumber = new ArrayList<>();
        while (cursor.moveToNext()) {
            entryNumber.add(cursor.getString(2));
            entryNumber.add(cursor.getString(3));
            entryNumber.add(cursor.getString(4));
            entryNumber.add(cursor.getString(5));
        }

        return entryNumber;
    }

    public void deleteCalendarEntry(int foodId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CALENDAR_FOOD_TABLE, CALENDAR_COLUMN_FOOD_ID + " = " + foodId, null);

    }

    public void deleteOldEntries(int olderThanNumber, String dayOrMonth) {
        SQLiteDatabase db = this.getWritableDatabase();

        LocalDate currDate = java.time.LocalDate.now();

        String thresholdDate;

        if(dayOrMonth == "Day"){
            thresholdDate = (currDate.minusDays(olderThanNumber)).toString();
        }else{
            thresholdDate = (currDate.minusMonths(olderThanNumber)).toString();

        }

        String[] selectionArgs = { thresholdDate };
        db.delete(CALENDAR_FOOD_TABLE, CALENDAR_DATE_ADDED+" <?" , selectionArgs);
        db.delete(CALENDAR_WEIGHT_TABLE, CALENDAR_WEIGHT_DATE_ENTERED+" <?" , selectionArgs);
    }

    public boolean addDailyWeight(double weight, String dateToAddOn){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CALENDAR_WEIGHT_NUMBER, weight);
        cv.put(CALENDAR_WEIGHT_DATE_ENTERED, dateToAddOn);

        long insert = db.insert(CALENDAR_WEIGHT_TABLE, null, cv);
        if (insert == -1){
            db.replace(CALENDAR_WEIGHT_TABLE, null, cv);
            return false;
        }
        else
            return true;
    }

    public String findDailyWeight(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String weightDaily = null;
        queryString = "SELECT "+CALENDAR_WEIGHT_NUMBER+" FROM " + CALENDAR_WEIGHT_TABLE + " WHERE " + CALENDAR_WEIGHT_DATE_ENTERED + " = ?";

        Cursor cursor = db.rawQuery(queryString, new String[]{date});
        while (cursor.moveToNext()) {
            weightDaily = cursor.getString(0);
        }
        return weightDaily;
    }

    public List<String> findDailyNutrition(String date){
        SQLiteDatabase db = this.getWritableDatabase();

        //date = "2024-05-27";
        queryString = "SELECT sum("+CALENDAR_COLUMN_FOOD_CALORIES+"), sum("+CALENDAR_COLUMN_FOOD_FAT+"), sum("+CALENDAR_COLUMN_FOOD_CARBS+"), sum("+CALENDAR_COLUMN_FOOD_PROTEIN+") FROM " + CALENDAR_FOOD_TABLE + " WHERE " + CALENDAR_DATE_ADDED + " = ?";

        Cursor cursor = db.rawQuery(queryString, new String[]{date});
        List<String> dailyNutrition = new ArrayList<>();

        while (cursor.moveToNext()) {
            dailyNutrition.add(cursor.getString(0));
            dailyNutrition.add(cursor.getString(1));
            dailyNutrition.add(cursor.getString(2));
            dailyNutrition.add(cursor.getString(3));
        }

        return dailyNutrition;
    }
}
