package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

public class FullScreenActivity extends AppCompatActivity implements TagPop.ExampleDialogListener {
    Toolbar toolbar;
    BottomNavigationView bottomAppBar;

    String path;
    String desPath;

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;


    final int READ_REQUEST_CODE = 42;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),  Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void copyFile(String inputPath, String inputFile, String outputPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath +"/" + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;
        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        switch (requestCode){

            case READ_REQUEST_CODE:
                Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri,
                        DocumentsContract.getTreeDocumentId(uri));
                desPath = getPath(this, docUri);

                File f = new File(path);
                copyFile(path, f.getName(), desPath);
                break;
            case 101:
                ImageView myImage = (ImageView) findViewById(R.id.full_screen);
                myImage.setImageURI(uri);
                break;
        }

    }


    private void openDialog() {
        TagPop tagPop = new TagPop();
        tagPop.show(getSupportFragmentManager(), "Example");
    }

    private void deleteImage(String path) {
        File file = new File(path);

        String[] projection = {MediaStore.Images.Media._ID};

        // Match on the file path
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{file.getAbsolutePath()};

        // Query for the ID of the media matching the file path
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst()) {
            // We found the ID. Deleting the item via the content provider will also remove the file
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
        }
        c.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_screen);

        Intent i = getIntent();
        path = i.getExtras().getString("path");
        File imgFile = new  File(path);

        toolbar = findViewById(R.id.toolbarFullscreen);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(path);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        String activityName = i.getExtras().getString("PreviousActivity");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(activityName);
                if (activityName.equals(MainActivity.class.toString())){
                    Intent intent = new Intent(FullScreenActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if (activityName.equals(ByDateActivity.class.toString())){
                    Intent intent = new Intent(FullScreenActivity.this, ByDateActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if (activityName.equals(FavoriteViewActivity.class.toString())){
                    Intent intent = new Intent(FullScreenActivity.this, FavoriteViewActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if (activityName.equals(LocationViewActivity.class.toString())){
                    Intent intent = new Intent(FullScreenActivity.this, LocationViewActivity.class);
                    finish();
                    startActivity(intent);
                }
                else if (activityName.equals(LoggedSecurityViewActivity.class.toString())){
                    Intent intent = new Intent(FullScreenActivity.this, LoggedSecurityViewActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });



        bottomAppBar = findViewById(R.id.bottombarFullscreen);

        Menu menu = bottomAppBar.getMenu();
        MenuItem fav = menu.findItem(R.id.Favorite);
        sharedPreferences = getSharedPreferences("State", 0);
        Boolean stateImage = sharedPreferences.getBoolean(path, false);
        System.out.println(stateImage);
        if (stateImage){
            fav.setIcon(R.drawable.ic_fav_yes);
        }
        else{
            fav.setIcon(R.drawable.ic_fav_no);
        }
        bottomAppBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Favorite:

                        if(!stateImage){
                            fav.setIcon(R.drawable.ic_fav_yes);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(path, true);
                            editor.apply();
                            System.out.println("Selected");
                            recreate();
                        }
                        else{
                            fav.setIcon(R.drawable.ic_fav_no);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(path, false);
                            editor.apply();
                            System.out.println("Not selected");
                            recreate();
                        }
                        break;
                    case R.id.Edit:
                        Intent edit = new Intent(FullScreenActivity.this, DsPhotoEditorActivity.class);
                        edit.setData(Uri.fromFile(new File(path)));

                        edit.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Photo Editor");

                        edit.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.parseColor("#333333"));

                        edit.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, Color.parseColor("#FFFFFF"));

                        edit.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
                                new int[]{DsPhotoEditorActivity.TOOL_WARMTH,
                                        DsPhotoEditorActivity.TOOL_PIXELATE,
                                        DsPhotoEditorActivity.TOOL_FRAME,
                                        DsPhotoEditorActivity.TOOL_ROUND,
                                        DsPhotoEditorActivity.TOOL_VIGNETTE,
                                        DsPhotoEditorActivity.TOOL_SHARPNESS,
                                });
                        startActivityForResult(edit, 101);
                        break;
                    case R.id.Delete:
                        deleteImage(path);
                        System.out.println(activityName);
                        if (activityName.equals(MainActivity.class.toString())){
                            Intent intent = new Intent(FullScreenActivity.this, MainActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        else if (activityName.equals(ByDateActivity.class.toString())){
                            Intent intent = new Intent(FullScreenActivity.this, ByDateActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        else if (activityName.equals(FavoriteViewActivity.class.toString())){
                            Intent intent = new Intent(FullScreenActivity.this, FavoriteViewActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        else if (activityName.equals(LocationViewActivity.class.toString())){
                            Intent intent = new Intent(FullScreenActivity.this, LocationViewActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        else if (activityName.equals(LoggedSecurityViewActivity.class.toString())){
                            Intent intent = new Intent(FullScreenActivity.this, LoggedSecurityViewActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        break;
                    case R.id.Share:
                        File file = new File(path);
                        Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() +
                                ".provider", file);
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/*");
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(share);
                        break;
                }
                return true;
            }
        });

//        btnMore = findViewById(R.id.btnMore);
//        btnMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showMenu();
//            }
//        });

//        btnBack = findViewById(R.id.btnBack);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String activityName = i.getExtras().getString("PreviousActivity");
//                System.out.println(activityName);
//
//                if (activityName.equals(MainActivity.class.toString())){
//                    Intent intent = new Intent(FullScreenActivity.this, MainActivity.class);
//                    finish();
//                    startActivity(intent);
//                }
//                else if (activityName.equals(ByDateActivity.class.toString())){
//                    Intent intent = new Intent(FullScreenActivity.this, ByDateActivity.class);
//                    finish();
//                    startActivity(intent);
//                }
//                else if (activityName.equals(FavoriteViewActivity.class.toString())){
//                    Intent intent = new Intent(FullScreenActivity.this, FavoriteViewActivity.class);
//                    finish();
//                    startActivity(intent);
//                }
//                else if (activityName.equals(LocationViewActivity.class.toString())){
//                    Intent intent = new Intent(FullScreenActivity.this, LocationViewActivity.class);
//                    finish();
//                    startActivity(intent);
//                }
//                else if (activityName.equals(LoggedSecurityViewActivity.class.toString())){
//                    Intent intent = new Intent(FullScreenActivity.this, LoggedSecurityViewActivity.class);
//                    finish();
//                    startActivity(intent);
//                }
//            }
//        });

//        btnFav = findViewById(R.id.btnFavorite);
//
//        System.out.println(stateImage);
//        if (stateImage){
//            btnFav.setImageResource(R.drawable.ic_fav_yes);
//        }
//        else{
//            btnFav.setImageResource(R.drawable.ic_fav_no);
//        }
//        btnFav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btnFav.setSelected(!btnFav.isSelected());
//
//                if (btnFav.isSelected()) {
//                    btnFav.setImageResource(R.drawable.ic_fav_yes);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean(path, true);
//                    editor.apply();
//                    System.out.println("Selected");
//                } else {
//                    btnFav.setImageResource(R.drawable.ic_fav_no);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean(path, false);
//                    editor.apply();
//                    System.out.println("Not selected");
//                }
//            }
//        });

        if(imgFile.exists()){

            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ExifInterface ei = null;

            try {
                ei = new ExifInterface(imgFile.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap rotatedBitmap = null;
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            ImageView myImage = findViewById(R.id.full_screen);

            myImage.setImageBitmap(rotatedBitmap);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_img_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.details:
                try {
                    listOfImageByLoc.listOfImageByLocation(getApplicationContext(), "LONG AN");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent2 = new Intent(FullScreenActivity.this, DetailsScreen.class);
                finish();
                intent2.putExtra("path", path);
                startActivity(intent2);
                break;
            case R.id.setWall:
                File f1 = new File(path);
                if(f1.exists()) {
                    Bitmap bmp = BitmapFactory.decodeFile(path);
                    WallpaperManager m=WallpaperManager.getInstance(getApplicationContext());
                    try {
                        m.setBitmap(bmp);
                        Toast.makeText(getApplicationContext(), "Wallpaper set", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error occurs while setting Wallpaper ", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.copy:
                Intent choose = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(choose, READ_REQUEST_CODE);
                break;
            case R.id.tag:
                openDialog();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void applyTexts(String loc) {
        if (!loc.equals("")){
            loc = loc.toUpperCase();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            editor = sharedPreferences.edit();

            editor.putString(path, loc);
            //editor.clear();
            editor.commit();
            Toast.makeText(this, "Location set", Toast.LENGTH_SHORT).show();
        }
    }
}