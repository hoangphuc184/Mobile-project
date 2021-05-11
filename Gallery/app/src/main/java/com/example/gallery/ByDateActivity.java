package com.example.gallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ByDateActivity extends AppCompatActivity {

    ImageButton btnPhoto;
    ImageButton btnVideo;
    ImageButton btnLoc;
    ImageButton btnFav;
    ImageButton btnSec;
    Toolbar mToolbar;

    ImageButton btnCamera;

    RecyclerView recyclerViewGroup;
    GroupAdapter groupAdapter;
    ArrayList<String> arrayListGroup;

    LinearLayoutManager linearLayoutManager;


    private static final int MY_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.by_date_activity);


        mToolbar = findViewById(R.id.toolbarAlbums);
        setSupportActionBar(mToolbar);
        btnCamera = findViewById(R.id.btn_camera);


        recyclerViewGroup = findViewById(R.id.rv_group);
        arrayListGroup = new ArrayList<>();
        try {
            arrayListGroup = listOfDate.listOfDateTaken(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        groupAdapter = new GroupAdapter(arrayListGroup, this);

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
                Intent intent = new Intent(ByDateActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnVideo = (ImageButton)findViewById(R.id.videos_view);
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ByDateActivity.this, VideoViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnLoc = (ImageButton)findViewById(R.id.location_view);
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ByDateActivity.this, LocationViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnFav = (ImageButton)findViewById(R.id.favorite_view);
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ByDateActivity.this, FavoriteViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnSec = (ImageButton)findViewById(R.id.secure_view);
        btnSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ByDateActivity.this, SecurityViewActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_for_image_only, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                Intent intent_setting = new Intent(ByDateActivity.this, Settings.class);
                intent_setting.putExtra("CallingActivity", ByDateActivity.class.toString());
                finish();
                startActivity(intent_setting);
                break;
            case R.id.change_to_by_date:
                break;
            case R.id.change_to_by_people:
                Intent intent_people = new Intent(ByDateActivity.this, ByPeopleActivity.class);
                finish();
                startActivity(intent_people);
                break;
            case R.id.create_album:
                Toast.makeText(getApplicationContext(), "Create album clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
