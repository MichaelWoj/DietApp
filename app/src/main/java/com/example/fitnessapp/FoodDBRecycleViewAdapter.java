package com.example.fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodDBRecycleViewAdapter extends RecyclerView.Adapter<FoodDBRecycleViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList rvDBFoodName, rvDBCaloriesNum, rvDBFatNum, rvDBCarbsNum , rvDBProteinNum;
    private final RecyclerViewInterface recyclerViewInterface;

    public FoodDBRecycleViewAdapter(Context context, ArrayList rvDBFoodName, ArrayList rvDBCaloriesNum, ArrayList rvDBFatNum, ArrayList rvDBCarbsNum, ArrayList rvDBProteinNum, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.rvDBFoodName = rvDBFoodName;
        this.rvDBCaloriesNum = rvDBCaloriesNum;
        this.rvDBFatNum = rvDBFatNum;
        this.rvDBCarbsNum = rvDBCarbsNum;
        this.rvDBProteinNum = rvDBProteinNum;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_view_row,parent,false);
        return new MyViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.rvDBFoodName.setText(String.valueOf(rvDBFoodName.get(position)));
        holder.rvDBCaloriesNum.setText(String.valueOf(rvDBCaloriesNum.get(position)));
        holder.rvDBFatNum.setText(String.valueOf(rvDBFatNum.get(position)));
        holder.rvDBCarbsNum.setText(String.valueOf(rvDBCarbsNum.get(position)));
        holder.rvDBProteinNum.setText(String.valueOf(rvDBProteinNum.get(position)));
    }

    @Override
    public int getItemCount() {
        return rvDBFoodName.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView rvDBFoodName, rvDBCaloriesNum, rvDBFatNum, rvDBCarbsNum , rvDBProteinNum;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            rvDBFoodName = itemView.findViewById(R.id.recyclerViewFoodName);
            rvDBCaloriesNum = itemView.findViewById(R.id.recyclerViewCaloriesNum);
            rvDBFatNum = itemView.findViewById(R.id.recyclerViewFatNum);
            rvDBCarbsNum = itemView.findViewById(R.id.recyclerViewCarbsNum);
            rvDBProteinNum = itemView.findViewById(R.id.recyclerViewProteinNum);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION ){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
