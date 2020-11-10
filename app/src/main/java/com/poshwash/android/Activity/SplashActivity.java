
package com.poshwash.android.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.poshwash.android.R;
import com.poshwash.android.Utils.LocationService;
import com.poshwash.android.Utils.MySharedPreferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends BaseActivity {
    public final static int REQUEST_LOCATION = 199;
    static int SPLASH_HOLD_TIME = 3000;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        startService(new Intent(SplashActivity.this, LocationService.class));
        keyHash();
        Fabric.with(this, new Crashlytics());
        holdSplashScreen();
    }

    private void holdSplashScreen() {
        Calendar cal = Calendar.getInstance();
        int date1 = cal.get(Calendar.DATE);
        int month1 = cal.get(Calendar.MONTH);

        Log.e("date:", String.valueOf(date1));
        Log.e("month:", String.valueOf(month1));
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(SPLASH_HOLD_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GoNext();
            }
        };
        timerThread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        int i = 0;
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GoNext();
            } else {
                finish();
            }
        }
    }

    public void GoNext() {
        Intent intent1 = null;
        if (MySharedPreferences.getUserId(SplashActivity.this).equals("")) {
            intent1 = new Intent(SplashActivity.this, Login.class);
        } else {
            intent1 = new Intent(SplashActivity.this, MainActivity.class);
        }
        startActivity(intent1);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult()", Integer.toString(resultCode));
        if (requestCode == REQUEST_LOCATION) {
            switch (resultCode) {
                case Activity.RESULT_OK: {
                    holdSplashScreen();
                    break;
                }
                case Activity.RESULT_CANCELED: {
                    finish();
                    break;
                }
            }
        }
    }

    public void keyHash() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.os.autospa", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
}