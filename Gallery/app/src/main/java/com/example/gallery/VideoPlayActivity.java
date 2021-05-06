package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
                    case R.id.delete:
                        break;
                    case R.id.details:

                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }
}