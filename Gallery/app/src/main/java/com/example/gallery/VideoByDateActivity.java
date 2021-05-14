package com.example.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class VideoByDateActivity extends AppCompatActivity {

    ImageButton btnPhoto;
    ImageButton btnVideo;
    ImageButton btnLoc;
    ImageButton btnFav;
    ImageButton btnSec;
    Toolbar mToolbar;

    ImageButton btnCamera;

    RecyclerView recyclerViewGroup;
    VideoGroupAdapter groupAdapter;
    ArrayList<String> arrayListGroup;

    LinearLayoutManager linearLayoutManager;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_by_date);


        mToolbar = findViewById(R.id.toolbarAlbums);
        setSupportActionBar(mToolbar);
        btnCamera = findViewById(R.id.btn_camera);


        recyclerViewGroup = findViewById(R.id.rv_group);
        arrayListGroup = new ArrayList<>();
        try {
            arrayListGroup = listOfVideosDate.listOfDateTaken(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        groupAdapter = new VideoGroupAdapter(this, arrayListGroup);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewGroup.setLayoutManager(linearLayoutManager);

        recyclerViewGroup.setAdapter(groupAdapter);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                finish();
                startActivity(intent);
            }
        });


        btnPhoto = (ImageButton)findViewById(R.id.photos_view);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoByDateActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnVideo = (ImageButton)findViewById(R.id.videos_view);
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoByDateActivity.this, VideoViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnLoc = (ImageButton)findViewById(R.id.location_view);
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoByDateActivity.this, LocationViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnFav = (ImageButton)findViewById(R.id.favorite_view);
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoByDateActivity.this, FavoriteViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnSec = (ImageButton)findViewById(R.id.secure_view);
        btnSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoByDateActivity.this, SecurityViewActivity.class);
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
            case R.id.setting:
                Intent intent_setting = new Intent(VideoByDateActivity.this, Settings.class);
                intent_setting.putExtra("CallingActivity", VideoViewActivity.class.toString());
                finish();
                startActivity(intent_setting);
                break;

            case R.id.change_to_by_date:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
