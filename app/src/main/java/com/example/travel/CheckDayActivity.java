package com.example.travel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CheckDayActivity extends AppCompatActivity {

//    TextView etDate;
//    Button showCalendar;
//    ArrayList<Integer> days = new ArrayList<Integer>();

    ListView listViewData;
    ArrayAdapter<String> adapter;
    String[] arrayPeliculas = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17"
    ,"18","19","20","21","22","23","24","25","26","27","28","29","30","31"};

    Button dayCheck ;
    ArrayList<String> days = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_day);

        listViewData = findViewById(R.id.listView_data);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice , arrayPeliculas);
        listViewData.setAdapter(adapter);

        dayCheck = findViewById(R.id.dayCheck);

        dayCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckDayActivity.this , CalendarActivity.class);
                for(int i=0; i<listViewData.getCount();i++){
                    if(listViewData.isItemChecked(i)){
                        days.add(String.valueOf(listViewData.getItemAtPosition(i)));
                    }
                }
                intent.putExtra("daylist" , days);
                startActivity(intent);
            }
        });

//        etDate = findViewById(R.id.et_date);
//        showCalendar = findViewById(R.id.bt_showcalendar);
//
//        Calendar calendar = Calendar.getInstance();
//        final int year = calendar.get(Calendar.YEAR);
//        final int month = calendar.get(Calendar.MONTH);
//        final int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        etDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        CheckDayActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int day) {
//                        month = month +1;
//                        String date = year + "/" + month + "/" + day;
//                        etDate.setText(date);
//
//                        days.add(day);
//                    }
//                },year,month,day);
//                datePickerDialog.show();
//            }
//        });
//
//        showCalendar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CheckDayActivity.this, CalendarActivity.class);
//                intent.putExtra("daylist" , days);
//                startActivity(intent);
//            }
//        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
////        int id = item.getItemId();
////        if(id == R.id.item_done){
////            String itemSelected = "Selected items : \n";
////            for(int i=0 ;i<listViewData.getCount();i++){
////                if(listViewData.isItemChecked(i)){
////                    itemSelected += listViewData.getItemAtPosition(i) + "\n";
////                }
////            }
////            Toast.makeText(this, itemSelected, Toast.LENGTH_SHORT).show();
////        }
//        return super.onOptionsItemSelected(item);
//    }
}