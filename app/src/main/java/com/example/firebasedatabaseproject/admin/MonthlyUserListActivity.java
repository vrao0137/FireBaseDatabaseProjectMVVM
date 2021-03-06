
package com.example.firebasedatabaseproject.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.example.firebasedatabaseproject.OnListItemClicked;
import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.admin.adapter.MonthlyUserDataAdapter;
import com.example.firebasedatabaseproject.admin.adminviewmodel.MonthlyUserListViewModel;
import com.example.firebasedatabaseproject.databinding.ActivityMonthlyUserListBinding;
import com.example.firebasedatabaseproject.user.model.NotesDataModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class MonthlyUserListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, OnListItemClicked {
    private ActivityMonthlyUserListBinding binding;
    private Context context;

    ArrayList<String>getNewMonthStringList = new ArrayList<>();
    HashSet<String>getHashMonthStringList = new HashSet<>();
    Boolean currentMotnhBoolean = false;
    ArrayList<String>getMonthStringList = new ArrayList<>();
    HashSet<String>getNewHashMonthStringList = new HashSet<>();

    String MonthName = "";
    String uUIID = "";
    String uUniqKey = "";
    MonthlyUserListViewModel monthlyUserListViewModel;

    ArrayList<NotesDataModel> lstUserNotesData = new ArrayList<>();
    ArrayList<NotesDataModel> lstUserNotesDataCheck = new ArrayList<>();
    MonthlyUserDataAdapter monthlyUserDataAdapter;
    //ArrayList
    ArrayList<NotesDataModel> newObserverList = new ArrayList<NotesDataModel>();
    ArrayList<NotesDataModel> newMonthList = new ArrayList<NotesDataModel>();
    ArrayList<NotesDataModel> newExtraData = new ArrayList<NotesDataModel>();

    //HashSet List
    HashSet<NotesDataModel> hashObserverList = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashMonthList = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashExtraData = new HashSet<NotesDataModel>();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMonthlyUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarTop.txvToolbarTitle.setText("MONTH DATA");
        monthlyUserListViewModel = ViewModelProviders.of(this).get(MonthlyUserListViewModel.class);
        checkUserDataAvailability();
       // getUserNotesData();
        initialize();
    }

    @Override
    public void onResume(){
        super.onResume();
        binding.toolbarTop.txvToolbarTitle.setText("MONTH DATA");
        checkUserDataAvailability();
       // getUserNotesData();
        initialize();
    }

    private void checkUserDataAvailability(){
        monthlyUserListViewModel.getMutableLiveDataCheck(uUIID).observe(this, new Observer<List<NotesDataModel>>() {
            @Override
            public void onChanged(List<NotesDataModel> notesDataModels) {
                lstUserNotesDataCheck.clear();
                lstUserNotesDataCheck.addAll((ArrayList<NotesDataModel>) notesDataModels);
                if (lstUserNotesDataCheck !=null && !lstUserNotesDataCheck.isEmpty()) {
                    getUserNotesData();
                }else {
                    binding.txvNoDataFound.setVisibility(View.VISIBLE);
                    binding.crdSppinerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getUserNotesData(){
        binding.txvNoDataFound.setVisibility(View.GONE);
        binding.crdSppinerView.setVisibility(View.VISIBLE);
        monthlyUserListViewModel.getMutableLiveDataMonths(uUIID).observe(this, new Observer<List<NotesDataModel>>() {
            @Override
            public void onChanged(List<NotesDataModel> notesDataModels) {
                lstUserNotesData.clear();

                hashObserverList.clear();
                hashExtraData.clear();

                lstUserNotesData.addAll((ArrayList<NotesDataModel>) notesDataModels);

                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                    }
                };
                String myFormat = "MMMM yyyy";
                SimpleDateFormat my = new SimpleDateFormat(myFormat, Locale.US);
                String mMonthYear = my.format(myCalendar.getTime());

                for (NotesDataModel obj1: lstUserNotesData){
                    if (obj1.getTask().equals(mMonthYear)){
                        hashObserverList.add(obj1);
                        currentMotnhBoolean = true;
                    }else {
                        hashExtraData.add(obj1);
                    }
                }

                //------Current Month Data----------------
                newObserverList.clear();
                newObserverList.addAll(hashObserverList);

                //-------Remainning month data--------------
                newExtraData.clear();
                newExtraData.addAll(hashExtraData);

                //------Add Current month Name as Spinner-----------
                getNewMonthStringList.clear();
                getHashMonthStringList.clear();

                getNewHashMonthStringList.clear();
                getMonthStringList.clear();

                if (currentMotnhBoolean.equals(true)){
                    getNewMonthStringList.add(mMonthYear);
                }else {
                    for (int i=0; i<newExtraData.size(); i++){
                        getNewHashMonthStringList.add(newExtraData.get(i).getTask());
                    }
                    getMonthStringList.addAll(getNewHashMonthStringList);
                    binding.spnrMonth.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }


                for (int i=0; i<newExtraData.size(); i++){
                    getHashMonthStringList.add(newExtraData.get(i).getTask());
                }
                //-----Add Reminning Month According to month data-----------
                getNewMonthStringList.addAll(getHashMonthStringList);

                binding.spnrMonth.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (currentMotnhBoolean.equals(true)){
                    monthlyUserDataAdapter.setDeveloperList(newObserverList);
                }else {
                    monthlyUserDataAdapter.setDeveloperList(newExtraData);
                }
                binding.rcvMonthlyUserData.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
                binding.rcvMonthlyUserData.setAdapter(monthlyUserDataAdapter);
                monthlyUserDataAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initialize(){
        context = this;
        Intent intent = getIntent();
        uUIID = intent.getStringExtra("UserUUID");
        binding.toolbarTop.ivToolbarButtonBack.setOnClickListener(this);
        checkUserDataAvailability();
        // getUserNotesData();

        binding.rcvMonthlyUserData.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
        monthlyUserDataAdapter = new MonthlyUserDataAdapter(this,lstUserNotesData,this);
        binding.rcvMonthlyUserData.setAdapter(monthlyUserDataAdapter);

        binding.spnrMonth.setOnItemSelectedListener(this);
        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,getNewMonthStringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnrMonth.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivToolbarButtonBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MonthName = getNewMonthStringList.get(position);

        hashMonthList.clear();

        for (NotesDataModel obj1: lstUserNotesData){
            if (obj1.getTask().equals(MonthName)){
                hashMonthList.add(obj1);
            }else {
                Log.e("False","");
            }
        }
        newMonthList.clear();
        newMonthList.addAll(hashMonthList);

        monthlyUserDataAdapter.setDeveloperList(newMonthList);
        binding.rcvMonthlyUserData.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
        binding.rcvMonthlyUserData.setAdapter(monthlyUserDataAdapter);
        monthlyUserDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClicked(int position, View view, String value) {
        switch (view.getId()) {
            case R.id.crdUpdatData:
                uUniqKey = newMonthList.get(position).getUniQKey();
                Intent intent = new Intent(this, UserTaskActivity.class).putExtra("UUIID",uUIID).putExtra("UniqKey",uUniqKey).putExtra("AdminHome","AdminHome");
                startActivity(intent);
                break;
        }
    }
}