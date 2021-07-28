package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridView;
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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyPageActivity extends AppCompatActivity {

    private String username = MainActivity.username;
    private String useremail = MainActivity.useremail;

    private ArrayList<PathItem> pathlist=new ArrayList<>();
    private ArrayList<Placeinfo> plist=new ArrayList<>();
    private PathAdapter pathAdapter;

    TextView tvPosts, tvFriends , displayName;
    EditText description;
    CircleImageView profileImg;
    RecyclerView recyclerView;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = LoginActivity.BASE_URL;

    private String placetitle, placeregion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        tvPosts = findViewById(R.id.tvPosts);
        tvFriends = findViewById(R.id.tvFriends);
        displayName = findViewById(R.id.display_name);
        description = findViewById(R.id.description);
        profileImg = findViewById(R.id.profile_image);
        recyclerView = findViewById(R.id.MyPageRecycler);

        displayName.setText(username);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
                        item = new PathItem(result.getTitle() , result.getRegion(),result.getTotalSize().toString());
                        pathlist.add(item);

                    }

                    tvPosts.setText(String.valueOf(pathlist.size()));

                    pathAdapter = new PathAdapter(getApplicationContext(), pathlist);
                    recyclerView.setAdapter(pathAdapter);

                    pathAdapter.setOnItemClickListener(new PathAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            placetitle =pathlist.get(position).getPathtitle();
                            placeregion = resultList.get(position).getRegion();
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
                    Toast.makeText(MyPageActivity.this, "Wrong Credentials", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pathinfo>> call, Throwable t) {
                Toast.makeText(MyPageActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }

        });
    }
}