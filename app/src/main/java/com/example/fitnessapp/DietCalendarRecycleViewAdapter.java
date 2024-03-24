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
    private ArrayList rvCalFoodName, rvCalCaloriesNum, rvCalFatNum, rvCalCarbsNum , rvCalProteinNum, rvCalDate, rvCalTime;
    private RecyclerViewInterface recyclerViewInterface;

    public DietCalendarRecycleViewAdapter(Context context, ArrayList rvDBFoodName, ArrayList rvDBCaloriesNum, ArrayList rvDBFatNum, ArrayList rvDBCarbsNum, ArrayList rvDBProteinNum, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.rvCalFoodName = rvCalFoodName;
        this.rvCalCaloriesNum = rvCalCaloriesNum;
        this.rvCalFatNum = rvCalFatNum;
        this.rvCalCarbsNum = rvCalCarbsNum;
        this.rvCalProteinNum = rvCalProteinNum;
        this.rvCalTime = rvCalTime;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.db_recycler_view_row,parent,false);
        return new MyViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.rvCalFoodName.setText(String.valueOf(rvCalFoodName.get(position)));
        holder.rvCalCaloriesNum.setText(String.valueOf(rvCalCaloriesNum.get(position)));
        holder.rvCalFatNum.setText(String.valueOf(rvCalFatNum.get(position)));
        holder.rvCalCarbsNum.setText(String.valueOf(rvCalCarbsNum.get(position)));
        holder.rvCalProteinNum.setText(String.valueOf(rvCalProteinNum.get(position)));
        holder.rvCalTime.setText(String.valueOf(rvCalTime.get(position)));
    }

    @Override
    public int getItemCount() {
        return rvCalFoodName.size();
    }

    public void matchCurrentDay(ArrayList<String> filteredList){
        rvCalDate = filteredList;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView rvCalFoodName, rvCalCaloriesNum, rvCalFatNum, rvCalCarbsNum , rvCalProteinNum, rvCalTime;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface){
            super(itemView);

            rvCalFoodName = itemView.findViewById(R.id.calendarRecyclerViewCaloriesNum);
            rvCalCaloriesNum = itemView.findViewById(R.id.calendarRecyclerViewCaloriesNum);
            rvCalFatNum = itemView.findViewById(R.id.calendarRecyclerViewFatNum);
            rvCalCarbsNum = itemView.findViewById(R.id.calendarRecyclerViewCaloriesNum);
            rvCalProteinNum = itemView.findViewById(R.id.calendarRecyclerViewProteinNum);
            rvCalTime = itemView.findViewById(R.id.calendarRecyclerTimeAdded);

            itemView.setOnClickListener(v -> {
                if (recyclerViewInterface != null){
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION ){
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            });

        }
    }
}
