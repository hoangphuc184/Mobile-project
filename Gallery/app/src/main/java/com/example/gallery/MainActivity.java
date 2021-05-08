package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageButton btnPhoto;
    ImageButton btnVideo;
    ImageButton btnLoc;
    ImageButton btnFav;
    ImageButton btnSec;
    Toolbar mToolbar;

    ImageButton btnCamera;

    SharedPreferences sharedPreferences = null;

    RecyclerView recyclerView;
    GalleryAdapter galleryAdapter;
    List<String> images;


    private static final int MY_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview_gallery_images);
        mToolbar = findViewById(R.id.toolbarAlbums);
        setSupportActionBar(mToolbar);

        sharedPreferences = getSharedPreferences("theme", 0);
        Boolean booleanValue = sharedPreferences.getBoolean("dark_mode", true);
        if (booleanValue){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        btnCamera = findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                finish();
                startActivity(intent);
            }
        });

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    }, MY_PERMISSION_CODE);
        } else {
            try {
                loadImages();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        if(ContextCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_WRITE_PERMISSION_CODE);
//        }
//
//        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
//                    Manifest.permission.CAMERA
//            }, 100);
//        }

        btnPhoto = (ImageButton)findViewById(R.id.photos_view);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnVideo = (ImageButton)findViewById(R.id.videos_view);
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnLoc = (ImageButton)findViewById(R.id.location_view);
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocationViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnFav = (ImageButton)findViewById(R.id.favorite_view);
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnSec = (ImageButton)findViewById(R.id.secure_view);
        btnSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecurityViewActivity.class);
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
                Intent intent_setting = new Intent(MainActivity.this, Settings.class);
                startActivity(intent_setting);
                break;
            case R.id.change_to_by_date:
                Intent intent = new Intent(MainActivity.this, ByDateActivity.class);
                finish();
                startActivity(intent);
                break;
            case R.id.create_album:
                Toast.makeText(getApplicationContext(), "Create album clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadImages() throws IOException {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        images = ImagesGallery.listOfImages(this);
        galleryAdapter = new GalleryAdapter(this, images, new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                Intent intent = new Intent(getApplicationContext(), FullScreenActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(galleryAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case MY_PERMISSION_CODE:{
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
                try {
                    loadImages();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;}

        }

}
}
