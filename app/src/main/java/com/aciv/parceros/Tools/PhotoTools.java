package com.aciv.parceros.Tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

public class PhotoTools {

    public static Bitmap getBitmapFromPath(String path){
        Bitmap photo = BitmapFactory.decodeFile(path);

        ExifInterface ei = null;

        try {
            ei = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return processBitmap(photo, ei);
    }

    public static Bitmap processBitmap(Bitmap bitmap, ExifInterface ei){
        Bitmap rotatedBitmap = null;

        if(ei != null){
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateBitmap(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateBitmap(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateBitmap(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
        }

        return rotatedBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
