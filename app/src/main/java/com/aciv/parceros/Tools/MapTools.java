package com.aciv.parceros.Tools;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapTools {
    public static String getAddressByLatLng(Context context, LatLng latLng){
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if(addresses != null && addresses.size() > 0){
                Log.w("debug", new Gson().toJson(addresses.get(0).getCountryCode()));
                Address address = addresses.get(0);
                StringBuilder addressBuilder = new StringBuilder();

                for(int i = 0; i <= address.getMaxAddressLineIndex(); i++)
                    addressBuilder.append(address.getAddressLine(i));

                return addressBuilder.toString();
            }
        } catch (IOException e) {
            Log.e("geocoder", "error camera moved geocoder");
            return "";
        }

        return "";
    }
}
