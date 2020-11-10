package com.aciv.parceros.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aciv.parceros.R;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;

public class SplashScreenActivity extends ActivityManagePermission {
    private ProgressBar loadPB;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        context = this;

        loadPB = findViewById(R.id.loadPB);

        loadAnimate();

        askCompactPermissions(new String[]{PermissionUtils.Manifest_ACCESS_COARSE_LOCATION, PermissionUtils.Manifest_ACCESS_FINE_LOCATION, PermissionUtils.Manifest_CAMERA, PermissionUtils.Manifest_READ_EXTERNAL_STORAGE}, new PermissionResult() {
            @Override
            public void permissionGranted() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(context, HomeClientActivity.class));
                        finish();
                    }
                }, 100);
            }

            @Override
            public void permissionDenied() {
                Toast.makeText(context, "Debes aceptar todos los permisos para continuar", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void permissionForeverDenied() {
                Toast.makeText(context, "Debes aceptar todos los permisos para continuar", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void loadAnimate(){
        ObjectAnimator loadAnimation = ObjectAnimator.ofInt(loadPB, "progress", 0, 100);
        loadAnimation.setDuration(1500);
        loadAnimation.setInterpolator(new LinearInterpolator());

        loadAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loadAnimate();
            }
        });

        loadAnimation.start();
    }
}
