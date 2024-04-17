package com.example.fitnessapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.LinearLayout;
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
    private DatabaseHelper databaseHelper;
    private DietCalendarRecycleViewAdapter calendarAdapter;
    private int day, month, year;
    private String str_month, str_day;
    private MainActivity mainActivity;
    private TextView weightTV, weightTVDescription;
    //Remember about the date changing back to OG. Do time check and if diff bigger than X amount of time, go back to normal
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendar = Calendar.getInstance();
        databaseHelper = new DatabaseHelper(getApplicationContext());
        mainActivity = new MainActivity();

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

        if(day < 10){
            str_day = "0" + day;
        }else{
            str_day = String.valueOf(day);
        }

        String selectedDate = year + "-" + str_month + "-" + str_day;
        displayData(selectedDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                if(month < 10){
                     str_month = "0" + (month+1);
                }else{
                     str_month = String.valueOf((month+1));
                }

                if(day < 10){
                    str_day = "0" + day;
                }else{
                    str_day = String.valueOf(day);
                }

                String selectedDate = year + "-" + str_month + "-" + str_day;

                updateRecyclerView(selectedDate);
            }
        });
    }

    public String getCurrentDate(){
        String date = String.valueOf(java.time.LocalDate.now());
        String[] current_date_array = date.split("-+");
        year = Integer.parseInt(current_date_array[0]);
        month = Integer.parseInt(current_date_array[1]);
        day = Integer.parseInt(current_date_array[2]);
        return date;
    }


    public void setCurrentDate(int day, int month, int year){
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.MONDAY, month - 1);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, day);
        long milli = calendar.getTimeInMillis();
        calendarView.setDate(milli);
    }

    public String getCurrentTime(){
        String time = String.valueOf(java.time.LocalTime.now());
        time = time.substring(0, time.indexOf("."));
        String hourFromTime = time.substring(0, time.indexOf(":"));
        String minutesFromTime = time.substring(time.indexOf(":") + 1, time.lastIndexOf(":"));
        time = hourFromTime +":"+minutesFromTime;
        return time;
    }
    @Override
    public void onItemClick(int position) {
        int calFoodId = Integer.parseInt(calendarFoodID.get(position));
        Intent intent = new Intent(DietCalendar.this, MainActivity.class);
        calendarDeleteConfirmationWindow(intent, calFoodId,this);
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
                calendarFoodTime.add(cursor.getString(8));

            }
        }
    }

    public void calendarDeleteConfirmationWindow(Intent intent,Integer idOfEntry, Context context) {

        final Dialog confirmationDialog = new Dialog(context);
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        confirmationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmationDialog.setContentView(R.layout.activity_db_settings_delete_popup);

        LinearLayout confirmDelete = confirmationDialog.findViewById(R.id.layoutConfirmDelete);
        LinearLayout confirmCancel = confirmationDialog.findViewById(R.id.layoutConfirmCancel);

        confirmDelete.setOnClickListener(v -> {
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            intent.putExtra("idForRemoval",idOfEntry);
            setResult(RESULT_OK, intent);
            confirmationDialog.dismiss();
            finish();
        });
        confirmCancel.setOnClickListener(v -> confirmationDialog.dismiss());

        confirmationDialog.show();
        confirmationDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        confirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmationDialog.getWindow().setGravity(Gravity.CENTER);
    }
}
