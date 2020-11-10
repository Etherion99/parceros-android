package com.aciv.parceros.Activities.Results;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aciv.parceros.Models.Database.Location;
import com.aciv.parceros.R;
import com.aciv.parceros.Tools.MapTools;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PickLocationMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private ImageButton backIB;
    private Button selectB;
    private TextView addressTV;

    private Context context;
    private GoogleMap map;
    private Location location = new Location();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location_map);

        backIB = findViewById(R.id.backIB);
        addressTV = findViewById(R.id.addressTV);
        selectB = findViewById(R.id.selectB);

        context = this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        selectB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent response = new Intent();
                response.putExtra("location", new Gson().toJson(location));
                response.putExtra("location_name", addressTV.getText());
                setResult(RESULT_OK, response);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMyLocationEnabled(true);

        location = new Gson().fromJson(getIntent().getStringExtra("location"), Location.class);

        if(location.getLat() == -1 && location.getLng() == -1){
            autoLoc();
        }else{
            addressTV.setText(getIntent().getStringExtra("location_name"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLat(), location.getLng()), 18));
        }

        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                addressTV.setText("Buscando...");
            }
        });

        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng cameraPos = map.getCameraPosition().target;
                location.setLat(cameraPos.latitude);
                location.setLng(cameraPos.longitude);

                addressTV.setText(MapTools.getAddressByLatLng(context, cameraPos));
            }
        });
    }

    private void autoLoc(){
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(context);

        locationClient.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<android.location.Location>() {
            @Override
            public void onSuccess(android.location.Location location) {
                if(location != null){
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
                }else{
                    Log.w("debug", "error last location");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("errro autoloc", e.toString());
            }
        });
    }
}
