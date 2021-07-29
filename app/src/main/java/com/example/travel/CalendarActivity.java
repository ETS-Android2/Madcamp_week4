package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    Button sendCalendar , refreshCalendar;
    Calendar calendar;
    HashMap<Integer,Object> dateHashMap;
    Dateinfo dateinfo;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    public static String BASE_URL = LoginActivity.BASE_URL;

    int [] july =      {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int [] august =    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int [] september = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

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

        //접속한 유저 안되는 날짜 리스트 받아오기
        HashMap<String, String> map = new HashMap<>();
        map.put("email", MainActivity.useremail);
        Call<Dateinfo> call = retrofitInterface.getFriendDays(map);

        call.enqueue(new Callback<Dateinfo>() {
            @Override
            public void onResponse(Call<Dateinfo> call, Response<Dateinfo> response) {

                if (response.code() == 200) {
                    dateinfo = response.body();
                    ArrayList<String> days = new ArrayList<String>();
                    days = dateinfo.getDays();

                    for(int j=0;j<days.size();j++){
                        String [] parsed = days.get(j).split("/");

                        if(parsed[0].equals("7")){
                            july[Integer.parseInt(parsed[1])-1] = 1;
                        }else if(parsed[0].equals("8")){
                            august[Integer.parseInt(parsed[1])-1] = 1;
                        }else if(parsed[0].equals("9")){
                            september[Integer.parseInt(parsed[1])-1] = 1;
                        }
                    }

                    for(int j=0;j<july.length;j++){
                        if(july[j] != 1){
                            dateHashMap.put(j+1 ,"current");
                        }
                    }
                    customCalendar.setDate(calendar,dateHashMap);


                } else if (response.code() == 404) {

                }

            }
            @Override
            public void onFailure(Call<Dateinfo> call, Throwable t) {

            }
        });

        //calendar
        customCalendar = findViewById(R.id.custom_calendar);
        sendCalendar = findViewById(R.id.sendCalendar);
        refreshCalendar = findViewById(R.id.refreshCalendar);

        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);

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
        //customCalendar.setDate(calendar,dateHashMap);
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

        refreshCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                days.clear();
                dateHashMap.clear();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
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

    }

    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[2];
        switch(newMonth.get(Calendar.MONTH)) {
            case Calendar.AUGUST:
                arr[0] = new HashMap<>();
                calendar.add(calendar.MONTH, 1);
                dateHashMap.clear();
                for (int i= 0; i<=30 ;i++){
                    if(august[i] != 1){
                        arr[0].put(i+1 , "current");
                    }
                }
                break;

            case Calendar.SEPTEMBER:
                arr[0] = new HashMap<>();
                calendar.add(calendar.MONTH, 2);
                for (int i= 0; i<=29 ;i++){
                    if(september[i] != 1){
                        arr[0].put(i+1 , "current");
                    }
                }
                dateHashMap.clear();
                break;
            case Calendar.JULY:
                arr[0] = new HashMap<>();
                calendar = Calendar.getInstance();
                dateHashMap.clear();
                for (int i= 0; i<=30 ;i++){
                    if(july[i] != 1){
                        arr[0].put(i+1, "current");
                    }
                }
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