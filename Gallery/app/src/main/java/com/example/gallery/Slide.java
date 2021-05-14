package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.ArrayList;

public class Slide extends AppCompatActivity {
    ViewPager mViewPager;
    ImageButton btnBack;
    // images array
    ArrayList<String> images = new ArrayList<>();

    // Creating Object of ViewPagerAdapter
    SlideAdapter mViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);


        btnBack = findViewById(R.id.slideBtnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ss = new Intent(Slide.this, MainActivity.class);
                finish();
                startActivity(ss);
            }
        });
        // Initializing the ViewPager Object
        mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);

        try {
            images = ImagesGallery.listOfImages(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new SlideAdapter(this, images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);

    }
}