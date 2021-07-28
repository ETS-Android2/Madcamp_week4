package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travel.Adapter.PathAdapter;
import com.example.travel.items.PathItem;
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

public class UserPathActivity extends AppCompatActivity {

    private TextView title, place;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = LoginActivity.BASE_URL;
    private RecyclerView recyclerView;
    private PathAdapter pathAdapter;
    private String useremail = MainActivity.useremail;

    private ArrayList<PathItem> pathlist=new ArrayList<>();
    private ArrayList<PlaceItem> plist=new ArrayList<>();
    private String placetitle, placeregion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_path);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        title = findViewById(R.id.pathTitle);
        place = findViewById(R.id.pathPlace);

        recyclerView = findViewById(R.id.userPathRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("email", useremail);

        Call<List<Pathinfo>> call = retrofitInterface.executeUserPath(map);

        call.enqueue(new Callback<List<Pathinfo>>() {
            @Override
            public void onResponse(Call<List<Pathinfo>> call, Response<List<Pathinfo>> response) {
                if(response.code() == 200) {
                    List<Pathinfo> resultList = response.body();
//                    Log.d("check", resultList.get(0).getLocations().get(0).getAddress());
                    PathItem item = new PathItem();

                    for (Pathinfo result : resultList) {
                        Log.d("path", "" + result.getTitle());
                        item = new PathItem(result.getTitle(), result.getRegion(), result.getTotalSize().toString());
                        pathlist.add(item);
                    }

                    pathAdapter = new PathAdapter(getApplicationContext(), pathlist);
                    recyclerView.setAdapter(pathAdapter);

                    pathAdapter.setOnItemClickListener(new PathAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            placetitle =pathlist.get(position).getPathtitle();
                            placeregion=pathlist.get(position).getPathregion();
                            plist = resultList.get(position).getLocations();
                            Intent intent = new Intent(getApplicationContext(), UserPlaceActivity.class);
                            intent.putExtra("title", placetitle);
                            intent.putExtra("region", placeregion);
                            intent.putExtra("list", plist);
                            startActivity(intent);
                            overridePendingTransition( R.anim.anim_slide_in_right_fast, 0);

                        }
                    });
                }
                else if (response.code() == 400) {
                Log.d("look", "400");
                Toast.makeText(UserPathActivity.this, "Wrong Credentials", Toast.LENGTH_LONG).show();
            }

            }



            @Override
            public void onFailure(Call<List<Pathinfo>> call, Throwable t) {
                Toast.makeText(UserPathActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }

            });


    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);

    }
}