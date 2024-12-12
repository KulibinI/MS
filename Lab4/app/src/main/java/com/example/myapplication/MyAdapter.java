package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<MyData> myDataList; // Объявление переменной
    private Context context;

    public MyAdapter(List<MyData> myDataList, Context context) {
        this.myDataList = myDataList; // Присваивание значения
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_data, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyData myData = myDataList.get(position);
        holder.userIdTextView.setText("User ID: " + myData.getUserId());
        holder.idTextView.setText("ID: " + myData.getId());
        holder.titleTextView.setText("Title: " + myData.getTitle());
        holder.bodyTextView.setText("Body: " + myData.getBody());
    }

    @Override
    public int getItemCount() {
        return myDataList.size(); // Возвращаем размер списка
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userIdTextView;
        TextView idTextView;
        TextView titleTextView;
        TextView bodyTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.userIdTextView);
            idTextView = itemView.findViewById(R.id.idTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            bodyTextView = itemView.findViewById(R.id.bodyTextView);
        }
    }
}