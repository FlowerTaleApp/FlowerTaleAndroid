package com.flowertale.flowertaleandroid.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class BitmapUtil {

    public static Bitmap compress(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        return BitmapFactory.decodeFile(file.getPath(), options);
    }
}
