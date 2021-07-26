package com.example.travel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class PathMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<Placeinfo> plist = new ArrayList<Placeinfo>();
    private GoogleMap mMap;

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
        }
        LatLng start = new LatLng(Double.parseDouble(plist.get(0).getLatitude()) , Double.parseDouble(plist.get(0).getLongtitude()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start,13));
    }
}