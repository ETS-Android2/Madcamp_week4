package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travel.Adapter.PathAdapter;
import com.example.travel.Adapter.PlaceAdapter;
import com.example.travel.items.Pathinfo;
import com.example.travel.items.Placeinfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    private static final int REQUEST_CODE_CHOOSE = 23;

    private FloatingActionButton topathMap;
    private Button imagePickBtn, imageDeleteBtn;


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

        imageDeleteBtn = findViewById(R.id.deletePic);
        // 여행 대표 사진 삭제하기
        imageDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> mapDel = new HashMap<>();

                mapDel.put("region",region);
                mapDel.put("title", title);
                Call<Void> callImage = retrofitInterface.deleteThumbnail(mapDel);

                callImage.enqueue(new Callback<Void>(){
                    @Override
                    public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                        if(response.code()==200){
                            Toast.makeText(UserPlaceActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t){
                        Toast.makeText(UserPlaceActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        imagePickBtn = findViewById(R.id.zhihu);
        // 여행 대표 사진 설정하기
        imagePickBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v){
                RxPermissions rxPermissions = new RxPermissions(UserPlaceActivity.this);
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                startAction(v);
                            } else {
                                Toast.makeText(UserPlaceActivity.this, R.string.permission_request_denied, Toast.LENGTH_LONG)
                                        .show();
                            }
                        }, Throwable::printStackTrace);
            }
        });
    }

    private void startAction(View v) {
        Matisse.from(UserPlaceActivity.this)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test"))
                .maxSelectable(1)
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .setOnSelectedListener((uriList, pathList) -> {
                    Log.e("onSelected", "onSelected: pathList=" + pathList);
                })
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(isChecked -> {
                    Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                })
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            try {
                saveImage(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("OnActivityResult ", String.valueOf(Matisse.obtainOriginalState(data)));
        }
    }

    // uri를 입력받아 byte array로 변환
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    // 여행 대표 사진을 db에 저장하기
    private void saveImage(List <Uri> uris, List<String> paths) throws IOException {
        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[uris.size()];

        for(int i=0;i<uris.size(); i++){
            InputStream iStream = getContentResolver().openInputStream(uris.get(i));
            byte[] imageBytes = getBytes(iStream);

            //사진 orientation 저장하기
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(paths.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
            String filename = region+"_"+title+"_"+orientation+"_.jpg";
            surveyImagesParts[i] = MultipartBody.Part.createFormData("image", filename, requestFile);
        }

        Call<Void> call = retrofitInterface.uploadThumbnail(surveyImagesParts);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                Toast.makeText(UserPlaceActivity.this, "Success", Toast.LENGTH_SHORT)
                        .show();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserPlaceActivity.this, "Failed", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);

    }

}