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

import com.aciv.parceros.Adapters.ShowServiceAdapter;
import com.aciv.parceros.Interfaces.RetrofitInterface;
import com.aciv.parceros.Models.Database.Service;
import com.aciv.parceros.Models.Database.User;
import com.aciv.parceros.R;
import com.aciv.parceros.Tools.RetrofitTools;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowServicesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_partner_show_services, container, false);

        final ListView servicesLV = root.findViewById(R.id.servicesLV);

        final Context context = getActivity();
        RetrofitInterface retrofit = RetrofitTools.getInterface();

        final List<Service> services = new ArrayList<>();
        final ShowServiceAdapter serviceAdapter = new ShowServiceAdapter(context, services);

        servicesLV.setAdapter(serviceAdapter);

        Call<ArrayList<Service>> showServicesCall = retrofit.showServicesWithReactionsByUser(8, 9);

        showServicesCall.enqueue(new Callback<ArrayList<Service>>() {
            @Override
            public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
                if(response.body() != null){
                    services.clear();
                    services.addAll(response.body());

                    serviceAdapter.notifyDataSetChanged();

                    Log.e("services", new Gson().toJson(services));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
                Log.e("services error", t.toString());
                Toast.makeText(context, "error al cargar servicios", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }


}
