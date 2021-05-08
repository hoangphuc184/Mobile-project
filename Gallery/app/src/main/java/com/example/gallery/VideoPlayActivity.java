package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class VideoPlayActivity extends AppCompatActivity {

    String str_video;
    VideoView vv_video;

    ImageButton btnMoreVid;
    ImageButton btnBackVid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_screen);

        vv_video = (VideoView) findViewById(R.id.video_full_screen);

        str_video = getIntent().getStringExtra("Video");

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(vv_video);
        vv_video.setMediaController(mediaController);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) vv_video.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        vv_video.setLayoutParams(params);
        vv_video.setVideoPath(str_video);
        vv_video.start();

        btnBackVid = findViewById(R.id.btnBackVid);
        btnBackVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnMoreVid = findViewById(R.id.btnMoreVid);
        btnMoreVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });
    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnMoreVid);
        popupMenu.getMenuInflater().inflate(R.menu.setting_video_view, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_vid:
                        deleteVideo(str_video);
                        Intent intent = new Intent(VideoPlayActivity.this, VideoViewActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.details_vid:
                        vv_video.pause();
                        Intent intent2 = new Intent(VideoPlayActivity.this, VideoDetailsScreen.class);
                        finish();
                        intent2.putExtra("path", str_video);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }
    private void deleteVideo(String path) {
        File file = new File(path);

        String[] projection = {MediaStore.Video.Media._ID};

        // Match on the file path
        String selection = MediaStore.Video.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{file.getAbsolutePath()};

        // Query for the ID of the media matching the file path
        Uri queryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst()) {
            // We found the ID. Deleting the item via the content provider will also remove the file
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
        }
        c.close();
    }
}