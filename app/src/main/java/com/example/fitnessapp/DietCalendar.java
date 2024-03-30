package com.example.fitnessapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DietCalendar extends AppCompatActivity implements RecyclerViewInterface{

    private ArrayList<String> calendarFoodID, calendarFoodName, calendarFoodTime, calendarFoodCaloriesNum, calendarFoodFatNum, calendarFoodCarbsNum, calendarFoodProteinNum, calendarFoodWeightNum;
    private CalendarView calendarView;
    private DatabaseHelper dataBaseHelper;
    private Calendar calendar;
    private DietCalendarRecycleViewAdapter calendarAdapter;
    private int day, month, year;
    private String str_month;
    private TextView weightTV, weightTVDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendar = Calendar.getInstance();

        calendarView = findViewById(R.id.calendarView);

        dataBaseHelper = new DatabaseHelper(DietCalendar.this);

        calendarFoodID = new ArrayList<>();
        calendarFoodName = new ArrayList<>();
        calendarFoodTime = new ArrayList<>();
        calendarFoodCaloriesNum = new ArrayList<>();
        calendarFoodFatNum = new ArrayList<>();
        calendarFoodCarbsNum = new ArrayList<>();
        calendarFoodProteinNum = new ArrayList<>();
        calendarFoodWeightNum = new ArrayList<>();

        RecyclerView caledarRecyclerView = findViewById(R.id.recyclerViewCalendarList);
        calendarAdapter = new DietCalendarRecycleViewAdapter(this, calendarFoodName, calendarFoodTime, calendarFoodCaloriesNum, calendarFoodFatNum, calendarFoodCarbsNum, calendarFoodProteinNum, calendarFoodWeightNum, this);
        calendarAdapter.notifyDataSetChanged();
        caledarRecyclerView.setAdapter(calendarAdapter);
        caledarRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getCurrentDate();

        setCurrentDate(day, month, year);

        if(month < 10){
            str_month = "0" + month;
        }else{
            str_month = String.valueOf(month);
        }

        String selectedDate = year + "-" + str_month + "-" + day;
        displayData(selectedDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                if(month < 10){
                     str_month = "0" + month;
                }else{
                     str_month = String.valueOf(month);
                }

                String selectedDate = year + "-" + str_month + "-" + day;

                updateRecyclerView(selectedDate);
            }
        });
    }

    public void getCurrentDate(){
        long date = calendarView.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        calendar.setTimeInMillis(date);
        String current_date = simpleDateFormat.format(calendar.getTime());
        String[] current_date_array = current_date.split("/+");
        day = Integer.parseInt(current_date_array[0]);
        month = Integer.parseInt(current_date_array[1]);
        year = Integer.parseInt(current_date_array[2]);
    }

    public void setCurrentDate(int day, int month, int year){
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.MONDAY, month - 1);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, day);
        long milli = calendar.getTimeInMillis();
        calendarView.setDate(milli);
    }

    @Override
    public void onItemClick(int position) {

    }

    private void updateRecyclerView(String userSelectedDate){
        clearRecycleView();
        calendarAdapter.notifyDataSetChanged();
        displayData(userSelectedDate);
    }
    private void clearRecycleView(){
        calendarFoodName.clear();
        calendarFoodTime.clear();
        calendarFoodCaloriesNum.clear();
        calendarFoodFatNum.clear();
        calendarFoodCarbsNum.clear();
        calendarFoodProteinNum.clear();
        calendarFoodWeightNum.clear();
    }

    private void displayData(String dateAdded) {
        Cursor cursor = dataBaseHelper.getFoodFromDate(dateAdded);
        if (cursor.getCount() == 0) {
            Toast.makeText(DietCalendar.this, "No Entry Found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                calendarFoodID.add(cursor.getString(0));
                calendarFoodName.add(cursor.getString(1));
                calendarFoodCaloriesNum.add(cursor.getString(2));
                calendarFoodFatNum.add(cursor.getString(3));
                calendarFoodCarbsNum.add(cursor.getString(4));
                calendarFoodProteinNum.add(cursor.getString(5));
                calendarFoodWeightNum.add(cursor.getString(6));
                calendarFoodTime.add(cursor.getString(7));

            }
        }
    }
}
