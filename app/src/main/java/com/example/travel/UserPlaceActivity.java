package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.travel.Adapter.PathAdapter;
import com.example.travel.Adapter.PlaceAdapter;
import com.example.travel.items.Pathinfo;
import com.example.travel.items.Placeinfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserPlaceActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = LoginActivity.BASE_URL;
    private RecyclerView recyclerView;
    private String useremail = MainActivity.useremail;
    private String title, region, latitude, longtitude;
    public static ArrayList<Placeinfo> plist;
    private ArrayList<Placeinfo> ilist;
    private PlaceAdapter placeAdapter;

    private FloatingActionButton topathMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_place);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        region = intent.getStringExtra("region");
        plist = (ArrayList<Placeinfo>)intent.getSerializableExtra("list");


        recyclerView = findViewById(R.id.userPlaceRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        placeAdapter = new PlaceAdapter(getApplicationContext(), plist);
        recyclerView.setAdapter(placeAdapter);

        placeAdapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                latitude =plist.get(position).getLatitude();
//                longtitude=plist.get(position).getLongtitude();
                Intent intent = new Intent(getApplicationContext(), PlaceDetailActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("region", region);
                intent.putExtra("place", plist.get(position));
                startActivity(intent);
//                return;
            }
        });

        topathMap = (FloatingActionButton)findViewById(R.id.bt_toPathMap);
        topathMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPlaceActivity.this, PathMapActivity.class);
                intent.putExtra("plist" , plist);
                startActivity(intent);
            }
        });
    }

}