package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.travel.items.Dateinfo;
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
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinCalendarActivity extends AppCompatActivity  implements OnNavigationButtonClickedListener {

    CustomCalendar customCalendar;
    Calendar calendar;
    HashMap<Integer,Object> dateHashMap;
    ArrayList<String> friends = new ArrayList<>();

    ArrayList<Integer> July = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)); //31
    ArrayList<Integer> August = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)); //31
    ArrayList<Integer> September = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)); //30

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    public static String BASE_URL = LoginActivity.BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_calendar);

        Intent intent =getIntent(); //친구 목록 받음.
        friends = (ArrayList<String>) intent.getSerializableExtra("friendlist");

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //for문 돌면서 친구들 date list 하나씩 받아옴
        for(int i=0;i<friends.size();i++) {

            HashMap<String, String> map = new HashMap<>();
            map.put("email", friends.get(i));
            Call<Dateinfo> call = retrofitInterface.getFriendDays(map);

            call.enqueue(new Callback<Dateinfo>() {
                @Override
                public void onResponse(Call<Dateinfo> call, Response<Dateinfo> response) {

                    if (response.code() == 200) {
                        Dateinfo result = response.body();
//                        for(int j=0;j<result.getDays().size();j++) {
//                            Log.d("chk", result.getDays().get(j));
//                        }
                        ArrayList<String> days = new ArrayList<String>();
                        days = result.getDays();

                        for(int j=0;j<days.size();j++){
                            String [] parsed = days.get(j).split("/");
                            if(parsed[0] == "7"){
                                July.add(Integer.parseInt(parsed[1]), 1);
                            }else if(parsed[0] =="8"){
                                August.add(Integer.parseInt(parsed[1]), 1);
                            }else if(parsed[0] == "9"){
                                September.add(Integer.parseInt(parsed[1]), 1);
                            }
                        }

                    } else if (response.code() == 404) {

                    }

                }

                @Override
                public void onFailure(Call<Dateinfo> call, Throwable t) {

                }
            });
        }

        customCalendar = findViewById(R.id.join_calendar);

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

//        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
//                String sDate = selectedDate.get(Calendar.DAY_OF_MONTH)
//                        +"/"+(selectedDate.get(Calendar.MONTH) + 1)
//                        +"/" +selectedDate.get(Calendar.YEAR);
//                Toast.makeText(getApplicationContext(), sDate, Toast.LENGTH_SHORT).show();
//
//            }
//        });

        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);
    }
    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[2];
        switch(newMonth.get(Calendar.MONTH)) {
            case Calendar.AUGUST:
                arr[0] = new HashMap<>();
                for(int i=0;i<August.size();i++){ // i = 0~30
                    if(August.get(i) == 0){
                        arr[0].put( i+1 , "current");
                    }
                }
                break;
            case Calendar.SEPTEMBER:
                arr[0] = new HashMap<>();
                for(int i=0;i<September.size();i++){ // i = 0~30
                    if(September.get(i) == 0){
                        arr[0].put( i+1 , "current");
                    }
                }
                break;
            case Calendar.JULY:
                arr[0] = new HashMap<>();
                for(int i=0;i<July.size();i++){ // i = 0~30
                    if(July.get(i) == 0){
                        arr[0].put( i+1 , "current");
                    }
                }
                break;
        }
        return arr;
    }

}