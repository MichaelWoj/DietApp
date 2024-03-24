package com.example.fitnessapp;

import android.os.Bundle;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DietCalendar extends AppCompatActivity {

    CalendarView calendarView;
    Calendar calendar;
    int day, month, year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        calendar = Calendar.getInstance();

        getCurrentDate();

        setCurrentDate(day, month, year);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

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
}
