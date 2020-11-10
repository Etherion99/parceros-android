package com.aciv.parceros.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aciv.parceros.Activities.ViewProfileActivity;
import com.aciv.parceros.Adapters.SearchUserAdapter;
import com.aciv.parceros.Interfaces.RetrofitInterface;
import com.aciv.parceros.Models.Database.Phone;
import com.aciv.parceros.Models.Database.Profession;
import com.aciv.parceros.Models.Database.Profile;
import com.aciv.parceros.Models.Database.User;
import com.aciv.parceros.R;
import com.aciv.parceros.Tools.RetrofitTools;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientHomeFragment extends Fragment {
    private RetrofitInterface retrofit;
    private List<User> searchedUsers = new ArrayList<>();
    private SearchUserAdapter searchUsersAdapter;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_client_home, container, false);

        EditText searchET = root.findViewById(R.id.searchET);
        ListView resultsLV = root.findViewById(R.id.resultsLV);

        context = getActivity();

        retrofit = RetrofitTools.getInterface();

        //searchedUsers.add(new User(8, "juan david guzman 123", "email", new Phone(), new Profile(new Profession(1, "Ingeniero electrónico"), -1, -1, false, "")));
        //debug
        searchUsersAdapter = new SearchUserAdapter(context, searchedUsers);

        resultsLV.setAdapter(searchUsersAdapter);

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 1){
                    Call<ArrayList<User>> searchUsersCall = retrofit.searchUsers(s.toString());

                    searchUsersCall.clone().enqueue(new Callback<ArrayList<User>>() {
                        @Override
                        public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                            List<User> results = response.body();

                            if(results != null){
                                searchedUsers.clear();

                                if(results.size() > 0){
                                    searchedUsers.addAll(response.body());
                                    searchUsersAdapter.notifyDataSetChanged();

                                    Log.e("users", new Gson().toJson(searchedUsers));
                                }else{
                                    Toast.makeText(context, "Sin resultados", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                Toast.makeText(context, "Erro al consultar", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                            Log.e("users", t.getMessage());
                        }
                    });
                }
            }
        });

        //searchET.setText("na");//debug

        resultsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent viewProfileIntent = new Intent(context, ViewProfileActivity.class);
                viewProfileIntent.putExtra("id", searchedUsers.get(position).getId());
                Log.e("id sent", searchedUsers.get(position).getId()+"");
                startActivity(viewProfileIntent);
            }
        });

        //Intent viewProfileIntent = new Intent(context, ViewProfileActivity.class);
        //viewProfileIntent.putExtra("id", 2);
        //startActivity(viewProfileIntent); //debug

        return root;
    }
}
