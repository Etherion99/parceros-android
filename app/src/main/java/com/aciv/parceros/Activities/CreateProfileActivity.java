package com.aciv.parceros.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aciv.parceros.Activities.Results.InputDescriptionActivity;
import com.aciv.parceros.Activities.Results.PickCancellationPolicyActivity;
import com.aciv.parceros.Activities.Results.PickLocationActivity;
import com.aciv.parceros.Activities.Results.PickPictureActivity;
import com.aciv.parceros.Activities.Results.PickProfessionActivity;
import com.aciv.parceros.Activities.Results.PickServicesActivity;
import com.aciv.parceros.Activities.Results.PickWorkScheduleActivity;
import com.aciv.parceros.Interfaces.RetrofitInterface;
import com.aciv.parceros.Models.Database.Location;
import com.aciv.parceros.Models.Database.Phone;
import com.aciv.parceros.Models.Database.Profession;
import com.aciv.parceros.Models.Database.APIResponse;
import com.aciv.parceros.Models.Database.Profile;
import com.aciv.parceros.Models.Database.Schedule;
import com.aciv.parceros.Models.Database.Service;
import com.aciv.parceros.Models.Database.SignupUser;
import com.aciv.parceros.Models.Database.User;
import com.aciv.parceros.R;
import com.aciv.parceros.Tools.RetrofitTools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProfileActivity extends AppCompatActivity {
    private TextView indicator1TV, indicator2TV, indicator3TV;
    private Button phoneB, nameB, emailB, professionB, hoursB, locationB, servicesB, pictureB, descriptionB, cancellationB, nextB;
    private ImageButton backIB;
    private ConstraintLayout step1CL, step2CL, step3CL;

    private final int PHONE_CODE = 8;
    private final int PROFESSION_CODE = 18;
    private final int LOCATION_CODE = 28;
    private final int WORK_SCHEDULE_CODE = 38;
    private final int SERVICES_CODE = 48;
    private final int PICTURE_CODE = 58;
    private final int DESCRIPTION_CODE = 68;
    private final int CANCELLATION_CODE = 78;

    private Context context;
    private LayoutInflater inflater;
    private int profileStep = 1;
    private TextView[] indicators;
    private ConstraintLayout[] containers;

    private Button[][] buttons;
    private boolean[][] validations;

    private User user = new User(/*0, "juan sebsatian", "juanstt99@gmail.com", new Phone("3104778865", "57")*/);//debug
    private Profile profile = new Profile(/*new Profession(4, ""), 3, 4, false, "descripcion perfil"*/);//debug
    private List<Schedule> schedules = new ArrayList<>();
    private Location location = new Location(/*2.390117, -75.890855, true, false, 0*/);//debug
    private List<Service> services = new ArrayList<>();
    private String photoPath = /*"/storage/emulated/0/Download/descarga (14).jpeg"*/"";//debug
    private RetrofitInterface retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        uiInit();

        /*schedules = new Gson().fromJson("[{\"days\":[{\"day\":1},{\"day\":4}],\"end_hour\":50,\"start_hour\":48},{\"days\":[{\"day\":1},{\"day\":4},{\"day\":6}],\"end_hour\":52,\"start_hour\":51}]", new TypeToken<List<Schedule>>(){}.getType());
        services = new Gson().fromJson("[{\"cost\":56,\"description\":\"5f5f5f4\",\"duration\":\"00:25\",\"name\":\"tt\"},{\"cost\":800,\"description\":\"vtv5c5c5c\",\"duration\":\"05:35\",\"name\":\"ctctc4f\"}]", new TypeToken<List<Service>>(){}.getType());*/
        //debug

        context = this;
        inflater = getLayoutInflater();

        retrofit = RetrofitTools.getInterface();

        indicators = new TextView[] {indicator1TV, indicator2TV, indicator3TV};
        containers = new ConstraintLayout[] {step1CL, step2CL, step3CL};
        buttons = new Button[][]{
            { phoneB, nameB, emailB, professionB },
            { hoursB, locationB, servicesB },
            { pictureB, descriptionB, cancellationB }
        };

        updateProfileStep();
        updateFillDataIndicators();

        setDataListeners();

        nextB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(/*validateNext()*/true){//debug
                    if(profileStep + 1 <= indicators.length){
                        profileStep++;
                        updateProfileStep();
                    }else{
                        Call<APIResponse> signupUserCall = retrofit.signupUser(new SignupUser(user, profile, schedules, location, services));

                        Log.e("parameters", new Gson().toJson(schedules));

                        signupUserCall.enqueue(new Callback<APIResponse>() {
                            @Override
                            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                APIResponse apiResponse = response.body();

                                if(apiResponse.isOk()){
                                    Log.e("picture", "uploading");
                                    File photoFile = new File(photoPath);
                                    RequestBody photoRB = RequestBody.create(MediaType.parse("image/*"), photoFile);
                                    MultipartBody.Part photoPart = MultipartBody.Part.createFormData("photo", photoFile.getName(), photoRB);

                                    Call<APIResponse> updateProfilePictureCall = retrofit.updateProfilePicture(photoPart, new Gson().fromJson(apiResponse.getMessage(), User.class).getId());

                                    updateProfilePictureCall.enqueue(new Callback<APIResponse>() {
                                        @Override
                                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                            APIResponse apiResponse = response.body();

                                            if(apiResponse.isOk()){
                                                Toast.makeText(CreateProfileActivity.this, "Perfil creado", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(context, HomePartnerActivity.class));
                                            }

                                            try {
                                                Log.e("picture sc", response.errorBody().string());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }catch (NullPointerException e){
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<APIResponse> call, Throwable t) {
                                            Log.e("picture", t.getMessage());
                                        }
                                    });
                                }

                                Log.e("singup ec", new Gson().toJson(apiResponse));

                                try {
                                    Log.e("signup sc", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponse> call, Throwable t) {
                                Log.e("signup", t.getMessage());
                            }
                        });
                    }
                }else{
                    Toast.makeText(context, "Completa la información para continuar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileStep - 1 >= 1){
                    profileStep--;
                    updateProfileStep();
                }else{
                    finish();
                }
            }
        });
    }

    private void uiInit(){
        indicator1TV = findViewById(R.id.indicator1TV);
        indicator2TV = findViewById(R.id.indicator2TV);
        indicator3TV = findViewById(R.id.indicator3TV);
        step1CL = findViewById(R.id.step1CL);
        step2CL = findViewById(R.id.step2CL);
        step3CL = findViewById(R.id.step3CL);
        phoneB = findViewById(R.id.phoneB);
        nameB = findViewById(R.id.nameB);
        emailB = findViewById(R.id.emailB);
        professionB = findViewById(R.id.professionB);
        hoursB = findViewById(R.id.hoursB);
        locationB = findViewById(R.id.locationB);
        servicesB = findViewById(R.id.servicesB);
        pictureB = findViewById(R.id.pictureB);
        descriptionB = findViewById(R.id.descriptionB);
        cancellationB = findViewById(R.id.cancellationB);
        nextB = findViewById(R.id.nextB);
        backIB = findViewById(R.id.backIB);
    }

    private void setDataListeners(){
        //personal data
        phoneB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createProfileToPhoneValidation = new Intent(context, PhoneValidationActivity.class);
                createProfileToPhoneValidation.putExtra("phone", new Gson().toJson(user.getPhone()));
                startActivityForResult(createProfileToPhoneValidation, PHONE_CODE);
            }
        });

        nameB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEditProfileData(R.string.name, user.getName());
            }
        });

        emailB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEditProfileData(R.string.email, user.getEmail());
            }
        });

        professionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createProfileToPickProfession = new Intent(context, PickProfessionActivity.class);
                createProfileToPickProfession.putExtra("selected", profile.getProfession().getId());
                startActivityForResult(createProfileToPickProfession, PROFESSION_CODE);
            }
        });

        //services data

        hoursB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createProfileToPickWorkSchedule =  new Intent(context, PickWorkScheduleActivity.class);

                if(!profile.isWork_24_7())
                    createProfileToPickWorkSchedule.putExtra("schedules", new Gson().toJson(schedules));

                createProfileToPickWorkSchedule.putExtra("work_24_7", profile.isWork_24_7());
                createProfileToPickWorkSchedule.putExtra("rest_break", profile.getRest_break());
                startActivityForResult(createProfileToPickWorkSchedule, WORK_SCHEDULE_CODE);
            }
        });

        locationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createProfileToPickLocation = new Intent(context, PickLocationActivity.class);
                createProfileToPickLocation.putExtra("location", new Gson().toJson(location));
                startActivityForResult(createProfileToPickLocation, LOCATION_CODE);
            }
        });

        servicesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createProfileToPickServices = new Intent(context, PickServicesActivity.class);
                createProfileToPickServices.putExtra("services", new Gson().toJson(services));
                startActivityForResult(createProfileToPickServices, SERVICES_CODE);
            }
        });

        //profile data

        pictureB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createProfileToPickPicture = new Intent(context, PickPictureActivity.class);
                createProfileToPickPicture.putExtra("photo", photoPath);
                startActivityForResult(createProfileToPickPicture, PICTURE_CODE);
            }
        });

        descriptionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createProfileToInputDescription = new Intent(context, InputDescriptionActivity.class);
                createProfileToInputDescription.putExtra("description", profile.getDescription());
                startActivityForResult(createProfileToInputDescription, DESCRIPTION_CODE);
            }
        });

        cancellationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createProfileToPickCancellation = new Intent(context, PickCancellationPolicyActivity.class);
                createProfileToPickCancellation.putExtra("cancellation", profile.getCancellation());
                startActivityForResult(createProfileToPickCancellation, CANCELLATION_CODE);
            }
        });
    }

    private void displayEditProfileData(final int titleId, String prevVal){
        String title = getString(titleId);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog;

        builder.setTitle(title);
        builder.setView(inflater.inflate(R.layout.dialog_edit_profile_data, null));

        dialog = builder.create();
        dialog.show();


        final EditText dataET = dialog.findViewById(R.id.dataET);
        Button saveB = dialog.findViewById(R.id.saveB);

        dataET.setHint(title);
        dataET.setText(prevVal);

        if(prevVal != null)
            dataET.setSelection(prevVal.length());

        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (titleId){
                    case R.string.name:
                        user.setName(dataET.getText().toString());
                        break;
                    case R.string.email:
                        user.setEmail(dataET.getText().toString());
                        break;
                }

                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                updateFillDataIndicators();
            }
        });
    }

    private void updateValidations(){
        validations = new boolean[][] {
                {
                    !user.getPhone().getFullNumber().equals(""),
                    !user.getName().equals(""),
                    !user.getEmail().equals(""),
                    profile.getProfession().getId() != -1
                    /*true, true, true, true*///debug
                },
                {
                    profile.getRest_break() != -1 && (schedules.size() > 0 || profile.isWork_24_7()),
                    location.getLat() != -1 && location.getLng() != -1 && (location.isTravel_to_work() || location.isWork_at_loc()),
                    services.size() > 0
                    /*true, true, true*///debug
                },
                {
                    !photoPath.equals(""),
                    !profile.getDescription().equals(""),
                    profile.getCancellation() != -1
                    /*true, true, true*///debug
                }
        };
    }

    private void updateFillDataIndicators(){
        updateValidations();

        for(int i = 0; i < buttons[profileStep-1].length; i++){
            if(validations[profileStep-1][i]){
                buttons[profileStep-1][i].setTextColor(ContextCompat.getColor(context, R.color.accentGradient));
            }else{
                buttons[profileStep-1][i].setTextColor(ContextCompat.getColor(context, R.color.text));
            }
        }
    }

    private boolean validateNext(){
        boolean valid = true;

        for(int i = 0; i < buttons[profileStep-1].length; i++){
            if(!validations[profileStep-1][i]){
                valid = false;
                break;
            }
        }

        return valid;
    }

    private void updateProfileStep(){
        for(int i = 0; i < 3; i++){
            if(i < profileStep){
                Log.e("i", i+"");
                indicators[i].setBackground(ContextCompat.getDrawable(context, R.drawable.v_circular_indicator_checked));
            }else{
                indicators[i].setBackground(ContextCompat.getDrawable(context, R.drawable.v_circular_indicator));
            }

            if(i == profileStep-1)
                containers[i].setVisibility(View.VISIBLE);
            else
                containers[i].setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PHONE_CODE:
                if(resultCode == RESULT_OK){
                    if (data != null) {
                        user.setPhone(new Gson().fromJson(data.getStringExtra("phone"), Phone.class));
                        updateFillDataIndicators();
                    }
                }
                break;
            case PROFESSION_CODE:
                if(resultCode == RESULT_OK){
                    if (data != null) {
                        profile.setProfession(new Gson().fromJson(data.getStringExtra("profession"), Profession.class));
                        updateFillDataIndicators();
                    }
                }
                break;
            case WORK_SCHEDULE_CODE:
                switch(resultCode){
                    case RESULT_OK:
                        if(data != null && data.getExtras() != null){
                            if(data.getExtras().containsKey("schedules"))
                                schedules = new Gson().fromJson(data.getStringExtra("schedules"), new TypeToken<List<Schedule>>(){}.getType());

                            profile.setWork_24_7(!data.getExtras().containsKey("schedules"));
                            profile.setRest_break(data.getIntExtra("rest_break", -1));

                            Log.e("work", String.valueOf(profile.getRest_break() != -1 && (schedules.size() > 0 || profile.isWork_24_7())));

                            updateFillDataIndicators();
                        }
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
            case LOCATION_CODE:
                switch (resultCode){
                    case RESULT_OK:
                        if (data != null) {
                            location = new Gson().fromJson(data.getStringExtra("location"), Location.class);
                            updateFillDataIndicators();
                        }
                        break;
                }
                break;
            case SERVICES_CODE:
                switch (resultCode){
                    case RESULT_OK:
                        if (data != null) {
                            services = new Gson().fromJson(data.getStringExtra("services"), new TypeToken<List<Service>>(){}.getType());
                            updateFillDataIndicators();
                        }
                        break;
                }
                break;
            case PICTURE_CODE:
                switch (resultCode){
                    case RESULT_OK:
                        if(data != null){
                            photoPath = data.getStringExtra("photo");
                            updateFillDataIndicators();
                        }
                        break;
                }
                break;
            case DESCRIPTION_CODE:
                switch (resultCode){
                    case RESULT_OK:
                        if(data != null){
                            profile.setDescription(data.getStringExtra("description"));
                            updateFillDataIndicators();
                        }
                        break;
                }
                break;
            case CANCELLATION_CODE:
                switch (resultCode){
                    case RESULT_OK:
                        if(data != null){
                            profile.setCancellation(data.getIntExtra("cancellation", -1));
                            updateFillDataIndicators();
                        }
                        break;
                }
                break;
        }
    }
}
