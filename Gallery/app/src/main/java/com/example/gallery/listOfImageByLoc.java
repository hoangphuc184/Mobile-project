package com.example.gallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class listOfImageByLoc {
    public static ArrayList<String> listOfImageByLocation(Context context, String loc) throws IOException {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        ArrayList<String> listOfImgByLoc = new ArrayList<>();

        ArrayList<String> temp = new ArrayList<>();
        if (loc.equals("NO LOCATION TAG")){
            for (int i = 0; i < ImagesGallery.listOfImages(context).size(); i++) {
                String savedData = sharedPreferences.getString(ImagesGallery.listOfImages(context).get(i), "");
                if (savedData.equals(""))
                    temp.add(ImagesGallery.listOfImages(context).get(i));
            }
            listOfImgByLoc.clear();
            listOfImgByLoc.addAll(temp);
        }
        else {
            for (int i = 0; i < ImagesGallery.listOfImages(context).size(); i++) {
                String savedData = sharedPreferences.getString(ImagesGallery.listOfImages(context).get(i), "");
                if (savedData.equals(loc))
                    temp.add(ImagesGallery.listOfImages(context).get(i));
            }
            listOfImgByLoc.clear();
            listOfImgByLoc.addAll(temp);
        }
        return listOfImgByLoc;
    }
}
