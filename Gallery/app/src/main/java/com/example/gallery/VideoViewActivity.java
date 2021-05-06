package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class VideoViewActivity extends AppCompatActivity {
    ImageButton btnAlbum;
    ImageButton btnVideo;
    ImageButton btnLoc;
    ImageButton btnFav;
    ImageButton btnSec;
    private Toolbar mToolbar;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<VideoModel> listOfVideo;
    VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_section);

        mToolbar = findViewById(R.id.toolbarVideos);
        setSupportActionBar(mToolbar);

//        mToolbar.setNavigationIcon(R.drawable.ic_back);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(VideoViewActivity.this, MainActivity.class);
//                finish();
//                startActivity(intent);
//            }
//        });

        recyclerView= findViewById(R.id.recyclerview_gallery_videos);
        layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        try {
            fetchVideoFromGallery();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnAlbum = (ImageButton)findViewById(R.id.photos_view);
        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoViewActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnVideo = (ImageButton)findViewById(R.id.videos_view);
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoViewActivity.this, VideoViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnLoc = (ImageButton)findViewById(R.id.location_view);
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoViewActivity.this, LocationViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnFav = (ImageButton)findViewById(R.id.favorite_view);
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoViewActivity.this, FavoriteViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnSec = (ImageButton)findViewById(R.id.secure_view);
        btnSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoViewActivity.this, SecurityViewActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                Toast.makeText(getApplicationContext(), "About clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(getApplicationContext(), "Settings clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.create_album:
                Toast.makeText(getApplicationContext(), "Create album clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchVideoFromGallery() throws IOException {
        listOfVideo = VideosGallery.listOfVideos(this);
        for (int  i= 0; i<listOfVideo.size();i++){
            System.out.println(listOfVideo.get(i));
        }
        videoAdapter = new VideoAdapter(this, listOfVideo, VideoViewActivity.this);
        recyclerView.setAdapter(videoAdapter);

    }
}