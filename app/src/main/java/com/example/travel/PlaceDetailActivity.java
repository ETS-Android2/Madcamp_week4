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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel.items.Placeinfo;
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
    private String BASE_URL = LoginActivity.BASE_URL;
    private String title, region;
    private Placeinfo place;
    private TextView tvTitle, tvRegion, tvAddress;
    private Placeinfo result;
    private ArrayList<String> mImages;

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
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
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
                result = response.body();
                mImages = result.getImage();

                mAdapter.setData(mImages);
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
        mAdapter.setData(null);
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

    // 사진 uri, path를 db에 저장하기
    private void saveImage(List <Uri> uris, List<String> paths) throws IOException {
//        Log.d("kyung", place.getAddress());

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
            String filename = MainActivity.useremail+"_"+title+"_"+region+"_"+place.getAddress()+"_"+orientation+"_"+System.currentTimeMillis()+".jpg";
            surveyImagesParts[i] = MultipartBody.Part.createFormData("image", filename, requestFile);

//            Log.d("kyung", String.valueOf(orientation));

            //mImages에 추가
            mImages.add(filename);
        }

        Call<Void> call = retrofitInterface.uploadImage(surveyImagesParts);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                SaveImageResponse saveImageResponse = response.body();
                Log.d("kyung", "success");
                mAdapter.setData(mImages);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("kyung", "onFailure: "+t.getLocalizedMessage());
            }
        });

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

    private class UriAdapter extends RecyclerView.Adapter<UriAdapter.UriViewHolder> {

        private List<String> mImageTitles;


        void setData(List<String> imageTitles) {
            mImageTitles = imageTitles;
            notifyDataSetChanged();
        }

        @Override
        public UriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new UriViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.row_custom_recycler_item, parent, false));
        }

        @Override
        public void onBindViewHolder(UriViewHolder holder, int position) {
//            Glide.with(holder.row_image.getContext()).load(R.drawable.whale).into(holder.row_image);

            // mImageTitles.get(position) 사진 db에서 불러와서 띄우기
            HashMap<String, String> map = new HashMap<>();

            map.put("name", mImageTitles.get(position));
            Call<ResponseBody> callImage = retrofitInterface.getImage(map);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            callImage.enqueue(new Callback<ResponseBody>(){
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    InputStream is = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    // filename에서 orientation 가져오기
                    String[] tmpName = mImageTitles.get(position).split("_");
//                    Log.d("kyung", String.valueOf(tmpName));
//                    Log.d("kyung", tmpName[4]);

                    // 사진 회전 처리
                    Bitmap bmRotated = rotateBitmap(bitmap, Integer.parseInt(tmpName[4]));
//                    images.add(position, bitmap);
                    holder.row_image.setImageBitmap(bmRotated);

//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bmRotated.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    byte[] byteArray = stream.toByteArray();

//                    imageByteArray.add(position, byteArray);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t){
                    Log.d("bitmapfail", "String.valueOf(bitmap)");
                    Toast.makeText(PlaceDetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        }

        @Override
        public int getItemCount() {
            return mImageTitles == null ? 0 : mImageTitles.size();
        }

        class UriViewHolder extends RecyclerView.ViewHolder {
            private ImageView row_image;

            UriViewHolder(View contentView) {
                super(contentView);
                row_image = (ImageView) contentView.findViewById(R.id.row_image);

                // 이미지 짧게 누르면 이미지 확대, image slider(ImageFullActivity)
                contentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ImageFullActivity로 intent
                        Intent intent = new Intent(PlaceDetailActivity.this, ImageFullActivity.class);
                        intent.putStringArrayListExtra("image", new ArrayList<>(mImageTitles));
                        intent.putExtra("position", getAdapterPosition());
                        startActivity(intent);

//                        setContentView(R.layout.activity_image_full);
//
//                        ViewPager viewPager = findViewById(R.id.viewPager);
//                        ImageFullAdapter imageFullAdapter = new ImageFullAdapter(PlaceDetailActivity.this, imageByteArray);
//                        viewPager.setAdapter(imageFullAdapter);
//                        viewPager.setCurrentItem(getAdapterPosition());
                    }
                });

                // 이미지 길게 누르면 삭제
                contentView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //삭제 alert
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlaceDetailActivity.this)
                                .setMessage("Do you want to delete the picture?")
                                .setTitle("Delete")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(PlaceDetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                        HashMap<String, String> map = new HashMap<>();

                                        int position = getAdapterPosition();
                                        map.put("email", MainActivity.useremail);
                                        map.put("region", region);
                                        map.put("title", title);
                                        map.put("address", place.getAddress());
                                        map.put("name", mImageTitles.get(position));

                                        Call<Placeinfo> call = retrofitInterface.deleteImage(map);
                                        call.enqueue(new Callback<Placeinfo>(){
                                            @Override
                                            public void onResponse(Call<Placeinfo> call, retrofit2.Response<Placeinfo> response) {
                                                if(response.code()==200){
                                                    result = response.body();
                                                    mImages = result.getImage();

                                                    mAdapter.setData(mImages);
                                                }else if(response.code()==404){
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<Placeinfo> call, Throwable t){
                                                Log.d("failed", "connection "+call);
                                                Toast.makeText(PlaceDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                });

                        builder.create();
                        builder.show();

                        return true;
                    }
                });
            }
        }
    }

}