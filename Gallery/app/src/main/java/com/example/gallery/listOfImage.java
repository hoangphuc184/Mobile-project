package com.example.gallery;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class listOfImage {
    public static ArrayList<String> listOfImages(Context context, String date) throws IOException {
        Uri uri;
        Cursor cursor;
        int index_column_data, index_column_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<>();
        String absoPathOfImage;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null,
                null, orderBy+" DESC");

        index_column_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while(cursor.moveToNext()){
            absoPathOfImage = cursor.getString(index_column_data);

            listOfAllImages.add(absoPathOfImage);
        }
        ArrayList<String> temp = new ArrayList<>();

        for (int i=0; i<listOfAllImages.size();i++){
            File file = new File(listOfAllImages.get(i));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (sdf.format(file.lastModified()).equals(date)){
                temp.add(listOfAllImages.get(i));
            }
        }
        listOfAllImages.clear();
        listOfAllImages.addAll(temp);
        return listOfAllImages;
    }
}
