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

import java.util.ArrayList;

public class VideoViewActivity extends AppCompatActivity {
//    ImageButton btnAlbum;
//    ImageButton btnVideo;
//    ImageButton btnLoc;
//    ImageButton btnFav;
//    ImageButton btnSec;
    private Toolbar toolbar;
    private ActionBar myActionbar;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<VideoModel> listOfVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_section);

        toolbar = findViewById(R.id.toolbarVideo);
        setSupportActionBar(toolbar);

        myActionbar = getSupportActionBar();
        myActionbar.setTitle("Videos");
        myActionbar.setDisplayHomeAsUpEnabled(true);

        recyclerView= findViewById(R.id.recyclerview_gallery_videos);
        layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        listOfVideo = new ArrayList<>();
        fetchVideoFromGallery();

//        btnAlbum = (ImageButton)findViewById(R.id.photos_view);
//        btnAlbum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(VideoViewActivity.this, MainActivity.class);
//                finish();
//                startActivity(intent);
//            }
//        });
//
//        btnVideo = (ImageButton)findViewById(R.id.videos_view);
//        btnVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(VideoViewActivity.this, VideoViewActivity.class);
//                finish();
//                startActivity(intent);
//            }
//        });
//
//        btnLoc = (ImageButton)findViewById(R.id.location_view);
//        btnLoc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(VideoViewActivity.this, LocationViewActivity.class);
//                finish();
//                startActivity(intent);
//            }
//        });
//
//        btnFav = (ImageButton)findViewById(R.id.favorite_view);
//        btnFav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(VideoViewActivity.this, FavoriteViewActivity.class);
//                finish();
//                startActivity(intent);
//            }
//        });
//
//        btnSec = (ImageButton)findViewById(R.id.secure_view);
//        btnSec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(VideoViewActivity.this, SecurityViewActivity.class);
//                startActivity(intent);
//            }
//        });

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

    private void fetchVideoFromGallery() {

        Uri uri;
        Cursor cursor;
        int columnIndex, thumbnail;

        String absolutePath;

        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String projection[] = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Thumbnails.DATA,
        };

        String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        thumbnail = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

        while (cursor.moveToNext()){
            absolutePath = cursor.getString(columnIndex);

            VideoModel videoModel = new VideoModel();
            videoModel.setSelected(false);
            videoModel.setVideoPath(absolutePath);
            videoModel.setVideoThumb(cursor.getString(thumbnail));

            listOfVideo.add(videoModel);
        }
        VideoAdapter videoAdapter = new VideoAdapter(getApplicationContext(), listOfVideo, VideoViewActivity.this);
        recyclerView.setAdapter(videoAdapter);

    }
}