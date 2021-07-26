//package com.example.travel;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.view.View;
//
//import org.naishadhparmar.zcustomcalendar.CustomCalendar;
//import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
//import org.naishadhparmar.zcustomcalendar.Property;
//
//import java.util.Calendar;
//import java.util.HashMap;
//
//public class CalendarActivity extends AppCompatActivity {
//
//    CustomCalendar customCalendar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_calendar);
//
//        customCalendar = findViewById(R.id.custom_calendar);
//
//        HashMap<Object, Property> descHashMap = new HashMap<>();
//        Property defaultProperty = new Property();
//        defaultProperty.layoutResource = R.layout.default_view;
//        defaultProperty.dateTextViewResource = R.id.text_view;
//        descHashMap.put("dafault",defaultProperty);
//
//        //for current date
//        Property currentProperty = new Property();
//        currentProperty.layoutResource = R.layout.current_view;
//        currentProperty.dateTextViewResource = R.id.text_view;
//        descHashMap.put("current" , currentProperty);
//
//        //for present date
//        Property presentProperty = new Property();
//        presentProperty.layoutResource = R.layout.present_view;
//        presentProperty.dateTextViewResource =R.id.text_view;
//        descHashMap.put("present",presentProperty);
//
//        //for absent
//        Property absentProperty = new Property();
//        absentProperty.layoutResource = R.layout.absent_view;
//        absentProperty.dateTextViewResource = R.id.text_view;
//        descHashMap.put("absent",absentProperty);
//
//        //set desc hash map on custom calendar
//        customCalendar.setMapDescToProp(descHashMap);
//
//        //initialize date hash map
//        HashMap<Integer,Object> dateHashMap = new HashMap<>();
//
//        Calendar calendar= Calendar.getInstance();
//        dateHashMap.put(calendar.get(Calendar.DAY_OF_MONTH), "current");
//        dateHashMap.put(1,"present");
//        dateHashMap.put(2,"absent");
//        dateHashMap.put(3,"present");
//        dateHashMap.put(4,"absent");
//        dateHashMap.put(20,"present");
//        dateHashMap.put(30,"absent");
//
//        customCalendar.setDate(calendar,dateHashMap);
//
//        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
//                String sDate = selectedDate.get(Calendar.DAY_OF_MONTH) + "/" +(selectedDate.get(Calendar.MONTH) +1)
//                        +"/" +selectedDate.get(Calendar.YEAR);
//            }
//        });
//    }
//}