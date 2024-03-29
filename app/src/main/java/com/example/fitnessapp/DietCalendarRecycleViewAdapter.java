package com.example.fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DietCalendarRecycleViewAdapter extends RecyclerView.Adapter<DietCalendarRecycleViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList rvCalendarFoodName, rvCalendarCaloriesNum, rvCalendarFatNum, rvCalendarCarbsNum , rvCalendarProteinNum, rvCalendarDate, rvCalendarWeight, rvCalendarTime;
    private final RecyclerViewInterface recyclerViewInterfaceCalendar;

    public DietCalendarRecycleViewAdapter(Context context, ArrayList rvCalendarFoodName, ArrayList rvCalendarTime,ArrayList rvCalendarCaloriesNum, ArrayList rvCalendarFatNum, ArrayList rvCalendarCarbsNum, ArrayList rvCalendarProteinNum, ArrayList rvCalendarWeight, RecyclerViewInterface recyclerViewInterfaceCalendar) {
        this.context = context;
        this.rvCalendarFoodName = rvCalendarFoodName;
        this.rvCalendarTime = rvCalendarTime;
        this.rvCalendarCaloriesNum = rvCalendarCaloriesNum;
        this.rvCalendarFatNum = rvCalendarFatNum;
        this.rvCalendarCarbsNum = rvCalendarCarbsNum;
        this.rvCalendarProteinNum = rvCalendarProteinNum;
        this.rvCalendarWeight = rvCalendarWeight;
        this.recyclerViewInterfaceCalendar = recyclerViewInterfaceCalendar;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.calendar_recycler_view_row,parent,false);
        return new MyViewHolder(v, recyclerViewInterfaceCalendar);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.rvCalFoodName.setText(String.valueOf(rvCalendarFoodName.get(position)));
        holder.rvCalFoodWeight.setText(String.valueOf(rvCalendarWeight.get(position)));
        holder.rvCalCaloriesNum.setText(String.valueOf(rvCalendarCaloriesNum.get(position)));
        holder.rvCalFatNum.setText(String.valueOf(rvCalendarFatNum.get(position)));
        holder.rvCalCarbsNum.setText(String.valueOf(rvCalendarCarbsNum.get(position)));
        holder.rvCalProteinNum.setText(String.valueOf(rvCalendarProteinNum.get(position)));
        holder.rvCalTime.setText(String.valueOf(rvCalendarTime.get(position)));
    }

    @Override
    public int getItemCount() {
        return rvCalendarFoodName.size();
    }

    public void matchCurrentDay(ArrayList<String> filteredList){
        rvCalendarDate = filteredList;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView rvCalFoodName, rvCalFoodWeight, rvCalCaloriesNum, rvCalFatNum, rvCalCarbsNum , rvCalProteinNum, rvCalTime;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterfaceCalendar){
            super(itemView);

            rvCalFoodName = itemView.findViewById(R.id.calendarRecyclerViewCaloriesNum);
            rvCalFoodWeight = itemView.findViewById(R.id.calendarRecyclerViewWeight);
            rvCalCaloriesNum = itemView.findViewById(R.id.calendarRecyclerViewCaloriesNum);
            rvCalFatNum = itemView.findViewById(R.id.calendarRecyclerViewFatNum);
            rvCalCarbsNum = itemView.findViewById(R.id.calendarRecyclerViewCaloriesNum);
            rvCalProteinNum = itemView.findViewById(R.id.calendarRecyclerViewProteinNum);
            rvCalTime = itemView.findViewById(R.id.calendarRecyclerTimeAdded);

            itemView.setOnClickListener(v -> {
                if (recyclerViewInterfaceCalendar != null){
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION ){
                        recyclerViewInterfaceCalendar.onItemClick(pos);
                    }
                }
            });

        }
    }
}
