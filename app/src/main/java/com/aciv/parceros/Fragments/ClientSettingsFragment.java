package com.aciv.parceros.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aciv.parceros.Activities.HomeClientActivity;
import com.aciv.parceros.Activities.HomePartnerActivity;
import com.aciv.parceros.R;

public class ClientSettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_client_settings, container, false);

        Button changeModeB = root.findViewById(R.id.changeModeB);

        final Context context = getActivity();

        changeModeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, HomePartnerActivity.class));
                getActivity().finish();
            }
        });


        return root;
    }
}
