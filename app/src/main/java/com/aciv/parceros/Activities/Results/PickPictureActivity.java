package com.aciv.parceros.Activities.Results;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.aciv.parceros.R;
import com.aciv.parceros.Tools.FileManager;
import com.aciv.parceros.Tools.PhotoTools;

import java.io.File;
import java.io.IOException;

public class PickPictureActivity extends AppCompatActivity {
    private ImageButton cameraIB, galleryIB, backIB;
    private ImageView previewIV;
    private Button doneB;

    private Context context;

    private final int TAKE_PICTURE = 18;
    private final int SELECT_PHOTO = 28;
    private String photoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_picture);

        cameraIB = findViewById(R.id.cameraIB);
        galleryIB = findViewById(R.id.galleryIB);
        previewIV = findViewById(R.id.previewIV);
        backIB = findViewById(R.id.backIB);
        doneB = findViewById(R.id.doneB);

        context = this;

        if(getIntent().getExtras() != null){
            photoPath = getIntent().getStringExtra("photo");
            previewIV.setImageBitmap(PhotoTools.getBitmapFromPath(photoPath));
        }

        cameraIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File photoFile = null;

                try{
                    photoFile = FileManager.createPhotoFile(context);
                    photoPath = photoFile.getAbsolutePath();
                }catch (IOException exception){
                    Toast.makeText(PickPictureActivity.this, "Error al procesar archivo", Toast.LENGTH_SHORT).show();
                }

                if(photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(context, "com.aciv.parceros.fileprovider", photoFile);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    if(cameraIntent.resolveActivity(getPackageManager()) != null){
                        startActivityForResult(cameraIntent, TAKE_PICTURE);
                    }
                }
            }
        });

        galleryIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

                startActivityForResult(galleryIntent, SELECT_PHOTO);
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
                if(!photoPath.equals("")){
                    Intent response = new Intent();
                    response.putExtra("photo", photoPath);
                    setResult(RESULT_OK, response);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case TAKE_PICTURE:
                if(resultCode == RESULT_OK){
                    previewIV.setImageBitmap(PhotoTools.getBitmapFromPath(photoPath));
                }
                break;
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri imageUri = data.getData();

                    photoPath = FileManager.getPathFromUri(context, imageUri);

                    previewIV.setImageBitmap(PhotoTools.getBitmapFromPath(photoPath));
                }
                break;
        }
    }
}
