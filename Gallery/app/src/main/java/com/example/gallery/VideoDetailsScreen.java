package com.example.gallery;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;

public class VideoDetailsScreen extends AppCompatActivity {
    String path;
    TextView date;
    TextView name;
    TextView filePath;
    TextView size;
    TextView width_height;
    TextView length;

    ImageButton btnBackDVV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details_screen);


        Intent i = getIntent();
        path = i.getExtras().getString("path");


        File file = new File(path);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        date = findViewById(R.id.create_date_DVV);
        date.setText(sdf.format(file.lastModified()));
        name =findViewById(R.id.file_name_tv_DVV);
        name.setText(file.getName());
        filePath = findViewById(R.id.path_file_tv_DVV);
        filePath.setText(path);
        size = findViewById(R.id.size);
        size.setText(String.format("%.2f", (double) file.length() / (1024 * 1024)) + " MB");


        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);

        width_height = findViewById(R.id.width_heigh_DVV);
        width_height.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH) + " x " + mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

        length = findViewById(R.id.length);
        long minutes = (Integer.valueOf(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000 ) / 60;
        long seconds = (Integer.valueOf(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000 ) % 60;
        length.setText(minutes+ ":"+seconds );

        btnBackDVV = findViewById(R.id.btnBackDVV);
        btnBackDVV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(VideoDetailsScreen.this, VideoPlayActivity.class);
                intent2.putExtra("Video", path);
                finish();
                startActivity(intent2);
            }
        });
    }
}