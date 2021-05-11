package com.example.gallery;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class PeopleImage {
    public static boolean checkFace(Context context, String path){
        File imgFile = new  File(path);
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        FaceDetector faceDetector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        if (!faceDetector.isOperational()){
            return false;
        }
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> face =  faceDetector.detect(frame);
        if (face.size() == 0)
            return false;
        else
            return true;
    }
    public static ArrayList<String> listOfPeopleImages(Context context) throws IOException {

        ArrayList<String> listOfAllImages = new ArrayList<>();

        for (int i =0;  i < ImagesGallery.listOfImages(context).size();i++ ){
            if(checkFace(context, ImagesGallery.listOfImages(context).get(i)))
                listOfAllImages.add(ImagesGallery.listOfImages(context).get(i));
     }
        return listOfAllImages;
    }
}
