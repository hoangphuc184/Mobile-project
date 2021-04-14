package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.VideoView;

public class VideoPlayActivity extends AppCompatActivity {

    String str_video;
    VideoView vv_video;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_screen);

        vv_video = (VideoView) findViewById(R.id.video_full_screen);

        str_video = getIntent().getStringExtra("Video");
        vv_video.setVideoPath(str_video);
        vv_video.start();
    }
}