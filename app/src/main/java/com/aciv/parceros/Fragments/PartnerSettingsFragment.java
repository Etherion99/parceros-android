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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.aciv.parceros.Activities.HomeClientActivity;
import com.aciv.parceros.R;

public class PartnerSettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_partner_settings, container, false);

        Button changeModeB = root.findViewById(R.id.changeModeB);
        ConstraintLayout bussinesPageCL = root.findViewById(R.id.bussinessPageCL);

        final Context context = getActivity();

        changeModeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, HomeClientActivity.class));
                getActivity().finish();
            }
        });

        bussinesPageCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return root;
    }
}
