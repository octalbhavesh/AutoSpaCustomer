package com.poshwash.android.Utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poshwash.android.Activity.Login;
import com.poshwash.android.Activity.TrackingActivity;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anandj on 3/16/2017.
 */

public class Util {

    final static String TAG = "Util.java";
    private static Dialog dialog;

    public static String getUniqueImageName() {
        //will generate a random num
        //between 15-10000
        Random r = new Random();
        int num = r.nextInt(10000 - 15) + 15;
        String fileName = "img_" + num + ".png";
        return fileName;
    }
    public static boolean isActivityFound(Context context) {
        boolean isActivityFound = false;
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
            if (services.get(0).topActivity.getPackageName().equalsIgnoreCase(context.getPackageName())) {
                isActivityFound = true;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return isActivityFound;
    }
    public static PubNub pubnub; // Pubnub instance

    /*
             Creates PNConfiguration instance and enters Pubnub credentials to create Pubnub instance.
             This Pubnub instance will be used whenever we need to create connection to Pubnub.
          */
    private void initPubnub() {

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(TrackingActivity.PUBNUB_SUBSCRIBE_KEY);
        pnConfiguration.setPublishKey(TrackingActivity.PUBNUB_PUBLISH_KEY);
        pnConfiguration.setSecure(true);
        pubnub = new PubNub(pnConfiguration);
    }

    public static void showErrorSnackBar(View view, String msg, Context context) {
        try {
            Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.red_400));
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setAsterisk(EditText editText, String msg) {
        //    String txt = getString(R.string.password);
        String colored = " *";
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(msg);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(builder);

    }
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
        }
        return false;
    }
    public static void setAsterisk(TextInputLayout editText, String msg) {
        //    String txt = getString(R.string.password);
        String colored = " *";


        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(msg);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        editText.setHint(builder);

     /*   String mm = "Legal first name";
        Spannable WordtoSpan = new SpannableString(mm);
        WordtoSpan.setSpan(
                new ForegroundColorSpan(Color.parseColor("#e62e6c")),
                16,
                mm.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        WordtoSpan.setSpan(new RelativeSizeSpan(0.9f), 16, mm.length(), 0);
        editText.setHint(WordtoSpan);*/

    }

    public synchronized static SpannableStringBuilder getSpannableText(String text, String colorCode) {
        SpannableStringBuilder span1 = new SpannableStringBuilder(text);
        ForegroundColorSpan color1 = new ForegroundColorSpan(Color.parseColor(colorCode));
        span1.setSpan(color1, 0, span1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return span1;
    }


    public static void setError(Context context, EditText text) {
        text.setHint(context.getString(R.string.cannotleftblank));
        text.setHintTextColor(ContextCompat.getColor(context, R.color.greencolor));
        //  text.requestFocus();
    }

    public static void setError(Context context, TextView text) {
        text.setError(context.getString(R.string.cannotleftblank));
        text.requestFocus();
    }

    public static void showAlertDialog(Context context, String title, String msg) {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);
        aBuilder.setTitle(title).setMessage(msg).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setIcon(R.drawable.appicon);

        AlertDialog alert = aBuilder.create();
        alert.show();
        Button button = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setTextColor(ContextCompat.getColor(context, R.color.greencolor));
    }

    public static void showAlertDialogForSignin(final Context context) {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);
        aBuilder.setTitle(context.getString(R.string.app_name))
                .setMessage(context.getString(R.string.login_alert))
                .setIcon(R.drawable.appicon)
                .setPositiveButton(context.getString(R.string.signin)
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LoginManager.getInstance().logOut();
                                String deivce_token = MySharedPreferences.getDeviceId(context);
                                String requestSave = MySharedPreferences.getRequest(context);
                                String langague = MySharedPreferences.getLanguage(context);
                                MySharedPreferences.ClearAll(context);
                                MySharedPreferences.setUserEmail(context, "");
                                MySharedPreferences.setIsFirstTime(context, "");
                                MySharedPreferences.setDeviceId(context, deivce_token);
                                MySharedPreferences.setRequest(context, requestSave);
                                MySharedPreferences.setLanguage(context, langague);
                                MySharedPreferences.setIsFirstTime(context, "yes");

                                Intent intent = new Intent(context, Login.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                                dialogInterface.dismiss();

                            }
                        }).setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = aBuilder.create();
        alert.show();
        Button button = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setTextColor(ContextCompat.getColor(context, R.color.greencolor));
    }

    public static Bitmap rotateImageBitmap(String photoPath, Bitmap bitmap) {
        Bitmap rotatedBitmap = null;
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {

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
                    return bitmap;

                case ExifInterface.ORIENTATION_UNDEFINED:
                    return bitmap;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static void showAlertDialogWithNoTitle(Context context, String msg) {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);
        aBuilder.setMessage(msg).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = aBuilder.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        nbutton.setTextColor(ContextCompat.getColor(context, R.color.greencolor));
       /* Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(Color.YELLOW);*/
    }

    public static void setError(Context context, EditText text, String msg) {
        text.setError(msg);
        text.requestFocus();
    }

    public static void setErrorTextInputLayout(Context context, TextInputLayout textInputLayout) {
        String msg = context.getString(R.string.cannotleftblank);
        textInputLayout.setError(Html.fromHtml("<font color='#8cc739'>" + msg + "</font>"));

    }

    public static void setErrorTextInputLayout(TextInputLayout textInputLayout, String msg) {
        //textInputLayout.setError(msg);
        textInputLayout.setError(Html.fromHtml("<font color='#8cc739'>" + msg + "</font>"));
    }

    public static void removeErrorTextInputLayput(TextInputLayout textInputLayout) {
        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(false);
    }

    public static void dismissPrograssDialog(MyProgressDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static Typeface setTypefaceRegular(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/jf_flat_regular.ttf");
    }

    public static String getFbProfileImageUrl(String id) {
        return "https://graph.facebook.com/" + id + "/picture?width=200&height=150";
    }

    public static void startNewActivity(Intent intent, Activity activity) {
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    public static String getPathForBelowKitkat(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
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
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

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
                final String[] selectionArgs = new String[]{
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

        return "Not Found";
    }

    public static void showProgress(Context mContext) {
        dialog = new Dialog(mContext, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custome_progress_dialog);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static void sucessToast(Context context, String message) {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
        mdToast.show();
    }

    public static void errorToast(Context context, String message) {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
        mdToast.show();
    }

    public static void warningToast(Context context, String message) {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
        mdToast.show();
    }
    public static void selectLanguage(final Context context, final EditText textView, int defaultSelected) {
        final String[] items = {context.getString(R.string.english), context.getString(R.string.arabic)};
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.singlechoiceselectitem);

        final RadioButton c1 = (RadioButton) dialog1.findViewById(R.id.radio1);
        final RadioButton c2 = (RadioButton) dialog1.findViewById(R.id.radio2);
        TextView txtTitle = (TextView) dialog1.findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.select_language);
        c1.setText(items[0]);
        c2.setText(items[1]);

        if (defaultSelected == 0) {
            c1.setChecked(true);
            c2.setChecked(false);
        } else {
            c1.setChecked(false);
            c2.setChecked(true);
        }

     /*   c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(items[1]);
                c1.setChecked(false);
                dialog1.dismiss();
                if (context instanceof Register) {
                    ((Register) context).setlanguage("ara");
                } else if (context instanceof EditProfile) {
                    ((EditProfile) context).setlanguage("ara");
                }
            }
        });
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(items[0]);
                c2.setChecked(false);
                dialog1.dismiss();
                if (context instanceof Register) {
                    ((Register) context).setlanguage("eng");
                } else if (context instanceof EditProfile) {
                    ((EditProfile) context).setlanguage("eng");
                }
            }
        });*/

        dialog1.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog1.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog1.show();
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
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

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    public static boolean isValidEmail(String target) {

        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static Typeface setTypefaceBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/jf_flat_regular.ttf");
    }

    public static void callWebserviceUpdateLocation(String lat, String lng, Context context) {
        Call<ResponseBody> call = null;
        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = new JSONObject();
            jsonObject1.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject1.put("lat", lat);
            jsonObject1.put("lng", lng);

            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = myApiEndpointInterface.updateLocation(new ConvertJsonToMap().jsonToMap(jsonObject1));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject mjJsonObject = new JSONObject(new String(response.body().bytes()));
                        if (mjJsonObject.getJSONObject("response").getBoolean("status")) {
                            // Log.v(TAG,mjJsonObject.getJSONObject("response").getString("message"));
                        } else {
                            //Log.e(TAG,mjJsonObject.getJSONObject("response").getString("message"));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public static void setProfilePic(Context context, ImageView imageView) {
        Bitmap default_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.defalut_bg);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageForEmptyUri(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageOnFail(new BitmapDrawable(context.getResources(), default_bitmap))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        if (MySharedPreferences.getProfileImage(context).compareTo("") != 0) {
            try {
                ImageLoader.getInstance().displayImage(MySharedPreferences.getProfileImage(context), imageView, options);
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.defalut_bg);
            }
        } else {
            imageView.setImageResource(R.drawable.defalut_bg);
        }
    }


    public static String ConvertDate(String datea, Context context) {
        String convert_date = "";
        try {
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dt.parse(datea);
            SimpleDateFormat dt1 = new SimpleDateFormat("hh.mm a , dd MMM yyyy");

            //  date.getHours()
            // convert_date = dt1.format(date) + " on " + dt2.format(date);

            if (DateFormat.is24HourFormat(context)) {
                if (date.getHours() > 12)
                    convert_date = dt1.format(date);
                    //convert_date = dt1.format(date) + " on " + dt2.format(date);
                else
                    convert_date = dt1.format(date);
            } else
                convert_date = dt1.format(date);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return convert_date;
    }


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Za-z]).{6,20}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static Bitmap getBitmap(Context context, Uri u) {

        Uri uri = u;
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 60000; // around 300kb
            in = context.getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("Test", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = context.getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
//                int height = b.getHeight();
//                int width = b.getWidth();
                int height = 340;
                int width = 340;

                Log.d("Test", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

          /*  Log.d("Test", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());*/
            return b;
        } catch (Exception e) {
            Log.e("Test", e.getMessage(), e);
            return null;
        }
    }

    public static String SetRating(String rate) {
        double i2 = Double.parseDouble(rate);
        String rating = new DecimalFormat("#.#").format(i2);

        return rating;
    }


    public static String DisplayAmount(String amount) {
        double i2 = Double.parseDouble(amount);
        String amt = new DecimalFormat("0.00").format(i2);

        return amt;
    }

    public static String PayAmount(String amount)// remove .00 after decimal
    {
        double i2 = Double.parseDouble(amount);
        String amt = new DecimalFormat("##.##").format(i2);

        return amt;
    }

    public static void Toast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

    public static void hideKeyboard(Context context, AutoCompleteTextView autoCompleteTextView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String convertUtcTimeToLocal(String dateStr, boolean changeFormetStatus, Context context) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = df.parse(dateStr);
        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(date);
        //  if (changeFormetStatus)
        return ConvertDate(formattedDate, context);
        /*else
            return formattedDate;*/
    }

    public static void setImageUsingImageLoader(Context context, ImageView imageView, String lat, String lng) {
        Bitmap default_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.appicon);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageForEmptyUri(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageOnFail(new BitmapDrawable(context.getResources(), default_bitmap))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        String icon = "http://115.112.57.155:8080/okowasch/img/pin.ico";
        String maputl = "http://maps.googleapis.com/maps/api/staticmap?zoom=17&size=512x512&markers=" + icon + "|" + lat + "," + lng;

        Log.d("url", maputl);
        //http://maps.googleapis.com/maps/api/staticmap?zoom=17&size=512x512&markers=icon:http://cdn.sstatic.net/Sites/stackoverflow/img/favicon.ico|34.052230,%20-118.243680
        //  if (MySharedPreferences.getProfileImage(context).compareTo("") != 0) {
        try {
            ImageLoader.getInstance().displayImage(maputl, imageView, options);
        } catch (Exception e) {

            imageView.setImageResource(R.drawable.user);
            //  }
        }
    }

    public static String convertRetrofitResponce(Response<ResponseBody> responce) {
        try {
            InputStream inputStream = responce.body().byteStream();

            BufferedReader bR = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";

            StringBuilder responseStrBuilder = new StringBuilder();
            while ((line = bR.readLine()) != null) {

                responseStrBuilder.append(line);
            }
            inputStream.close();
            return responseStrBuilder.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static DisplayMetrics getScreenMetrics(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    @SuppressWarnings("unchecked")
    public static HashMap reverseGeocode(double lat, double lon, Context context) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        HashMap map = new HashMap();
        try {

            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String state_name = addresses.get(0).getAddressLine(1);
            String country_name = addresses.get(0).getAddressLine(2);
            String knownName = addresses.get(0).getFeatureName();
            /* String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();*/

            map.put("address", address + ", " + state_name + ", " + country_name);
            map.put("name", knownName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }

    public static String getPageName(int pagetype, Context context) {
        switch (pagetype) {
            case 0:
                return context.getResources().getString(R.string.aboutus);
            case 1:
                return context.getResources().getString(R.string.contactus);
            case 2:
                return context.getResources().getString(R.string.privacyplicies);
            case 3:
                return context.getResources().getString(R.string.termscondition);
            default:
                return null;

        }
    }

    public static boolean isValidDate(String txt) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MM-yyyy");
            Date date = inputFormat.parse(txt);
            Date todayDate = inputFormat.parse(inputFormat.format(new Date()));

            if (date.after(todayDate)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }


    public static String calculateTimeDiffFromNow(String time, Context context) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);// 2017-11-14T10:01:40.095Z
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.setLenient(false);

        Date date = new Date();
        TimeZone timeZone = TimeZone.getDefault();

        //   Date prevTime = sdf.parse(time.replaceAll("Z$", "+0000"));
        Date prevTime = sdf.parse(time);
        sdf.setTimeZone(timeZone);
        long prevMillisecond = prevTime.getTime();
//        long pm = prevMillisecond + 3600000;
        long diff = date.getTime() - prevMillisecond;
        String hms = String.format("%02d hr :%02d min :%02d sec", TimeUnit.MILLISECONDS.toHours(diff),
                TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)),
                TimeUnit.MILLISECONDS.toSeconds(diff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff)));

        String valToReturn;


        //show only seconds
        if (diff <= 1000 * 60) {
            long val = diff / 1000;
            //if(val)
            //valToReturn = val + " Sec";
            valToReturn = context.getString(R.string.just_now);
        }
        //show only minutes
        else if (diff <= 1000 * 60 * 60) {
            long val = diff / (1000 * 60);
            valToReturn = val + " " + context.getString(R.string.minute_text);
        }

        //show only hours
        else {
            long val = diff / (1000 * 60 * 60);
            if (val <= 24)
                valToReturn = val + " " + context.getString(R.string.hour);
            else {
                // show only days
                val = val / 24;
                if (val <= 7)
                    valToReturn = val + " " + context.getString(R.string.days);
                    // when days are more than 7 show only date
                else
                    return ConvertDateTimeZone(time);
            }
        }

        Log.i("Utils.java", "Time diff is " + hms);
        // valToReturn = valToReturn.equalsIgnoreCase(context.getString(R.string.just_now)) ?valToReturn:valToReturn + " " + context.getString(R.string.ago);
        if (!valToReturn.equalsIgnoreCase(context.getString(R.string.just_now)))
            return valToReturn + " " + context.getString(R.string.ago);
        else
            return valToReturn;
    }

    public static String calculateTimeDiffFromNowWithoutUTC(String time, Context context) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);// 2017-11-14T10:01:40.095Z
        //   sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        //   sdf.setLenient(false);

        Date date = new Date();
        //  TimeZone timeZone = TimeZone.getDefault();

        //   Date prevTime = sdf.parse(time.replaceAll("Z$", "+0000"));
        Date prevTime = sdf.parse(time);
        //   sdf.setTimeZone(timeZone);
        long prevMillisecond = prevTime.getTime();
//        long pm = prevMillisecond + 3600000;
        long diff = date.getTime() - prevMillisecond;
        String hms = String.format("%02d hr :%02d min :%02d sec", TimeUnit.MILLISECONDS.toHours(diff),
                TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)),
                TimeUnit.MILLISECONDS.toSeconds(diff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff)));

        String valToReturn;


        //show only seconds
        if (diff <= 1000 * 60) {
            long val = diff / 1000;
            //if(val)
            //valToReturn = val + " Sec";
            valToReturn = context.getString(R.string.just_now);
        }
        //show only minutes
        else if (diff <= 1000 * 60 * 60) {
            long val = diff / (1000 * 60);
            valToReturn = val + " " + context.getString(R.string.minute_text);
        }

        //show only hours
        else {
            long val = diff / (1000 * 60 * 60);
            if (val <= 24)
                valToReturn = val + " " + context.getString(R.string.hour);
            else {
                // show only days
                val = val / 24;
                if (val <= 7)
                    valToReturn = val + " " + context.getString(R.string.days);
                    // when days are more than 7 show only date
                else
                    return ConvertDateTimeZone(time);
            }
        }

        Log.i("Utils.java", "Time diff is " + hms);
        // valToReturn = valToReturn.equalsIgnoreCase(context.getString(R.string.just_now)) ?valToReturn:valToReturn + " " + context.getString(R.string.ago);
        if (!valToReturn.equalsIgnoreCase(context.getString(R.string.just_now)))
            return valToReturn + " " + context.getString(R.string.ago);
        else
            return valToReturn;
    }

    public static String ConvertDateTimeZone(String datea) {
        String convert_date = "";
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);// 2017-11-14T10:01:40.095Z
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            sdf.setLenient(false);

            Date prevTime = sdf.parse(datea);

            SimpleDateFormat dt1 = new SimpleDateFormat("hh.mm a , dd MMM yyyy");

            convert_date = dt1.format(prevTime);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convert_date;
    }

    public static boolean CheckDate(String datea) {
        boolean isValid = true;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 2017-11-14T10:01:40.095Z

            Date selectedDate = sdf.parse(datea);
            Calendar currentcal = Calendar.getInstance();
            Date currentDate = currentcal.getTime();

            if (selectedDate.after(currentDate)) {
                isValid = false;
            } else {
                isValid = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static String getStringByLocal(String id) {
        return String.format(Locale.ENGLISH, id);
    }

    public static String Time24to12(String tim) {
        String time = "";

        String t[] = tim.split(" - ");
        SimpleDateFormat dfFrom = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dfTO = new SimpleDateFormat("hh:mm a");

        Date date = null;

        try {
            date = dfFrom.parse(t[0]);
            time = dfTO.format(date);

            date = dfFrom.parse(t[1]);
            time = time + " - " + dfTO.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String Time24to12WithLocale(String tim) {
        String time = "";

        String t[] = tim.split(" - ");
        SimpleDateFormat dfFrom = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dfTO = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        Date date = null;
        try {
            date = dfFrom.parse(t[0]);
            time = dfTO.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String Time12to24(String tim) {
        String time = "";
        String t[] = tim.split(" - ");
        SimpleDateFormat dfFrom = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat dfTO = new SimpleDateFormat("HH:mm");

        Date date = null;

        try {
            date = dfFrom.parse(t[0]);
            time = dfTO.format(date);

            date = dfFrom.parse(t[1]);
            time = time + " - " + dfTO.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String convertTimeInDetail(String tim) {
        String time = "";
        SimpleDateFormat dfFrom = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dfTO = new SimpleDateFormat("dd MMM.yyyy");

        Date date = null;

        try {
            date = dfFrom.parse(tim);
            time = dfTO.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static boolean getTimeDifference(String booking_time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm a , dd MMM yyyy");
        try {
            Date oldDate = dateFormat.parse(booking_time);
            Date currentDate = new Date();
            long diff = oldDate.getTime() - currentDate.getTime();
            long diffInMin = TimeUnit.MILLISECONDS.toMinutes(diff);
            return diffInMin > 0 && diffInMin <= 15;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
