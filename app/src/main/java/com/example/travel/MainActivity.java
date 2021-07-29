package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.widget.Toolbar;

import com.example.travel.Adapter.DrawerAdapter;
import com.example.travel.items.DrawerItem;
import com.example.travel.items.OnSwipeTouchListener;
import com.example.travel.items.SimpleItem;
import com.example.travel.items.SpaceItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.john.waveview.WaveView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.SlidingRootNavLayout;

import co.dift.ui.SwipeToAction;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    public static String username, useremail;
//    private SeekBar seekBar;
//    private WaveView waveView;
//    private CardView mapcard, card;
//    private FloatingActionButton toProf, toSearch;
//    private EditText place;
//    private Button placebtn ,bt_calendar;
//    private RecyclerView recyclerView;
//    private MainAdapter mainAdapter;

    private Intent classs = new Intent();
    private Intent links = new Intent();

    private static final int POS_CLOSE=0;
    private static final int POS_MAP=1;
    private static final int POS_SEARCH=2;
    private static final int POS_MY_PROFILE=3;

    private static final int POS_LOGOUT=5;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;
    private Toolbar toolbar;

    ViewFlipper vf;
    private FragmentManager fragmentManager;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

//        initialize(savedInstanceState);
//        initializeLogic();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        vf = findViewById(R.id.vf);


        slidingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(180)
                .withRootViewScale(0.75f)
                .withRootViewElevation(25)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer)
                .inject();


        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_CLOSE),
                createItemFor(POS_MAP).setChecked(true),
                createItemFor(POS_SEARCH),
                createItemFor(POS_MY_PROFILE),
                new SpaceItem(260),
                createItemFor(POS_LOGOUT)

                ));

        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.drawer_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_MAP);



        Intent intent = getIntent();
        username = intent.getStringExtra("name");
        useremail = intent.getStringExtra("email");



//        pic = findViewById(R.id.mainpic);
//
//        pic.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()){
//            @Override
//            public void onSwipeRight() {
//                super.onSwipeRight();
//                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
//                startActivity(intent);
//                overridePendingTransition(0,0);
//            }
//
//            @Override
//            public void onSwipeLeft() {
//                super.onSwipeLeft();
//
//                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
//                intent.putExtra("place", "대한민국");
//                startActivity(intent);
//                overridePendingTransition(0,0);
//            }
//
//            @Override
//            public void onSwipeBottom() {
//                super.onSwipeBottom();
//                Intent intent = new Intent(getApplicationContext(), UserPathActivity.class);
//                startActivity(intent);
//
////                overridePendingTransition(R.anim.anim_slide_in_right_fast, 0);
//            }
//        });
//
//
//
//        swipeToAction = new SwipeToAction(toProf, new SwipeToAction.SwipeListener() {
//            @Override
//            public boolean swipeLeft(Object itemData) {
//                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
//                intent.putExtra("place", "대한민국");
//                startActivity(intent);
//                overridePendingTransition(0,0);
//                return true;
//
//            }
//
//            @Override
//            public boolean swipeRight(Object itemData) {
//                return true;
//            }
//
//            @Override
//            public void onClick(Object itemData) {
//                Intent intent = new Intent(getApplicationContext(), UserPathActivity.class);
//                startActivity(intent);
//
//                overridePendingTransition(R.anim.anim_slide_in_right_fast, 0);
//            }
//
//            @Override
//            public void onLongClick(Object itemData) {
//
//            }
//        });



//
//        toProf = findViewById(R.id.toProfile);
//        toSearch = findViewById(R.id.toSearch);

//        place = findViewById(R.id.editPlace);
//        placebtn = findViewById(R.id.placeBtn);

//        card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), UserPathActivity.class);
//                startActivity(intent);
//            }
//        });

//        toProf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), UserPathActivity.class);
//                startActivity(intent);
//
//                overridePendingTransition(R.anim.anim_slide_in_right_fast, 0);
//            }
//        });
//
//        toSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), OtherPathActivity.class);
//                startActivity(intent);
//
//                overridePendingTransition(R.anim.anim_slide_in_left_fast, 0);
//
//            }
//        });

//        placebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
//                intent.putExtra("place", place.getText().toString());
//                startActivity(intent);
//                overridePendingTransition(0,0);
//            }
//        });
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                waveView.setProgress(progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.id_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta =  getResources().obtainTypedArray(R.array.id_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i=0;i<ta.length();i++){
            int id = ta.getResourceId(i, 0);
            if(id!=0){
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;

    }


    private DrawerItem createItemFor(int position){
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(R.color.colorPrimary)
                .withTextTint(R.color.black)
                .withSelectedIconTint(R.color.colorPrimary)
                .withSelectedTextTint(R.color.colorPrimary);

    }

    private long time= 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "두번 뒤로가시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() - time < 2000) {
            finish();
        }
    }


    @Override
    public void onItemSelected(int position) {

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        if(position==POS_MAP){
            //vf.setDisplayedChild(0);
            transaction.replace(R.id.container, new FrontActivity());


        }
        else if(position==POS_SEARCH){
           // vf.setDisplayedChild(1);
            transaction.replace(R.id.container, new OtherPathActivity());

        }
        else if(position==POS_MY_PROFILE){
//            vf.setDisplayedChild(2);
            transaction.replace(R.id.container, new MyPageActivity());

        }
        else if(position==POS_LOGOUT){

        }

        slidingRootNav.closeMenu();
        transaction.commit();
    }
}