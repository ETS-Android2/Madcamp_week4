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
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.travel.Adapter.DrawerAdapter;
import com.example.travel.items.DrawerItem;
import com.example.travel.items.OnSwipeTouchListener;
import com.example.travel.items.SimpleItem;
import com.example.travel.items.SpaceItem;
import com.example.travel.items.Userinfo;
import com.google.android.gms.maps.model.Circle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.john.waveview.WaveView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.SlidingRootNavLayout;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import co.dift.ui.SwipeToAction;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    private CircleImageView userpic;
    private TextView name;
    private Intent classs = new Intent();
    private Intent links = new Intent();
    Bitmap bmRotated;


    private static final int POS_CLOSE=0;
    private static final int POS_MAP=0;
    private static final int POS_SEARCH=1;
    private static final int POS_MY_PROFILE=2;

    private static final int POS_LOGOUT=4;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;
    private Toolbar toolbar;

    ViewFlipper vf;
    private FragmentManager fragmentManager;
    private Retrofit retrofit;

    private RetrofitInterface retrofitInterface;
    public static String BASE_URL = "http://192.249.18.176:80";

    public static MainActivity mainActivity;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = MainActivity.this;

        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
                createItemFor(POS_MAP).setChecked(true),
                createItemFor(POS_SEARCH),
                createItemFor(POS_MY_PROFILE),
                new SpaceItem(400),
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
        Log.d("main" , username);
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
                .withIconTint(R.color.white)
                .withTextTint(R.color.white)
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
            HashMap<String, String> map = new HashMap<>();

            map.put("email", useremail);

            Call<Void> call = retrofitInterface.executeLogout(map);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code()==200){
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,R.anim.anim_slide_out_bottom);
                        finish();
                    }
                    else if(response.code()==400){
                        Toast.makeText(getApplicationContext(), "계정에 이상이 있습니다.", Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                }
            });
        }

        slidingRootNav.closeMenu();
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // 사진 회전 처리 함수
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}