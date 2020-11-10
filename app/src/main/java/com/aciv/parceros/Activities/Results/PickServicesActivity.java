package com.aciv.parceros.Activities.Results;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aciv.parceros.Models.Database.Reaction;
import com.aciv.parceros.Models.Database.Service;
import com.aciv.parceros.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PickServicesActivity extends AppCompatActivity {
    private TextView serviceDurationTV;
    private EditText serviceNameET, serviceDescriptionET, serviceCostET;
    private Button addServiceB, doneB;
    private ImageButton backIB;
    private ChipGroup servicesCG;

    private Context context;
    private List<Service> services = new ArrayList<>();
    private String duration = "";
    private final int DURATION_CODE = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_services);

        serviceNameET = findViewById(R.id.serviceNameET);
        serviceDescriptionET = findViewById(R.id.serviceDescriptionET);
        serviceDurationTV = findViewById(R.id.serviceDurationTV);
        serviceCostET = findViewById(R.id.serviceCostET);
        servicesCG = findViewById(R.id.servicesCG);
        addServiceB = findViewById(R.id.addServiceB);
        doneB = findViewById(R.id.doneB);
        backIB = findViewById(R.id.backIB);

        context = this;

        if(getIntent().getExtras() != null){
            List<Service> servicesDB = new Gson().fromJson(getIntent().getStringExtra("services"), new TypeToken<List<Service>>(){}.getType());

            for(Service service: servicesDB)
                addService(service);

            services.addAll(servicesDB);
        }

        serviceDurationTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickServicesToPickDuration = new Intent(context, PickDurationActivity.class);

                if(!duration.equals(""))
                    pickServicesToPickDuration.putExtra("duration", duration);

                startActivityForResult(pickServicesToPickDuration, DURATION_CODE);
            }
        });

        addServiceB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = serviceNameET.getText().toString();
                final String description = serviceDescriptionET.getText().toString();
                int cost = -1;

                try{
                    cost = Integer.parseInt(serviceCostET.getText().toString());
                }catch (NumberFormatException e){
                    Log.e("cost format", e.toString());
                }

                if(!name.equals("") && !description.equals("") && !duration.equals("") && cost != -1){
                    final Service service = new Service(0, name, description, duration, cost, new ArrayList<Reaction>());

                    addService(service);

                    duration = "";
                    serviceNameET.setText("");
                    serviceDescriptionET.setText("");
                    serviceCostET.setText("");
                    serviceDurationTV.setText(getString(R.string.service_duration));
                    serviceDurationTV.setTextColor(ContextCompat.getColor(context, R.color.text));
                }else{
                    Toast.makeText(context, "Completa todos los datos del servicio", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                if(services.size() > 0){
                    Intent response = new Intent();
                    response.putExtra("services", new Gson().toJson(services));
                    setResult(RESULT_OK, response);
                    finish();
                }else{
                    Toast.makeText(context, "Ingresa mínimo un servicio", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addService(Service service){
        final Chip serviceChip = new Chip(context);
        serviceChip.setText(service.getName());
        serviceChip.setCloseIconVisible(true);
        serviceChip.setClickable(true);
        serviceChip.setTag(service);

        serviceChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Service service = (Service) v.getTag();

                serviceNameET.setText(service.getName());
                serviceDescriptionET.setText(service.getDescription());

                duration = service.getDuration();

                String[] durationParts = service.getDuration().split(":");
                serviceDurationTV.setText(durationParts[0] + " horas con " + durationParts[1] + " minutos");
                serviceDurationTV.setTextColor(ContextCompat.getColor(context, R.color.accentGradient));

                serviceCostET.setText(String.valueOf(service.getCost()));
            }
        });

        serviceChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Service service = (Service) v.getTag();

                services.remove(service);
                servicesCG.removeView(v);
            }
        });

        servicesCG.addView(serviceChip);
        services.add(service);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){
            case RESULT_OK:
                if(data != null){
                    duration = data.getStringExtra("duration");
                    String[] durationParts = duration.split(":");
                    serviceDurationTV.setText(durationParts[0] + " horas con " + durationParts[1] + " minutos");
                    serviceDurationTV.setTextColor(ContextCompat.getColor(context, R.color.accentGradient));
                }
                break;
        }
    }
}
