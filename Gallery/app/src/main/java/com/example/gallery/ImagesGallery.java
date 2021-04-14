package com.example.gallery;

import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class ImagesGallery {

    public static ArrayList<String> listOfImages(Context context){
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
        for (int i =0; i<listOfAllImages.size(); i++){
            String pathToFile = listOfAllImages.get(i);
            File file = new File(pathToFile);
            if(file.exists()) {
                long date = file.lastModified();
                Date d = new Date(date);
                System.out.println(d +"i"+i);
            }
        }


        return listOfAllImages;
    }
}
