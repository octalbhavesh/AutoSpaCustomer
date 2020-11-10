package com.poshwash.android.Utils;

import android.content.Context;

public class Env {
    public static Context appContext;
    public static Context currentActivity;

    public static void init(Context appContext) {
        Env.appContext = appContext;
    }
}
