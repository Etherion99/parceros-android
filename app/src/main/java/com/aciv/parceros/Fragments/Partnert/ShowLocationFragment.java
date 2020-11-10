package com.aciv.parceros.Fragments.Partnert;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aciv.parceros.Interfaces.RetrofitInterface;
import com.aciv.parceros.Models.Database.Location;
import com.aciv.parceros.R;
import com.aciv.parceros.Tools.RetrofitTools;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowLocationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_partner_show_location, container, false);

        final ImageView mapIV = root.findViewById(R.id.mapIV);
        final ImageView locationRadioIV = root.findViewById(R.id.locationRadioIV);

        final Context context = getActivity();
        RetrofitInterface retrofit = RetrofitTools.getInterface();

        Call<Location> showLocationCall = retrofit.showLocationByUser(8);

        showLocationCall.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if(response.body() != null){
                    Log.e("location", new Gson().toJson(response.body()));
                    Location location = response.body();

                    Glide.with(context)
                            .load("https://maps.googleapis.com/maps/api/staticmap?center=" + location.getLat() + ", " + location.getLng() + "&zoom=15&size=400x600&maptype=roadmap&key=AIzaSyDiuQwvNSe-Tl3oqXCk6GFR22PGe1Bovqo")
                            .into(mapIV);

                    locationRadioIV.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(context, "error location", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                Log.e("location error", t.getMessage());
            }
        });

        return root;
    }

}
