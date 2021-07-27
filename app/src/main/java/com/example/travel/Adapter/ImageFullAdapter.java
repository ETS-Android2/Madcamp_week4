package com.example.travel.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.travel.ImageFullActivity;
import com.example.travel.LoginActivity;
import com.example.travel.PlaceDetailActivity;
import com.example.travel.R;
import com.example.travel.RetrofitInterface;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageFullAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> imageTitles;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = LoginActivity.BASE_URL;

    public ImageFullAdapter(Context ctx, ArrayList<String> images) {
        context=ctx;
        imageTitles = images;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageTitles.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }


    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        // db에서 이미지 가져오기
        HashMap<String, String> map = new HashMap<>();

        map.put("name", imageTitles.get(position));
        Call<ResponseBody> callImage = retrofitInterface.getImage(map);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        callImage.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                InputStream is = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                // filename에서 orientation 가져오기
                String[] tmpName = imageTitles.get(position).split("_");

                // 사진 회전 처리
                Bitmap bmRotated = PlaceDetailActivity.rotateBitmap(bitmap, Integer.parseInt(tmpName[4]));
//                    images.add(position, bitmap);
                imageView.setImageBitmap(bmRotated);
                container.addView(imageView, 0);
//                return imageView;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
                Log.d("bitmapfail", "String.valueOf(bitmap)");
                Toast.makeText(context.getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((ImageView) object);
    }
}
