package com.example.travel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.directions.route.RoutingListener;
import com.example.travel.items.Placeinfo;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

public class PathMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<Placeinfo> plist = new ArrayList<Placeinfo>();
    private ArrayList<LatLng> latLngList = new ArrayList<LatLng>();
    private GoogleMap mMap;
    Polyline polyline = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.path_map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        plist = (ArrayList<Placeinfo>) intent.getSerializableExtra("plist");
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap = googleMap;
        Random random = new Random();
        for(int i=0;i<plist.size();i++){
            MarkerOptions mOptions = new MarkerOptions();
            LatLng point = new LatLng(Double.parseDouble(plist.get(i).getLatitude()), Double.parseDouble(plist.get(i).getLongtitude()));
            mOptions.title(plist.get(i).getAddress());
            mOptions.position(point);
            mOptions.snippet((i+1)+"번째 목적지");

            mOptions.icon(BitmapDescriptorFactory.defaultMarker(random.nextFloat()*360));
            mMap.addMarker(mOptions);

            latLngList.add(new LatLng(Double.parseDouble(plist.get(i).getLatitude()), Double.parseDouble(plist.get(i).getLongtitude())));
        }

        if(polyline != null) polyline.remove(); // 경로 그냥 다각형으로 그려주는 코드
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(latLngList).clickable(true)
                .color(Color.parseColor("#767676"));
        //polyline.setColor(ContextCompat.getColor(PathMapActivity.this ,R.color.colorPrimary));
        polyline = mMap.addPolyline(polylineOptions);

        LatLng start = new LatLng(Double.parseDouble(plist.get(0).getLatitude()) , Double.parseDouble(plist.get(0).getLongtitude()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start,13));
    }
}