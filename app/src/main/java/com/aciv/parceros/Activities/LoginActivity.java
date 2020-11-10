package com.aciv.parceros.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.aciv.parceros.Interfaces.RetrofitInterface;
import com.aciv.parceros.Models.Database.APIResponse;
import com.aciv.parceros.Models.Database.Phone;
import com.aciv.parceros.R;
import com.aciv.parceros.Tools.RetrofitTools;
import com.aciv.parceros.Tools.SessionTools;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private RetrofitInterface retrofit;
    private Activity activity;
    private Context context;
    private final int PHONE_VALIDATION = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;
        context = this;

        retrofit = RetrofitTools.getInterface();

        Intent loginToPhoneValidation = new Intent(context, PhoneValidationActivity.class);
        startActivityForResult(loginToPhoneValidation, PHONE_VALIDATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Phone phone = new Gson().fromJson(data.getStringExtra("phone"), Phone.class);

            Log.e("auth params", new Gson().toJson(phone));

            Call<APIResponse> userAuthCall = retrofit.authUser(phone);

            userAuthCall.enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    Log.e("auth response", new Gson().toJson(response.body()));

                    APIResponse apiResponse = response.body();

                    long id = Long.parseLong(apiResponse.getMessage());

                    SharedPreferences.Editor prefsEditor = SessionTools.getEditor(activity);

                    prefsEditor.putLong("user_id", id);
                    prefsEditor.commit();

                    startActivity(new Intent(context, HomeClientActivity.class));
                    finish();
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {
                    Log.e("auth error", t.toString());
                }
            });
        }else if(requestCode == RESULT_CANCELED){

        }
    }
}
