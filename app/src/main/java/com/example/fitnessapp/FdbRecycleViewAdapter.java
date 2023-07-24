package com.example.fitnessapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FdbRecycleViewAdapter extends RecyclerView.Adapter<FdbRecycleViewAdapter.MyViewHolder> {


    @NonNull
    @Override
    public FdbRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FdbRecycleViewAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView rvDBFoodName, rvDBCalories, rvDBCaloriesNum, rvDBFat, rvDBFatNum, rvDBCarbs, rvDBCarbsNum , rvDBProtein, rvDBProteinNum;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rvDBFoodName = itemView.findViewById(R.id.recyclerViewFoodName);
            rvDBCalories = itemView.findViewById(R.id.recyclerViewCalories);
            rvDBCaloriesNum = itemView.findViewById(R.id.recyclerViewCaloriesNum);
            rvDBFat = itemView.findViewById(R.id.recyclerViewFat);
            rvDBFatNum = itemView.findViewById(R.id.recyclerViewFatNum);
            rvDBCarbs = itemView.findViewById(R.id.recyclerViewCarbs);
            rvDBCarbsNum = itemView.findViewById(R.id.recyclerViewCarbsNum);
            rvDBProtein = itemView.findViewById(R.id.recyclerViewProtein);
            rvDBProteinNum = itemView.findViewById(R.id.recyclerViewProteinNum);
        }
    }
}
