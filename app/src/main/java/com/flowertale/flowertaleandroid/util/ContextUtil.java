package com.flowertale.flowertaleandroid.util;

import android.content.Context;

public class ContextUtil {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void initial(Context c) {
        context = c;
    }
}
