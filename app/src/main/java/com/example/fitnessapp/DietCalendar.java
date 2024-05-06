package com.example.fitnessapp;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

public class DietCalendar extends AppCompatActivity implements RecyclerViewInterface{

    private ArrayList<String> calendarFoodID, calendarFoodName, calendarFoodTime, calendarFoodCaloriesNum, calendarFoodFatNum, calendarFoodCarbsNum, calendarFoodProteinNum, calendarFoodWeightNum;
    private CalendarView calendarView;
    private DatabaseHelper dataBaseHelper;
    private Calendar calendar;
    private DatabaseHelper databaseHelper;
    private ImageButton settings;
    private DietCalendarRecycleViewAdapter calendarAdapter;
    private int day, month, year;
    private String str_month, str_day, str_time;
    public static final String savedCalendarEntryTime = "time";
    public static final String savedCalendarDate = "date";
    private MainActivity mainActivity;
    private String dayOrMonthSpinner = "Day";
    private TextView weightTV, weightTVDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendar = Calendar.getInstance();
        databaseHelper = new DatabaseHelper(getApplicationContext());
        mainActivity = new MainActivity();

        calendarView = findViewById(R.id.calendarView);

        settings = findViewById(R.id.itemSettingsBtn);

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

        str_time = getCurrentTime();

        checkTimeDiff(str_time);

        saveTimeSharedPreferences();

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

        settings.setOnClickListener(view -> {
            calendarRemoveOldEntriesPopup(this);
        });

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
                saveDateSharedPreferences(selectedDate);
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
    private void saveTimeSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS_TIME", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(savedCalendarEntryTime, str_time);

        editor.apply();
    }
    private void saveDateSharedPreferences(String str_date){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS_TIME", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(savedCalendarDate, str_date);

        editor.apply();
    }
    @Override
    public void onItemClick(int position) {
        int calFoodId = Integer.parseInt(calendarFoodID.get(position));
        Intent intent = new Intent(DietCalendar.this, MainActivity.class);
        calendarDeleteConfirmationWindow(intent, calFoodId,this);
    }

    private void checkTimeDiff(String entryTime) {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS_TIME", MODE_PRIVATE);

        String previousEntryTime = sharedPreferences.getString(savedCalendarEntryTime,"00:00");
        String previousSetDate = sharedPreferences.getString(savedCalendarDate,"1970-01-01");

        LocalTime pastTime = LocalTime.parse(previousEntryTime);
        LocalTime currentTime = LocalTime.parse(entryTime);

        Duration duration = Duration.between(pastTime, currentTime);


        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60; // Remaining minutes after hours

        if(hours < 1 & minutes<=5 ){
            String[] current_date_array = previousSetDate.split("-+");
            year = Integer.parseInt(current_date_array[0]);
            month = Integer.parseInt(current_date_array[1]);
            day = Integer.parseInt(current_date_array[2]);
            setCurrentDate(day, month, year);
        }
        else{
            getCurrentDate();

            setCurrentDate(day, month, year);
        }
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

    public void calendarRemoveOldEntriesPopup(Context context) {

        final Dialog timeDialog = new Dialog(context);
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        timeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timeDialog.setContentView(R.layout.calendar_remove_old_entries_popup);

        EditText olderThanNumberET = findViewById(R.id.olderThanNumber);

        LinearLayout confirmDelete = timeDialog.findViewById(R.id.layoutConfirmDeleteCalendar);
        LinearLayout confirmCancel = timeDialog.findViewById(R.id.layoutConfirmCancelCalendar);

        Spinner spinner = findViewById(R.id.dayMonthSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                R.array.day_month,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                dayOrMonthSpinner = adapterView.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        confirmDelete.setOnClickListener(v -> {
            int olderThanNumberInt = Integer.parseInt(String.valueOf(olderThanNumberET));
            DatabaseHelper.deleteOldEntries(olderThanNumberInt,dayOrMonthSpinner);
            finish();
        });
        confirmCancel.setOnClickListener(v -> timeDialog.dismiss());


        timeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        timeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timeDialog.getWindow().setGravity(Gravity.CENTER);
    }
}
