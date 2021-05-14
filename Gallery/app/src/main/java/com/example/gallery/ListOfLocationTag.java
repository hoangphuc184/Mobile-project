package com.example.gallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class ListOfLocationTag {
    public static ArrayList<String> listOfLocation(Context context) throws IOException {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        ArrayList<String> listOfLoc = new ArrayList<>();

       for (int i = 0; i<ImagesGallery.listOfImages(context).size(); i++){
           String savedData = sharedPreferences.getString(ImagesGallery.listOfImages(context).get(i), "");
           if (!savedData.equals(""))
                listOfLoc.add(savedData);
       }
       if (!listOfLoc.contains("NO LOCATION TAG"))
            listOfLoc.add("NO LOCATION TAG");

        Set<String> temp = new LinkedHashSet<>();
        for (int i =0; i< listOfLoc.size();i++) {temp.add(listOfLoc.get(i));}
        listOfLoc.clear();
        listOfLoc.addAll(temp);

        return listOfLoc;
    }
}
