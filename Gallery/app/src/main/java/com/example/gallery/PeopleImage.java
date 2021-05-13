package com.example.gallery;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class PeopleImage {
    public static ArrayList<String> listOfAllImages;

    public static boolean checkFace(Context context, String path){
        File imgFile = new  File(path);
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        FaceDetector faceDetector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .setClassificationType(FaceDetector.NO_CLASSIFICATIONS)
                .build();
        if (!faceDetector.isOperational()){
            return false;
        }
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> face =  faceDetector.detect(frame);
        faceDetector.release();
        if (face.size() == 0)
            return false;
        else
            return true;
    }
    public static void handleFaceDetection(Context context) throws IOException {
        listOfAllImages = new ArrayList<>();
        for (int i =0;  i < ImagesGallery.listOfImages(context).size();i++ ){
            if(checkFace(context, ImagesGallery.listOfImages(context).get(i)))
                listOfAllImages.add(ImagesGallery.listOfImages(context).get(i));
     }
    }
    public static ArrayList<String> listOfPeopleImages(){
        return listOfAllImages;
    }
}
