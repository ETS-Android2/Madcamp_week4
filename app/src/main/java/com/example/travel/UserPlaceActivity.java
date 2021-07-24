package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.travel.Adapter.PathAdapter;
import com.example.travel.Adapter.PlaceAdapter;
import com.example.travel.items.Pathinfo;
import com.example.travel.items.Placeinfo;

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
    private ArrayList<Placeinfo> plist;
    private ArrayList<Placeinfo> ilist;
    private PlaceAdapter placeAdapter;


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
                latitude =plist.get(position).getLatitude();
                longtitude=plist.get(position).getLongtitude();
                return;

            }
        });




    }

}