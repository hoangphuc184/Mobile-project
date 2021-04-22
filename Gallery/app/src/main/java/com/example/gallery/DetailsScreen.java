package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;

public class DetailsScreen extends AppCompatActivity {
    String path;
    TextView date;
    TextView name;
    TextView filePath;
    TextView size;
    TextView width_height;

    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);

        Intent i = getIntent();
        path = i.getExtras().getString("path");

        File file = new File(path);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        date = findViewById(R.id.create_date);
        date.setText(sdf.format(file.lastModified()));
        name =findViewById(R.id.file_name_tv);
        name.setText(file.getName());
        filePath = findViewById(R.id.path_file_tv);
        filePath.setText(path);
        size = findViewById(R.id.size);
        size.setText(String.format("%.2f", (double) file.length() / (1024 * 1024)) + " MB");
        width_height = findViewById(R.id.width_heigh);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        width_height.setText(imageWidth + " x " + imageHeight);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}