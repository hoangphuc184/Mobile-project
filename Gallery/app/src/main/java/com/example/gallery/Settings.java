package com.example.gallery;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

public class Settings extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar mActionbar;
    SwitchCompat switchCompat;
    SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = (Toolbar)findViewById(R.id.toolbarSettings);
        setSupportActionBar(mToolbar);
        mActionbar = getSupportActionBar();
        mActionbar.setHomeAsUpIndicator(R.drawable.ic_back);
        mActionbar.setDisplayHomeAsUpEnabled(true);

//        mToolbar.setNavigationIcon(R.drawable.ic_back);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Settings.this, MainActivity.class);
//                finish();
//                startActivity(intent);
//            }
//        });

        switchCompat = findViewById(R.id.switch_night);

        sharedPreferences = getSharedPreferences("theme", 0);
        Boolean booleanValue = sharedPreferences.getBoolean("dark_mode", true);
        if (booleanValue){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchCompat.setChecked(true);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            switchCompat.setChecked(false);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchCompat.setChecked(true);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("dark_mode", true);
                    editor.commit();
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchCompat.setChecked(false);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("dark_mode", false);
                    editor.commit();
                }
            }
        });

    }
}