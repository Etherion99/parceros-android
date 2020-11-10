package com.aciv.parceros.Fragments.Partnert;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aciv.parceros.Adapters.ShowScheduleAdapter;
import com.aciv.parceros.Interfaces.RetrofitInterface;
import com.aciv.parceros.Models.Database.Schedule;
import com.aciv.parceros.Models.Local.ShowScheduleItem;
import com.aciv.parceros.R;
import com.aciv.parceros.Tools.ListTools;
import com.aciv.parceros.Tools.RetrofitTools;
import com.aciv.parceros.Tools.SchedulesTools;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowScheduleFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_partner_show_schedule, container, false);

        final ListView schedulesLV = root.findViewById(R.id.schedulesLV);

        final Context context = getActivity();
        RetrofitInterface retrofit = RetrofitTools.getInterface();

        Call<ArrayList<Schedule>> showSchedulesCall = retrofit.showSchedulesByUser(8);

        showSchedulesCall.enqueue(new Callback<ArrayList<Schedule>>() {
            @Override
            public void onResponse(Call<ArrayList<Schedule>> call, Response<ArrayList<Schedule>> response) {
                if(response.body() != null){
                    List<Schedule> schedules = response.body();

                    Log.e("pre schedules", new Gson().toJson(response.body()));

                    List<ShowScheduleItem> schedulesItems = SchedulesTools.scheduleDBToItem(schedules);

                    Log.e("schedules", new Gson().toJson(schedulesItems));

                    schedulesLV.setAdapter(new ShowScheduleAdapter(context, schedulesItems));
                }else{
                    Toast.makeText(context, "error schedules", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Schedule>> call, Throwable t) {
                Log.e("schedules error", t.getMessage());
            }
        });

        return root;
    }
}
