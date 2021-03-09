package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class LocationViewActivity extends AppCompatActivity {
    ImageButton btnAlbum;
    ImageButton btnVideo;
    ImageButton btnLoc;
    ImageButton btnFav;
    ImageButton btnSec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_section);

        btnAlbum = (ImageButton)findViewById(R.id.album_views);
        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationViewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnVideo = (ImageButton)findViewById(R.id.video_views);
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationViewActivity.this, VideoViewActivity.class);
                startActivity(intent);
            }
        });

        btnLoc = (ImageButton)findViewById(R.id.loc_views);
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationViewActivity.this, LocationViewActivity.class);
                startActivity(intent);
            }
        });

        btnFav = (ImageButton)findViewById(R.id.fav_views);
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationViewActivity.this, FavoriteViewActivity.class);
                startActivity(intent);
            }
        });

        btnSec = (ImageButton)findViewById(R.id.sec_views);
        btnSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationViewActivity.this, SecurityViewActivity.class);
                startActivity(intent);
            }
        });

    }
}