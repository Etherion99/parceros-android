package com.aciv.parceros.Activities.Results;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.aciv.parceros.Adapters.SimpleRVAdapter;
import com.aciv.parceros.Interfaces.RetrofitInterface;
import com.aciv.parceros.Models.Database.Profession;
import com.aciv.parceros.R;
import com.aciv.parceros.Tools.RetrofitTools;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class PickProfessionActivity extends AppCompatActivity implements SimpleRVAdapter.ItemClickListener {
    private RecyclerView professionsRV;

    private Context context;
    private SimpleRVAdapter.ItemClickListener itemClickListener;
    private RetrofitInterface retrofit;
    private RecyclerView.LayoutManager professionsLM;
    private RecyclerView.Adapter professionAdapter;

    private ArrayList<Profession> professions;
    private int selected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_profession);

        professionsRV = findViewById(R.id.professionsRV);

        context = this;
        itemClickListener = this;

        professionsLM = new LinearLayoutManager(context);
        professionsRV.setLayoutManager(professionsLM);

        retrofit = RetrofitTools.getInterface();

        Call<ArrayList<Profession>> professionsCall = retrofit.getProfessions();

        professionsCall.enqueue(new Callback<ArrayList<Profession>>() {
            @Override
            public void onResponse(Call<ArrayList<Profession>> call, Response<ArrayList<Profession>> response) {
                if(response.isSuccessful()){
                    professions = response.body();

                    Log.e("professions", new Gson().toJson(professions));

                    professionAdapter = new SimpleRVAdapter(context, professions, selected, itemClickListener);
                    professionsRV.setAdapter(professionAdapter);
                }else{
                    try {
                        Log.e("error professions", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Profession>> call, Throwable t) {
                Log.e("error professions", t.getMessage());
            }
        });

        selected = Objects.requireNonNull(getIntent().getExtras()).getInt("selected");
    }

    @Override
    public void onRVItemClick(int position) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("profession", new Gson().toJson(professions.get(position)));
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
