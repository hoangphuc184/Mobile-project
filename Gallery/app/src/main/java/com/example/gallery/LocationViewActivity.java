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

        btnAlbum = (ImageButton)findViewById(R.id.photos_view);
        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationViewActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnVideo = (ImageButton)findViewById(R.id.videos_view);
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationViewActivity.this, VideoViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnLoc = (ImageButton)findViewById(R.id.location_view);
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationViewActivity.this, LocationViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnFav = (ImageButton)findViewById(R.id.favorite_view);
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationViewActivity.this, FavoriteViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnSec = (ImageButton)findViewById(R.id.secure_view);
        btnSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationViewActivity.this, SecurityViewActivity.class);
                startActivity(intent);
            }
        });

    }
}