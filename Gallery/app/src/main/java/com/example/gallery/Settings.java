package com.example.gallery;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    Toolbar mToolbar;
    ActionBar mActionbar;
    SwitchCompat switchCompat;
    SharedPreferences sharedPreferences = null;
    TextView change_lang;
    final String[] list_lang = {"en", "vi", "es", "fr", "ja", "zh"};
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load language
        loadLocale();

        setContentView(R.layout.activity_settings);

        mToolbar = (Toolbar)findViewById(R.id.toolbarSettings);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                String activityName = bundle.getString("CallingActivity");

                if (activityName.equals(MainActivity.class.toString())){
                    Intent intent = new Intent(Settings.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if (activityName.equals(ByDateActivity.class.toString())){
                    Intent intent = new Intent(Settings.this, ByDateActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if (activityName.equals(FavoriteViewActivity.class.toString())){
                    Intent intent = new Intent(Settings.this, FavoriteViewActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if (activityName.equals(LocationViewActivity.class.toString())){
                    Intent intent = new Intent(Settings.this, LocationViewActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if (activityName.equals(LoggedSecurityViewActivity.class.toString())){
                    Intent intent = new Intent(Settings.this, LoggedSecurityViewActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if (activityName.equals(VideoViewActivity.class.toString())){
                    Intent intent = new Intent(Settings.this, VideoViewActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });

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
                    editor.apply();
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchCompat.setChecked(false);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("dark_mode", false);
                    editor.apply();
                }
            }
        });

        change_lang = findViewById(R.id.language_change);
        change_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });
    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {"English", "Tiếng Việt", "Español", "French", "日本語", "中国人"};
        int j = 0;
        for (int i = 0; i < list_lang.length; i++){
            if (list_lang[i].equals(language))
                j = i;
        }

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Settings.this);
        mBuilder.setTitle(R.string.choose_language);
        mBuilder.setSingleChoiceItems(listItems, j, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    //Spanish
                    setLocale("en");
                    recreate();
                }
                else if (which == 1){
                    //French
                    setLocale("vi");
                    recreate();
                }
                else if (which == 2){
                    //Japanese
                    setLocale("es");
                    recreate();
                }
                else if (which == 3){
                    //Vietnamese
                    setLocale("fr");
                    recreate();
                }
                else if (which == 4){
                    //Chinese
                    setLocale("ja");
                    recreate();
                }
                else if (which == 5){
                    //English
                    setLocale("zh");
                    recreate();
                }

                //Dismiss alert dialog when language is selected
                dialog.dismiss();
            }
        });

        //Show alert dialog
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        //Save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("language", MODE_PRIVATE).edit();
        editor.putString("My_lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("language", MODE_PRIVATE);
        language = preferences.getString("My_lang", "");
        setLocale(language);
    }
}