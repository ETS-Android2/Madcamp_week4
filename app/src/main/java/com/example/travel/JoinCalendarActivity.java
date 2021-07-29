package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.travel.items.Dateinfo;
import com.example.travel.items.SelectDay;
import com.example.travel.items.Userinfo;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinCalendarActivity extends AppCompatActivity  implements OnNavigationButtonClickedListener {

    CustomCalendar customCalendar;
    String place;
    Calendar calendar;
    HashMap<Integer,Object> dateHashMap =new HashMap<>();
    ArrayList<String> friends = new ArrayList<>();
    ArrayList<String> travelDay = new ArrayList<>();

    Button saveDays;

    ArrayList<Integer> July =      new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)); //31
    ArrayList<Integer> August =    new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)); //31
    ArrayList<Integer> September = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)); //30

    int [] july =      {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int [] august =    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int [] september = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    public static String BASE_URL = LoginActivity.BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_calendar);
        saveDays = findViewById(R.id.sendTravelDays);

        Intent intent =getIntent(); //친구 목록 받음.
        friends = (ArrayList<String>) intent.getSerializableExtra("friendlist");
        place = intent.getStringExtra("place");

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        customCalendar = findViewById(R.id.join_calendar);

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
        //calendar = Calendar.getInstance(); //현재 날짜가 속한 달


        //for문 돌면서 친구들 date list 하나씩 받아옴
        for(int i=0;i<friends.size();i++) {
            int j = i;
            HashMap<String, String> map = new HashMap<>();
            map.put("email", friends.get(i));
            Call<Dateinfo> call = retrofitInterface.getFriendDays(map);

            call.enqueue(new Callback<Dateinfo>() {
                @Override
                public void onResponse(Call<Dateinfo> call, Response<Dateinfo> response) {

                    if (response.code() == 200) {
                        Dateinfo result = response.body();
//                        for(int j=0;j<result.getDays().size();j++) {
//                            Log.d("days", result.getDays().get(j));
//                        }
                        ArrayList<String> days = new ArrayList<String>();
                        days = result.getDays();

                        for(int j=0;j<result.getDays().size();j++) {
//                            Log.d("days", days.get(j));
                        }

                        for(int j=0;j<days.size();j++){
                            String [] parsed = days.get(j).split("/");
                            for(int i =0;i<2;i++){
//                                Log.d("parse" , parsed[i]);
                            }
                            if(parsed[0].equals("7")){
                                July.add(Integer.parseInt(parsed[1]), 1);
                                july[Integer.parseInt(parsed[1])-1] = 1;
                            }else if(parsed[0].equals("8")){
                                August.add(Integer.parseInt(parsed[1]), 1);
                                august[Integer.parseInt(parsed[1])-1] = 1;
                            }else if(parsed[0].equals("9")){
                                September.add(Integer.parseInt(parsed[1]), 1);
                                september[Integer.parseInt(parsed[1])-1] = 1;
                            }
                        }
//                        for(int i=1;i<=31;i++){
//                            Log.d("july" , String.valueOf(july[i-1]));
//                        }

                        calendar = Calendar.getInstance(); //현재 날짜가 속한 달

                    } else if (response.code() == 404) {

                    }

                }

                @Override
                public void onFailure(Call<Dateinfo> call, Throwable t) {

                }
            });
        }

        //dateHashMap = new HashMap<>();

//        for(int i=0 ; i<31 ; i++){
//            if(july[i] != 1){
//                dateHashMap.put(i+1 , "current");
//            }
//        }

        saveDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDay selectDay = new SelectDay(travelDay , place);
                Call<Void> call = retrofitInterface.selectDays(selectDay);
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



        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String sDate = selectedDate.get(Calendar.DAY_OF_MONTH)
                        +"/"+(selectedDate.get(Calendar.MONTH) + 1)
                        +"/" +selectedDate.get(Calendar.YEAR);
                Toast.makeText(getApplicationContext(), sDate, Toast.LENGTH_SHORT).show();

                if((selectedDate.get(Calendar.MONTH)+1) == 7 ){
                    july[selectedDate.get(Calendar.DAY_OF_MONTH)-1] = 3;
                }
                else if((selectedDate.get(Calendar.MONTH)+1) == 8 ){
                    august[selectedDate.get(Calendar.DAY_OF_MONTH)-1] = 3;
                }
                else if((selectedDate.get(Calendar.MONTH)+1) == 9 ){
                    september[selectedDate.get(Calendar.DAY_OF_MONTH)-1] = 3;
                }

                travelDay.add((selectedDate.get(Calendar.MONTH)+1) + "/" + selectedDate.get(Calendar.DAY_OF_MONTH));

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
//                for(int i=0;i<August.size();i++){ // i = 0~30
//                    if(August.get(i) == 0){
//                        arr[0].put( i+1 , "current");
//                    }
//                }
                for (int i= 0; i<=30 ;i++){
                    if(august[i] != 1 && august[i]!=3){
                        arr[0].put(i+1 , "current");
                    }else if(august[i] ==3){
                        arr[0].put(i+1 ,"present");
                    }
                }
                break;
            case Calendar.SEPTEMBER:
                arr[0] = new HashMap<>();
                calendar.add(calendar.MONTH, 2);
//                for(int i=0;i<September.size();i++){ // i = 0~30
//                    if(September.get(i) == 0){
//                        arr[0].put( i+1 , "current");
//                    }
//                }
                for (int i= 0; i<=29 ;i++){
                    if(september[i] != 1 && september[i] !=3 ){
                        arr[0].put(i+1 , "current");
                    }else if(september[i] ==3){
                        arr[0].put(i+1 ,"present");
                    }
                }
                break;
            case Calendar.JULY:
                arr[0] = new HashMap<>();
                calendar = Calendar.getInstance();
//                for(int i=0;i<July.size();i++){ // i = 0~30
//                    if(July.get(i) == 0){
//                        arr[0].put( i+1 , "current");
//                    }
//                }
                for (int i= 0; i<=30 ;i++){
                    if(july[i] != 1 && july[i] !=3){
                        arr[0].put(i+1, "current");
                    }else if(july[i] ==3){
                        arr[0].put(i+1 ,"present");
                    }
                }
                break;
        }
        return arr;
    }

}