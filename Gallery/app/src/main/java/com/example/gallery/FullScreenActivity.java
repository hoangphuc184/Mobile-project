package com.example.gallery;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

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

public class FullScreenActivity extends AppCompatActivity {
    ImageButton btnMore;
    ImageButton btnBack;
    String path;
    String desPath;
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
        Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri,
                DocumentsContract.getTreeDocumentId(uri));
        desPath = getPath(this, docUri);
        File f = new File(path);
        copyFile(path, f.getName(), desPath);
    }

    private void showMenu(){
        PopupMenu popupMenu = new PopupMenu(this, btnMore);
        popupMenu.getMenuInflater().inflate(R.menu.setting_img_view, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        deleteImage(path);
                        Intent intent = new Intent(FullScreenActivity.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.copy:
                        Intent choose = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                        startActivityForResult(choose, READ_REQUEST_CODE);
                        break;
                    case R.id.share:
//                        File imageFile = new File(path);
//                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//                        Bitmap icon = bitmap;
//                        Intent share = new Intent(Intent.ACTION_SEND);
//                        share.setType("image/jpeg");
//                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
//                        try {
//                            f.createNewFile();
//                            FileOutputStream fo = new FileOutputStream(f);
//                            fo.write(bytes.toByteArray());
//                            fo.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        System.out.println("GGGGG "+FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", f));
//                        share.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", f));
//                        startActivity(Intent.createChooser(share, "Share Image"));
                        File file = new File(path);
                        Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/*");
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(share);
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
                    case R.id.details:
                        Intent intent2 = new Intent(FullScreenActivity.this, DetailsScreen.class);
                        finish();
                        intent2.putExtra("path", path);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
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

        btnMore = findViewById(R.id.btnMore);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
            ImageView myImage = (ImageView) findViewById(R.id.full_screen);

            myImage.setImageBitmap(rotatedBitmap);

        }
    }
}