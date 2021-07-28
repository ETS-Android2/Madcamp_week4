package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Map;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.travel.items.OnSwipeTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.john.waveview.WaveView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import co.dift.ui.SwipeToAction;

public class MainActivity extends AppCompatActivity {

    public static String username, useremail;
//    private SeekBar seekBar;
//    private WaveView waveView;
//    private CardView mapcard, card;
//    private FloatingActionButton toProf, toSearch;
//    private EditText place;
//    private Button placebtn ,bt_calendar;
//    private RecyclerView recyclerView;
//    private MainAdapter mainAdapter;
    private ImageView pic;
    private SwipeToAction swipeToAction;
    private  FlowingDrawer mDrawer;
    private FrameLayout containermenu;

    private Intent classs = new Intent();
    private Intent links = new Intent();

    private ResideMenu resideMenu;

//    ArrayList<Main> mains = new ArrayList<>();


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


        Intent intent = getIntent();
        username = intent.getStringExtra("name");
        useremail = intent.getStringExtra("email");

        // attach to current activity;
        resideMenu = new ResideMenu(this);
//        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);

        // create menu items;
        String titles[] = { "Home", "Gallery", "Calendar", "Settings" };
        int icon[] = { R.drawable.whalehome, R.drawable.whalehome, R.drawable.whalehome, R.drawable.whalehome };

        for (int i = 0; i < titles.length; i++){
            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            resideMenu.addMenuItem(item,  ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        }

        resideMenu.openMenu(ResideMenu.DIRECTION_LEFT); // or ResideMenu.DIRECTION_RIGHT
        resideMenu.closeMenu();

        resideMenu.setMenuListener(menuListener);
        private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
            @Override
            public void openMenu() {
                Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void closeMenu() {
                Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
            }
        };

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

    private void initialize(Bundle savedInstanceState) {
        containermenu =(FrameLayout) findViewById(R.id.containermenu);
        mDrawer =(FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);

        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);



            }
        });


    }

    private void initializeLogic() {
        containermenu.setBackgroundColor(0xFFFFFFFF);
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



}