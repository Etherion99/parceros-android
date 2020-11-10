package com.aciv.parceros.Activities.Results;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aciv.parceros.Adapters.PlaceSuggestionsAdapter;
import com.aciv.parceros.Interfaces.PlaceDetailsInterface;
import com.aciv.parceros.Models.Database.Location;
import com.aciv.parceros.Models.Local.PlaceSuggestion;
import com.aciv.parceros.R;
import com.aciv.parceros.Tools.MapTools;
import com.aciv.parceros.Tools.SOTools;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PickLocationActivity extends AppCompatActivity {
    private EditText searchET;
    private ConstraintLayout shadowCL, placeSuggestionsCL;
    private Button openMapB, doneB;
    private ListView placeSuggestionsLV;
    private CheckBox servicesAtLocCB, travelForServiceCB;
    private TextView editMaxDistanceTV;
    private ImageButton backIB;

    private Context context;
    private Location location = new Location();
    private boolean textChanged = true, editingDistance = true;
    private Handler searchHandler = new Handler();
    private Runnable searchRunnable;
    private AutocompleteSessionToken autocompleteToken = AutocompleteSessionToken.newInstance();;
    private PlacesClient placesClient;
    private final RectangularBounds autocompleteBounds  = RectangularBounds.newInstance(new LatLng(7.035423, -73.190258), new LatLng(7.171957, -73.067366));
    private List<PlaceSuggestion> placeSuggestions = new ArrayList<>();
    private PlaceSuggestionsAdapter suggestionsAdapter;
    private int maxDistance = 0;
    private final int MAP_CODE = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);

        uiInit();

        context = this;

        if (!Places.isInitialized())
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));

        placesClient = Places.createClient(context);
        suggestionsAdapter = new PlaceSuggestionsAdapter(context, placeSuggestions);
        placeSuggestionsLV.setAdapter(suggestionsAdapter);

        setDataListeners();

        if(getIntent() != null){
            location = new Gson().fromJson(getIntent().getStringExtra("location"), Location.class);
            textChanged = false;


            searchET.setText(MapTools.getAddressByLatLng(context, new LatLng(location.getLat(), location.getLng())));
            servicesAtLocCB.setChecked(location.isWork_at_loc());


            if(location.isTravel_to_work()){
                editingDistance = false;
                travelForServiceCB.setChecked(location.isTravel_to_work());
                maxDistance = location.getMax_distance();
                editMaxDistanceTV.setText((maxDistance < 100 ? (maxDistance+1) + " km" : "Sin límite") + " (Editar)");
            }

        }

        /*location.setWork_at_loc(true);
        location.setLat(7.139851299);
        location.setLng(-73.1223114);
        Intent locationToLocationMap = new Intent(context, PickLocationMapActivity.class);
        locationToLocationMap.putExtra("location", new Gson().toJson(location));*/
        //startActivityForResult(locationToLocationMap, MAP_CODE);//debug
    }

    private void uiInit(){
        searchET = findViewById(R.id.searchET);
        shadowCL = findViewById(R.id.shadowCL);
        placeSuggestionsCL = findViewById(R.id.placeSuggestionsCL);
        openMapB = findViewById(R.id.openMapB);
        placeSuggestionsLV = findViewById(R.id.placeSuggestionsLV);
        servicesAtLocCB = findViewById(R.id.servicesAtLocCB);
        travelForServiceCB = findViewById(R.id.travelForServicesCB);
        editMaxDistanceTV = findViewById(R.id.editMaxDistanceTV);
        doneB = findViewById(R.id.doneB);
        backIB = findViewById(R.id.backIB);
    }

    private void setDataListeners(){
        //search listeners
        shadowCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchET.clearFocus();

                SOTools.hideKeyboard(context);
            }
        });

        searchET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    shadowCL.setVisibility(View.VISIBLE);
                    placeSuggestionsCL.setVisibility(View.VISIBLE);
                    searchET.setElevation(12 * context.getResources().getDisplayMetrics().density + 0.5f);
                }else{
                    shadowCL.setVisibility(View.GONE);
                    placeSuggestionsCL.setVisibility(View.GONE);
                    searchET.setElevation(0 * context.getResources().getDisplayMetrics().density + 0.5f);
                }
            }
        });

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if(textChanged){
                    location = new Location();

                    searchHandler.removeCallbacks(searchRunnable);

                    searchRunnable = new Runnable() {
                        @Override
                        public void run() {
                            searchAddress(s.toString());
                        }
                    };

                    searchHandler.postDelayed(searchRunnable, 400);
                }else{
                    textChanged = true;
                }
            }
        });

        //pick location listeners
        placeSuggestionsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                textChanged = false;

                getPlaceLoc(placeSuggestions.get(position).getId(), new PlaceDetailsInterface() {
                    @Override
                    public void onSuccess(LatLng latLng) {
                        location.setLat(latLng.latitude);
                        location.setLng(latLng.longitude);

                        searchET.setText(placeSuggestions.get(position).getName());
                        searchET.clearFocus();

                        placeSuggestions.clear();
                        suggestionsAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        openMapB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locationToLocationMap = new Intent(context, PickLocationMapActivity.class);
                locationToLocationMap.putExtra("location", new Gson().toJson(location));
                locationToLocationMap.putExtra("location_name", searchET.getText().toString());
                startActivityForResult(locationToLocationMap, MAP_CODE);
            }
        });

        //location options listeners
        servicesAtLocCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                location.setWork_at_loc(isChecked);
            }
        });

        travelForServiceCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                location.setTravel_to_work(isChecked);
                if(editingDistance){
                    if(isChecked){
                        editMaxDistance();
                    }else{
                        maxDistance = 0;
                        editMaxDistanceTV.setText("");
                        location.setMax_distance(0);
                    }
                }else{
                    editingDistance = true;
                }
            }
        });

        editMaxDistanceTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMaxDistance();
            }
        });

        //navigation listeners
        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        doneB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location.getLat() != -1 && location.getLng() != -1 && (location.isWork_at_loc() || location.isTravel_to_work())){
                    Intent response = new Intent();
                    response.putExtra("location", new Gson().toJson(location));
                    setResult(RESULT_OK, response);
                    finish();
                }else{
                    Toast.makeText(context, "Elige tu ubicación y por lo menos una de las preferencias", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void searchAddress(String query){
        FindAutocompletePredictionsRequest autocompleteRequest = FindAutocompletePredictionsRequest.builder()
                .setLocationRestriction(autocompleteBounds)
                .setSessionToken(autocompleteToken)
                .setQuery(query)
                .setCountry("co")
                .setTypeFilter(TypeFilter.GEOCODE)
                .build();

        placesClient.findAutocompletePredictions(autocompleteRequest).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onSuccess(FindAutocompletePredictionsResponse findAutocompletePredictionsResponse) {
                placeSuggestions.clear();

                for(AutocompletePrediction prediction: findAutocompletePredictionsResponse.getAutocompletePredictions())
                    placeSuggestions.add(new PlaceSuggestion(prediction.getPlaceId(), prediction.getFullText(null).toString()));

                suggestionsAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    Log.e("debug place", "Place not found: " + apiException.getMessage());
                }
            }
        });
    }

    private void editMaxDistance(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_travel_distance, null);

        final SeekBar distanceSB = dialogView.findViewById(R.id.distanceSB);
        final TextView distanceTV = dialogView.findViewById(R.id.distanceTV);
        Button doneB = dialogView.findViewById(R.id.doneB);

        distanceSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 100)
                    distanceTV.setText("Sin límite");
                else
                    distanceTV.setText((progress+1) + " km");

                location.setMax_distance(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        distanceSB.setProgress(maxDistance);

        dialogBuilder.setView(dialogView);

        final AlertDialog dialog = dialogBuilder.create();

        doneB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxDistance = distanceSB.getProgress();
                editMaxDistanceTV.setText((maxDistance < 100 ? (maxDistance+1) + " km" : "Sin límite") + " (Editar)");
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getPlaceLoc(String placeId, final PlaceDetailsInterface placeDetailsInterface){
        List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG);

        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, fields);

        placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                placeDetailsInterface.onSuccess(fetchPlaceResponse.getPlace().getLatLng());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("location", "error latlng place details");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){
            case RESULT_OK:
                if(data != null){
                    textChanged = false;

                    location = new Gson().fromJson(data.getStringExtra("location"), Location.class);
                    searchET.setText(data.getStringExtra("location_name"));
                }
                break;
            case RESULT_CANCELED:
                break;
        }
    }
}
