package com.example.gallery;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class listOfVideos {
    public static ArrayList<VideoModel> listOfVideos(Context context, String date) throws IOException {
        Uri uri;
        Cursor cursor;
        int columnIndex, thumbnail;
        ArrayList<VideoModel> listOfVideo = new ArrayList<>();
        String absolutePath;

        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Thumbnails.DATA,
        };

        String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        thumbnail = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

        while (cursor.moveToNext()){
            absolutePath = cursor.getString(columnIndex);

            VideoModel videoModel = new VideoModel();
            videoModel.setSelected(false);
            videoModel.setVideoPath(absolutePath);
            videoModel.setVideoThumb(cursor.getString(thumbnail));

            listOfVideo.add(videoModel);
        }

        ArrayList<VideoModel> temp = new ArrayList<>();

        for (int i=0; i<listOfVideo.size();i++){
            File file = new File(listOfVideo.get(i).getVideoPath());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (sdf.format(file.lastModified()).equals(date)){
                temp.add(listOfVideo.get(i));
            }
        }
        listOfVideo.clear();
        listOfVideo.addAll(temp);
        
        return listOfVideo;
    }
}
