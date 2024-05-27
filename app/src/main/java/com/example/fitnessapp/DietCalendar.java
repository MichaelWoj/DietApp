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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DietCalendar extends AppCompatActivity implements RecyclerViewInterface{

    private ArrayList<String> calendarFoodID, calendarFoodName, calendarFoodTime, calendarFoodCaloriesNum, calendarFoodFatNum, calendarFoodCarbsNum, calendarFoodProteinNum, calendarFoodWeightNum;
    private CalendarView calendarView;
    private DatabaseHelper dataBaseHelper;
    private Calendar calendar;
    private DatabaseHelper databaseHelper;
    private ImageButton settings, swapCardsWeight, swapCardsNutrition;
    private CardView weightCard, nutritionCard;
    private DietCalendarRecycleViewAdapter calendarAdapter;
    private int day, month, year;
    private String str_month, str_day, str_time, selectedDate;
    public static final String savedCalendarEntryTime = "time";
    public static final String savedCalendarDate = "date";
    public static final String savedToggleButtonState = "toggleButton";
    private MainActivity mainActivity;
    private String dayOrMonthSpinner = "Day";
    private TextView weightKgOrLbsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendar = Calendar.getInstance();
        databaseHelper = new DatabaseHelper(getApplicationContext());
        mainActivity = new MainActivity();

        calendarView = findViewById(R.id.calendarView);

        settings = findViewById(R.id.itemSettingsBtn);
        swapCardsWeight = findViewById(R.id.swapToNutritionAndBackWeight);
        swapCardsNutrition = findViewById(R.id.swapToNutritionAndBackNutrition);

        dataBaseHelper = new DatabaseHelper(DietCalendar.this);

        calendarFoodID = new ArrayList<>();
        calendarFoodName = new ArrayList<>();
        calendarFoodTime = new ArrayList<>();
        calendarFoodCaloriesNum = new ArrayList<>();
        calendarFoodFatNum = new ArrayList<>();
        calendarFoodCarbsNum = new ArrayList<>();
        calendarFoodProteinNum = new ArrayList<>();
        calendarFoodWeightNum = new ArrayList<>();

        weightKgOrLbsTV = findViewById(R.id.calendarWeightKgOrLbs);

        weightCard = findViewById(R.id.calendarWeightCard);
        nutritionCard = findViewById(R.id.calendarNutritionCard);

        RecyclerView caledarRecyclerView = findViewById(R.id.recyclerViewCalendarList);
        calendarAdapter = new DietCalendarRecycleViewAdapter(this, calendarFoodName, calendarFoodTime, calendarFoodCaloriesNum, calendarFoodFatNum, calendarFoodCarbsNum, calendarFoodProteinNum, calendarFoodWeightNum, this);
        calendarAdapter.notifyDataSetChanged();
        caledarRecyclerView.setAdapter(calendarAdapter);
        caledarRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        str_time = getCurrentTime();

        checkTimeDiff(str_time);

        saveTimeSharedPreferences();
        loadToggleButtonSharedPreferences();


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

        selectedDate = year + "-" + str_month + "-" + str_day;
        displayData(selectedDate);

        setNutritionOnDay(selectedDate);

        settings.setOnClickListener(view -> {
            showCalendarSettings(this);
        });

        swapCardsWeight.setOnClickListener(view -> {
            weightCard.setVisibility(View.GONE);
            nutritionCard.setVisibility(View.VISIBLE);

        });
        swapCardsNutrition.setOnClickListener(view -> {
            weightCard.setVisibility(View.VISIBLE);
            nutritionCard.setVisibility(View.GONE);

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

                selectedDate = year + "-" + str_month + "-" + str_day;
                saveDateSharedPreferences(selectedDate);
                updateRecyclerView(selectedDate);
                setWeightOnDay();
                setNutritionOnDay(selectedDate);

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

    private void savedToggleButtonSharedPreferences(String kgOrLbs){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS_BUTTON_MODE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(savedToggleButtonState, kgOrLbs);

        editor.apply();
    }

    private void loadToggleButtonSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS_BUTTON_MODE", MODE_PRIVATE);
        String loadKgOrLbs = sharedPreferences.getString(savedToggleButtonState, "kg");
        weightKgOrLbsTV.setText(loadKgOrLbs);

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

        long days = duration.toDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60; // Remaining minutes after hours


        if(days == 0 & hours < 1 & minutes<=5 ){
            String[] current_date_array = previousSetDate.split("-+");
            year = Integer.parseInt(current_date_array[0]);
            month = Integer.parseInt(current_date_array[1]);
            day = Integer.parseInt(current_date_array[2]);
            setCurrentDate(day, month, year);
            selectedDate = previousSetDate;
            setWeightOnDay();
        }else{
            selectedDate = getCurrentDate();

            setCurrentDate(day, month, year);
            setWeightOnDay();
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

    private void setWeightOnDay(){
        TextView calendarWeightTV = findViewById(R.id.calendarWeight);
        String selectedDateWeight = dataBaseHelper.findDailyWeight(selectedDate);
        if(selectedDateWeight == null){
            calendarWeightTV.setText("Not Entered");
        }else{
            calendarWeightTV.setText(selectedDateWeight);
        }
    }

    private void setNutritionOnDay(String dateAdded) {
        TextView dailyCalendarCalories = findViewById(R.id.dailyCalendarCaloriesNumber);
        TextView dailyCalendarFat = findViewById(R.id.dailyCalendarFatNumber);
        TextView dailyCalendarCarbs = findViewById(R.id.dailyCalendarCarbsNumber);
        TextView dailyCalendarProtein = findViewById(R.id.dailyCalendarProteinNumber);

        List<String> dailyNutrition= dataBaseHelper.findDailyNutrition(dateAdded);

        if (dailyNutrition.get(0) != null) {

            String calorieNum = dailyNutrition.get(0);
            String fatNum = dailyNutrition.get(1);
            String carbsNum = dailyNutrition.get(2);
            String proteinNum = dailyNutrition.get(3);

            dailyCalendarCalories.setText(calorieNum);
            dailyCalendarFat.setText(fatNum);
            dailyCalendarCarbs.setText(carbsNum);
            dailyCalendarProtein.setText(proteinNum);
        }else{
            dailyCalendarCalories.setText("0.0");
            dailyCalendarFat.setText("0.0");
            dailyCalendarCarbs.setText("0.0");
            dailyCalendarProtein.setText("0.0");
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

    private void showCalendarSettings(Context context) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calendar_settings_popup);

        LinearLayout settingsEnterWeight = dialog.findViewById(R.id.layoutCalendarSettingsEnterWeight);
        LinearLayout settingsDeleteOldEntries = dialog.findViewById(R.id.layoutCalendarSettingsDeleteOldEntries);
        Button setToKg = dialog.findViewById(R.id.setToKg);
        Button setToLbs = dialog.findViewById(R.id.setToLbs);


        settingsEnterWeight.setOnClickListener(v -> {
            calendarDailyWeightPopup(context);
            dialog.dismiss();
        });

        settingsDeleteOldEntries.setOnClickListener(v -> {
            calendarRemoveOldEntriesPopup(context);
            dialog.dismiss();
        });

        setToKg.setOnClickListener(view -> {
            weightKgOrLbsTV.setText("kg");
            savedToggleButtonSharedPreferences("kg");
        });

        setToLbs.setOnClickListener(view -> {
            weightKgOrLbsTV.setText("lbs");
            savedToggleButtonSharedPreferences("lbs");
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    public void calendarDailyWeightPopup(Context context) {

        final Dialog weightDialog = new Dialog(context);
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        weightDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        weightDialog.setContentView(R.layout.calendar_set_weight_popup);

        EditText olderThanNumberET = weightDialog.findViewById(R.id.dailyWeight);

        LinearLayout confirmDelete = weightDialog.findViewById(R.id.layoutConfirmWeight);
        LinearLayout confirmCancel = weightDialog.findViewById(R.id.layoutCancelWeight);

        confirmDelete.setOnClickListener(v -> {
            if(olderThanNumberET.getText().toString().isEmpty()){
                Toast.makeText(DietCalendar.this, "Please weight", Toast.LENGTH_SHORT).show();
            }else{
                String dailyWeightToString = olderThanNumberET.getText().toString();
                double dailyWeightToDouble = Double.parseDouble(dailyWeightToString);
                dataBaseHelper.addDailyWeight(dailyWeightToDouble, selectedDate);
                setWeightOnDay();
                weightDialog.dismiss();
            }
        });
        confirmCancel.setOnClickListener(v -> weightDialog.dismiss());

        weightDialog.show();
        weightDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        weightDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void calendarRemoveOldEntriesPopup(Context context) {

        final Dialog timeDialog = new Dialog(context);
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        timeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timeDialog.setContentView(R.layout.calendar_remove_old_entries_popup);

        EditText olderThanNumberET = timeDialog.findViewById(R.id.olderThanNumber);

        LinearLayout confirmDelete = timeDialog.findViewById(R.id.layoutConfirmDeleteCalendar);
        LinearLayout confirmCancel = timeDialog.findViewById(R.id.layoutConfirmCancelCalendar);

        Spinner spinner = timeDialog.findViewById(R.id.dayMonthSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                R.array.day_month,
                R.layout.spinner_item_layout
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
            String olderThanNumberToString = olderThanNumberET.getText().toString();
            int olderThanNumberInt = Integer.parseInt(olderThanNumberToString);
            dataBaseHelper.deleteOldEntries(olderThanNumberInt,dayOrMonthSpinner);
            timeDialog.dismiss();
        });
        confirmCancel.setOnClickListener(v -> timeDialog.dismiss());

        timeDialog.show();
        timeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        timeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timeDialog.getWindow().setGravity(Gravity.CENTER);
    }
}
