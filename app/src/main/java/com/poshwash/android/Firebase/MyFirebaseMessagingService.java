package com.poshwash.android.Firebase;

/**
 * Created by abhinava on 9/12/2016.
 */

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.poshwash.android.Activity.MainActivity;
import com.poshwash.android.Activity.RatingActivity;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.R;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    int id = (int) (System.currentTimeMillis() * (int) (Math.random() * 100));


    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        MySharedPreferences.setDeviceId(getApplicationContext(), refreshedToken);
        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //  Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("message"));
        sendNotification(remoteMessage.getData().get("message"));


    }

    private void sendNotification(String message) {
        String msg = null;
        Log.e("message", message);
        try {
            JSONObject jsonObject = new JSONObject(message);
            msg = jsonObject.optString("msg");
            int notification_type = Integer.parseInt(jsonObject.getString("Booking_status"));

            switch (notification_type) {
                case 1: {
                  /*  Intent intent = new Intent(this, MainActivity.class);
                    String title = getString(R.string.app_name);
                    notificationManager(intent, title, messagea);*/
                }
                break;
                case 2: {
                    BookingRequestNotification(jsonObject, notification_type);
                }
                break;
                case 3: {
                    BookingRequestNotification(jsonObject, notification_type);
                }
                break;
                case 4: {
                    BookingRequestNotification(jsonObject, notification_type);
                }
                break;
                case 5: {
                    BookingRequestNotification(jsonObject, notification_type);
                }
                break;
                case 6: {
                    BookingRequestNotification(jsonObject, notification_type);
                }
                break;
                case 7: {
                    BookingRequestNotification(jsonObject, notification_type);
                }
                break;
                case 8: {
                    BookingRequestNotification(jsonObject, notification_type);
                }
                break;
                case 11: {
                    BookingRequestNotification(jsonObject, notification_type);
                }
                break;
                case 121: {
                    //  ShowReminderNotification(jsonObject);
                }
                break;
                case 777: {
                    //  showProductsUpdateNotification(messagea);
                }
                break;
                case 778: {
                    //  showBockUser(messagea);
                }
                break;
                default: {
                    Intent intent = new Intent(this, MainActivity.class);
                    String title = getString(R.string.app_name);
                    // notificationManager(intent, title, messagea);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void BookingRequestNotification(JSONObject jsonObject, int type) throws JSONException {
        if (type == 0) {
            Intent i = new Intent("initiated");
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        } else if (type == 1) {
            Intent i = new Intent("initiated");
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        } else if (type == 2) {
            if (Util.isActivityFound(this)) {
                if (AutoSpaApplication.recent_activity.getClass().getName().
                        compareToIgnoreCase("com.poshwash.android.Activity.MainActivity") == 0) {
                    Intent i = new Intent(this, RatingActivity.class);
                    i.putExtra("data", String.valueOf(jsonObject));
                    startActivity(i);
                }
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                String title = getString(R.string.app_name);
                String message = jsonObject.getString("msg");
                notificationManager(intent, title, message);
            }
        } else if (type == 3) {
            Intent i = new Intent("initiated");
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        } else if (type == 4) {
            Intent i = new Intent("initiated");
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        } else if (type == 5) {
            Intent i = new Intent("initiated");
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        } else if (type == 6) {
            Intent i = new Intent("initiated");
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        } else if (type == 7) {
            Intent i = new Intent("initiated");
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        }
        else if (type == 8) {
            Intent i = new Intent("initiated");
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        }
        else if (type == 9) {
            Intent i = new Intent("initiated");
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        }
        Intent intent = new Intent(this, MainActivity.class);
        String title = getString(R.string.app_name);
        String message = jsonObject.getString("msg");
        notificationManager(intent, title, message);

    }

    private void notificationManager(Intent intent1, String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "3")
                        .setSmallIcon(R.drawable.iphone)//R.drawable.ic_stat_ic_notification
                        .setContentTitle("Posh Wash")
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("3",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


    }


}