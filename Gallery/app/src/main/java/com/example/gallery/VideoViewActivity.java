package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class VideoViewActivity extends AppCompatActivity {
    ImageButton btnAlbum;
    ImageButton btnVideo;
    ImageButton btnLoc;
    ImageButton btnFav;
    ImageButton btnSec;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<VideoModel> listOfVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_section);

        recyclerView= findViewById(R.id.recyclerview_gallery_videos);
        layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        listOfVideo = new ArrayList<>();
        fetchVideoFromGallery();

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