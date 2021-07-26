/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.travel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travel.items.Placeinfo;
import com.example.travel.items.SaveImageResponse;
import com.example.travel.items.SaveUriInput;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_CHOOSE = 23;
    private UriAdapter mAdapter;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http:192.249.18.176:80";
    private String title, region;
    private Placeinfo place;
    private TextView tvTitle, tvRegion, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        region = intent.getStringExtra("region");
        place = (Placeinfo)intent.getSerializableExtra("place");

        setContentView(R.layout.activity_place_detail);
        findViewById(R.id.zhihu).setOnClickListener(this);
        tvTitle = (TextView)findViewById(R.id.title);
        tvTitle.setText(title);
        tvRegion = (TextView)findViewById(R.id.region);
        tvRegion.setText(region);
        tvAddress = (TextView)findViewById(R.id.address);
        tvAddress.setText(place.getAddress());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new UriAdapter());

        // db에서 해당 장소와 관련된 데이터들 불러오기
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("email", MainActivity.useremail);
        map.put("region", region);
        map.put("title", title);
        map.put("address", place.getAddress());

        Call<Placeinfo> call = retrofitInterface.executeSingleSpot(map);

        call.enqueue(new Callback<Placeinfo>() {
            @Override
            public void onResponse(Call<Placeinfo> call, retrofit2.Response<Placeinfo> response) {
                Placeinfo result = response.body();
                if(result.getImage().size() > 0){
                    //이미지 불러서 glide 설정

                }
            }

            @Override
            public void onFailure(Call<Placeinfo> call, Throwable t) {
                
            }
        });
    }


    // 사진 새로 업로드
    // <editor-fold defaultstate="collapsed" desc="onClick">
    @SuppressLint("CheckResult")
    @Override
    public void onClick(final View v) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        startAction(v);
                    } else {
                        Toast.makeText(PlaceDetailActivity.this, R.string.permission_request_denied, Toast.LENGTH_LONG)
                                .show();
                    }
                }, Throwable::printStackTrace);
    }
    // </editor-fold>

    private void startAction(View v) {
        Matisse.from(PlaceDetailActivity.this)
            .choose(MimeType.ofImage(), false)
            .countable(true)
            .capture(true)
            .captureStrategy(
                    new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test"))
            .maxSelectable(10)
            .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
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
        mAdapter.setData(null, null);
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
            mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
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

    // 사진 uri, path를 db에 저장하기
    private void saveImage(List <Uri> uris, List<String> paths) throws IOException {
//        Log.d("kyung", place.getAddress());

        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[uris.size()];

        for(int i=0;i<uris.size(); i++){
            InputStream iStream = getContentResolver().openInputStream(uris.get(i));
            byte[] imageBytes = getBytes(iStream);

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
            String filename = MainActivity.useremail+"_"+title+"_"+region+"_"+place.getAddress()+"_"+i+"_"+new Date()+".jpg";
            surveyImagesParts[i] = MultipartBody.Part.createFormData("image", filename, requestFile);

            Log.d("kyung", place.getAddress());
        }

            Call<Void> call = retrofitInterface.uploadImage(surveyImagesParts);

//            Log.d("kyung", "check1");

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                SaveImageResponse saveImageResponse = response.body();
                    Log.d("kyung", "success");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d("kyung", "onFailure: "+t.getLocalizedMessage());
                }
            });

    }

    private static class UriAdapter extends RecyclerView.Adapter<UriAdapter.UriViewHolder> {

        private List<Uri> mUris;
        private List<String> mPaths;

        void setData(List<Uri> uris, List<String> paths) {
            mUris = uris;
            mPaths = paths;
            notifyDataSetChanged();
        }

        @Override
        public UriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new UriViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.uri_item, parent, false));
        }

        @Override
        public void onBindViewHolder(UriViewHolder holder, int position) {
            Glide.with(holder.iv.getContext()).load(mUris.get(position)).into(holder.iv);
        }

        @Override
        public int getItemCount() {
            return mUris == null ? 0 : mUris.size();
        }

        static class UriViewHolder extends RecyclerView.ViewHolder {
            private ImageView iv;

            UriViewHolder(View contentView) {
                super(contentView);
                iv = (ImageView) contentView.findViewById(R.id.iv);
            }
        }
    }

}