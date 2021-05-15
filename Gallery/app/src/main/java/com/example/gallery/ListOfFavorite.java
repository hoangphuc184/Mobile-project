package com.example.gallery;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class ListOfFavorite {
    public static ArrayList<String> listOfFavorite(Context context) throws IOException{
        SharedPreferences sharedPreferences = context.getSharedPreferences("State", 0);

        ArrayList<String> listOfFav = new ArrayList<>();

        for (int i = 0; i < ImagesGallery.listOfImages(context).size(); i++){
            String path = ImagesGallery.listOfImages(context).get(i);
            Boolean stateOfImg = sharedPreferences.getBoolean(path, false);
            if(stateOfImg){
                listOfFav.add(path);
            }
        }
        Set<String> temp = new LinkedHashSet<>();
        for (int i = 0; i< listOfFav.size(); i++){
            temp.add(listOfFav.get(i));
        }
        listOfFav.clear();
        listOfFav.addAll(temp);

        return listOfFav;
    }

}
