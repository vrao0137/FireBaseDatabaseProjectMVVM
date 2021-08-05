package com.example.firebasedatabaseproject.admin.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedatabaseproject.admin.MonthlyUserListActivity;
import com.example.firebasedatabaseproject.databinding.AdminUserDesignBinding;
import com.example.firebasedatabaseproject.model.NotesDataModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class MonthlyUserDataAdapter extends RecyclerView.Adapter<MonthlyUserDataAdapter.MyViewHolder>{
    MonthlyUserListActivity monthlyUserListActivity;
    ArrayList<NotesDataModel> lstUserNotesData;

    ArrayList<NotesDataModel> mDeveloperModel;

    public MonthlyUserDataAdapter(MonthlyUserListActivity monthlyUserListActivity, ArrayList<NotesDataModel> lstUserNotesData) {
        this.monthlyUserListActivity = monthlyUserListActivity;
        this.lstUserNotesData = lstUserNotesData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AdminUserDesignBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MonthlyUserDataAdapter.MyViewHolder holder, int position) {
        holder.binding.txvProjectName.setText(mDeveloperModel.get(position).getProjectName());
        holder.binding.txvDay.setText(mDeveloperModel.get(position).getWorkedHours());
        holder.binding.txvDate.setText(mDeveloperModel.get(position).getDate());
    }

    public void setDeveloperList(ArrayList<NotesDataModel> mDeveloperModel) {
        this.mDeveloperModel = mDeveloperModel;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mDeveloperModel != null){
            return mDeveloperModel.size();
        }else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdminUserDesignBinding binding;
        public MyViewHolder(AdminUserDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
