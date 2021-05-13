package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class MainActivity extends AppCompatActivity {

    ImageButton btnPhoto;
    ImageButton btnLoc;
    ImageButton btnFav;
    ImageButton btnSec;
    Toolbar mToolbar;
    ImageButton btnCamera;
    SharedPreferences sharedPreferences = null;

    RecyclerView recyclerView;
    GalleryAdapter galleryAdapter;
    List<String> images;
    String currentPhotoPath;
    private ImageView imageView;
    private static final int MY_PERMISSION_CODE = 101;
    private static final int CAMERA_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Language setting
        sharedPreferences = getSharedPreferences("language", MODE_PRIVATE);
        String language = sharedPreferences.getString("My_lang", "");
        setLocale(language);

        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview_gallery_images);
        mToolbar = findViewById(R.id.toolbarAlbums);
        setSupportActionBar(mToolbar);

        //Theme setting
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
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                startActivityForResult(intent,1);
                finish();
                startActivity(intent);
            }
        });
<<<<<<< HEAD
        
=======

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    }, MY_PERMISSION_CODE);
        } else {
            try {
                loadImages(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

>>>>>>> 7b29e62fee1b2bee6e6e42682766b1ef88d47399
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

        btnVideo = (ImageButton)findViewById(R.id.videos_view);
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoViewActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnPhoto = (ImageButton)findViewById(R.id.photos_view);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
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
        getMenuInflater().inflate(R.menu.setting_for_image_only, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                Intent intent_setting = new Intent(MainActivity.this, Settings.class);
                intent_setting.putExtra("CallingActivity", MainActivity.class.toString());
                finish();
                startActivity(intent_setting);
                break;
            case R.id.change_to_by_date:
                Intent intent = new Intent(MainActivity.this, ByDateActivity.class);
                finish();
                startActivity(intent);
                break;
            case R.id.change_to_by_people:
                Intent intent_people = new Intent(MainActivity.this, ByPeopleActivity.class);
                finish();
                startActivity(intent_people);
                break;
            case R.id.change_grid_layout:
                try {
                    loadImages(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.change_linear_layout:
                try {
                    loadImages(2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
    }

    private void loadImages(int opt) throws IOException {
        recyclerView.setHasFixedSize(true);
        if (opt == 1) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        else if (opt == 2){
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
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
                    loadImages(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;}

        }

    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            File photoFile = null;
            try {
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(this,"com.example.gallery", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap)extras.get("data");
            imageView.setImageBitmap(image);
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, ".jpg", storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
