package com.example.kwesiecommerce.Controller;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.activity.result.ActivityResultLauncher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUploadHandler {
    public static final int PICK_IMAGE_REQUEST = 1;
    private ActivityResultLauncher<Intent> getContentLauncher;

    public ImageUploadHandler(ActivityResultLauncher<Intent> getContentLauncher) {
        this.getContentLauncher = getContentLauncher;
    }

    public void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getContentLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    public Bitmap getBitmapFromUri(Activity activity, Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
    }

    public Uri saveImageToInternalStorage(Context context, Bitmap bitmap, String fileName) {
        File directory = context.getFilesDir();
        File file = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            Log.e("saveToInternalStorage", e.getMessage());
        }

        return Uri.fromFile(file);
    }


    public String getFileExtension(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


}

