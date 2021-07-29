package com.example.travel.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel.LoginActivity;
import com.example.travel.PlaceDetailActivity;
import com.example.travel.RetrofitInterface;
import com.example.travel.items.PathItem;
import com.example.travel.items.Pathinfo;
import com.example.travel.R;
import com.example.travel.items.SelectDay;
import com.gjiazhe.panoramaimageview.GyroscopeObserver;
import com.gjiazhe.panoramaimageview.PanoramaImageView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PathAdapter extends RecyclerView.Adapter<PathAdapter.ViewHolder> {
    static Context context;
    private ArrayList<PathItem> mList = null;
    private OnItemClickListener mListener;
    private GyroscopeObserver gyroscopeObserver;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = LoginActivity.BASE_URL;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.userpath_item, parent, false);
        ViewHolder vh = new ViewHolder(view, mListener);

        return vh;
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

    @Override
    public void onBindViewHolder(@NonNull PathAdapter.ViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getPathtitle());

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //날짜 불러오기
        HashMap<String , String > datemap = new HashMap<>();
        datemap.put("title" , mList.get(position).getPathtitle());
        Call<SelectDay> calldays = retrofitInterface.getDays(datemap);

        calldays.enqueue(new Callback<SelectDay>(){
            @Override
            public void onResponse(Call<SelectDay> call, retrofit2.Response<SelectDay> response) {
                if(response.code()==200){
                   SelectDay selectDay = response.body();
                   ArrayList<String> days = selectDay.getDays();
                   if(days.size() > 1){
                       holder.days.setText(days.get(0) + " ~ " + days.get(days.size()-1));
                   }else if(days.size() == 1) {
                       holder.days.setText(days.get(0));
                   }
                }
            }

            @Override
            public void onFailure(Call<SelectDay> call, Throwable t){

            }
        });

        String imageName = mList.get(position).getImage();
//        Log.d("kyung", imageName);
        if(!mList.get(position).getImage().equals("")){
            HashMap<String, String> map = new HashMap<>();

            map.put("name", imageName);
            Call<ResponseBody> callImage = retrofitInterface.getImage(map);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            callImage.enqueue(new Callback<ResponseBody>(){
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    InputStream is = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    // filename에서 orientation 가져오기
                    String[] tmpName = mList.get(position).getImage().split("_");

                    // 사진 회전 처리
                    Bitmap bmRotated = rotateBitmap(bitmap, Integer.parseInt(tmpName[2]));
                    holder.panoramaImageView.setImageBitmap(bmRotated);
                    gyroscopeObserver.register(context);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t){
                    Log.d("bitmapfail", "String.valueOf(bitmap)");
                    Toast.makeText(context.getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            gyroscopeObserver.register(context);
        }

        gyroscopeObserver.register(context);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, days;
        PanoramaImageView panoramaImageView;

        public ViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);

            title = itemView.findViewById(R.id.pathTitle);
            days = itemView.findViewById(R.id.period);

            gyroscopeObserver = new GyroscopeObserver();
//        // Set the maximum radian the device should rotate to show image's bounds.
//        // It should be set between 0 and π/2.
//        // The default value is π/9.
            gyroscopeObserver.setMaxRotateRadian(Math.PI/9);

            panoramaImageView = (PanoramaImageView) itemView.findViewById(R.id.pathImg);
//        // Set GyroscopeObserver for PanoramaImageView.
            panoramaImageView.setGyroscopeObserver(gyroscopeObserver);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public PathAdapter(Context context, ArrayList<PathItem> mlist){
        this.context = context;
        this.mList = mlist;
    }


}
