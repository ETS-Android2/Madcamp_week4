package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travel.Adapter.ExpandableAdapter;
import com.example.travel.items.ExpandableItem;
import com.example.travel.items.Pathinfo;
import com.example.travel.items.PlaceItem;
import com.example.travel.items.Placeinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtherPathActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    //    public ArrayList<ExpandableItem> data;
    private EditText editText;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http:192.249.18.176";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_path);

        editText = findViewById(R.id.editSearch);
        mRecyclerView = findViewById(R.id.expandableRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        data = new ArrayList<>();

        // db에서 데이터들 가져오기
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<List<Pathinfo>> call = retrofitInterface.getAllPath();

        call.enqueue(new Callback<List<Pathinfo>>(){
            @Override
            public void onResponse(Call<List<Pathinfo>> call, retrofit2.Response<List<Pathinfo>> response) {
                if(response.code()==200){
                    List<Pathinfo> result = response.body();

                    editText.addTextChangedListener(new TextWatcher() {
                        // 입력하기 전
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            ArrayList<ExpandableItem> data = new ArrayList<>();

                            for(int i=0;i<result.size();i++){
                                Pathinfo tmpResult = result.get(i);
                                ExpandableItem tmpItem = new ExpandableItem(ExpandableAdapter.HEADER, tmpResult.getRegion(), tmpResult.getTitle());

                                tmpItem.invisibleChildren = new ArrayList<>();
                                for(int j=0;j<tmpResult.getTotalSize();j++){
                                    Placeinfo tmpPlace = tmpResult.getLocations().get(j);
                                    tmpItem.invisibleChildren.add(new ExpandableItem(ExpandableAdapter.CHILD, tmpPlace.getAddress()));
                                }
                                data.add(tmpItem);
                            }

                            mRecyclerView.setAdapter(new ExpandableAdapter(data));
                        }

                        // 입력란에 변화가 있을 때
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            ArrayList<ExpandableItem> data = new ArrayList<>();

                            for(int i=0;i<result.size();i++){
                                Pathinfo tmpResult = result.get(i);
                                if(tmpResult.getRegion().contains(s)){

                                    ExpandableItem tmpItem = new ExpandableItem(ExpandableAdapter.HEADER, tmpResult.getRegion(), tmpResult.getTitle());

                                    tmpItem.invisibleChildren = new ArrayList<>();
                                    for(int j=0;j<tmpResult.getTotalSize();j++){
                                        Placeinfo tmpPlace = tmpResult.getLocations().get(j);
                                        tmpItem.invisibleChildren.add(new ExpandableItem(ExpandableAdapter.CHILD, tmpPlace.getAddress()));
                                    }
                                    data.add(tmpItem);
                                }
                            }
                            mRecyclerView.setAdapter(new ExpandableAdapter(data));
                        }

                        // 입력이 끝났을 때
                        @Override
                        public void afterTextChanged(Editable s) {
                            ArrayList<ExpandableItem> data = new ArrayList<>();

                            for(int i=0;i<result.size();i++){
                                Pathinfo tmpResult = result.get(i);
                                if(tmpResult.getRegion().contains(s)){

                                    ExpandableItem tmpItem = new ExpandableItem(ExpandableAdapter.HEADER, tmpResult.getRegion(), tmpResult.getTitle());

                                    tmpItem.invisibleChildren = new ArrayList<>();
                                    for(int j=0;j<tmpResult.getTotalSize();j++){
                                        Placeinfo tmpPlace = tmpResult.getLocations().get(j);
                                        tmpItem.invisibleChildren.add(new ExpandableItem(ExpandableAdapter.CHILD, tmpPlace.getAddress()));
                                    }
                                    data.add(tmpItem);
                                }
                            }
                            mRecyclerView.setAdapter(new ExpandableAdapter(data));
                        }
                    });
                }else if(response.code()==404){
                    Toast.makeText(OtherPathActivity.this,"No Courses", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pathinfo>> call, Throwable t){
                Log.d("failed", "connection "+call);
                Toast.makeText(OtherPathActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, R.anim.anim_slide_out_right_fast);
    }
}