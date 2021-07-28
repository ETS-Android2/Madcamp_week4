package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travel.items.Dateinfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalendarActivity extends AppCompatActivity  implements OnNavigationButtonClickedListener {

    CustomCalendar customCalendar;
    ArrayList<String> days = new ArrayList<>();
    FloatingActionButton sendCalendar ;
    Calendar calendar;
    HashMap<Integer,Object> dateHashMap;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    public static String BASE_URL = LoginActivity.BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //calendar
        customCalendar = findViewById(R.id.custom_calendar);
        sendCalendar = findViewById(R.id.sendCalendar);

        HashMap<Object, Property> descHashMap = new HashMap<>();

        Property defaultProperty = new Property();
        defaultProperty.layoutResource = R.layout.default_view;
        defaultProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("default" , defaultProperty); //흰색

        Property currentProperty = new Property();
        currentProperty.layoutResource = R.layout.current_view;
        currentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("current" , currentProperty); // 파란색

        Property presentProperty = new Property();
        presentProperty.layoutResource = R.layout.present_view;
        presentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("present" ,presentProperty); //초록색

        Property absentProperty = new Property();
        absentProperty.layoutResource = R.layout.absent_view;
        absentProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("absent" , absentProperty); //핑크색

        customCalendar.setMapDescToProp(descHashMap);
        dateHashMap = new HashMap<>();

        calendar = Calendar.getInstance(); //현재 날짜가 속한 달
        customCalendar.setDate(calendar,dateHashMap);
        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String sDate = selectedDate.get(Calendar.DAY_OF_MONTH)
                        +"/"+(selectedDate.get(Calendar.MONTH) + 1)
                        +"/" +selectedDate.get(Calendar.YEAR);
                Toast.makeText(getApplicationContext(), sDate, Toast.LENGTH_SHORT).show();

                dateHashMap.put(selectedDate.get(Calendar.DAY_OF_MONTH) , "absent");
                customCalendar.setDate(calendar,dateHashMap);

                days.add((selectedDate.get(Calendar.MONTH) + 1)+ "/" +selectedDate.get(Calendar.DAY_OF_MONTH)); //안되는 날짜들 list
                // 8/20 이런식으로 저장
            }
        });

        //안되는 날짜들 리스트로 보냄
        sendCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for(int i=0;i<days.size();i++){
//                    Log.d("checked : " ,days.get(i));
//                }
                Dateinfo dateinfo = new Dateinfo(MainActivity.useremail , days);
                Call<Void> call = retrofitInterface.sendDays(dateinfo);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {

                        } else if (response.code() == 400) {

                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });

        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);

    }

    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[2];
        switch(newMonth.get(Calendar.MONTH)) {
            case Calendar.AUGUST:
                arr[0] = new HashMap<>();
                calendar.add(calendar.MONTH, 1);
                dateHashMap.clear();
                //arr[0].put(6, "current");
                break;

            case Calendar.SEPTEMBER:
                arr[0] = new HashMap<>();
                calendar.add(calendar.MONTH, 2);
                dateHashMap.clear();
                break;

            case Calendar.JUNE:
                arr[0] = new HashMap<>();
                dateHashMap.clear();
                calendar.add(calendar.MONTH , -1);
                break;
        }
        return arr;
    }
}