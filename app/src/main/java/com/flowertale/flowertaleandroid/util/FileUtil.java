package com.flowertale.flowertaleandroid.util;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class FileUtil {

    public static String convertToBase64(Bitmap scaledBitmap) {
        if (scaledBitmap == null || scaledBitmap.isRecycled()) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        byte[] bytes = out.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static String getImagePath(Activity activity, Uri uri, String selection) {
        String path = null;
        Cursor cursor = activity.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public static void qualityCompress(Bitmap bitmap, File file) {
        // size of the image to be uploaded restricted less than 1M
        final int size = 1 * 1024 * 1024;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int quality = 90;
        while (baos.toByteArray().length > size) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
