package com.poshwash.android.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.poshwash.android.Adapter.PlaceArrayAdapter;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Constant.AutoSpaConstant;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.customViews.CustomButtonBold;
import com.poshwash.android.customViews.CustomEditTextFont;
import com.poshwash.android.customViews.CustomTexViewBold;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends BaseActivity implements View.OnClickListener {

    private DisplayImageOptions options;
    private String fname;
    private Uri outputFileUri;
    private Context context;
    private MyProgressDialog progressDialog;

    private RelativeLayout rrToolbar;
    private ImageView backIcon;
    private CustomTexViewBold txtHeading;
    private CircleImageView userProfleimg;
    private RelativeLayout rrCamera;
    private EditText edtFirstname;
    private EditText edtLastname;
    private TextView edtEnteremail;
    private CustomEditTextFont edtPhonenumber;
    private Spinner spinnerpreferedlanguage;
    private CustomButtonBold btnSave;

    private File photoFile = null;
    private final String TAG = "EditProfile.java";
    private PlaceArrayAdapter autoCompleteAdapter = null;
    private String lat = "", lng = "";
    int PLACE_PICKER_REQUEST = 200;
    String existphone_number = "";
    String languageValue = "eng";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.editprofile);
        context = this;
        initView();
        //initAdapter();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        rrToolbar = (RelativeLayout) findViewById(R.id.rr_toolbar);
        backIcon = (ImageView) findViewById(R.id.back_icon);
        if (MySharedPreferences.getLanguage(context).equalsIgnoreCase("eng")) {
            backIcon.setRotation(0);
        } else {
            backIcon.setRotation(180);
        }
        txtHeading = (CustomTexViewBold) findViewById(R.id.txt_heading);
        userProfleimg = (CircleImageView) findViewById(R.id.user_profleimg);
        rrCamera = (RelativeLayout) findViewById(R.id.rr_camera);
        edtFirstname = (EditText) findViewById(R.id.edt_firstname);
        edtLastname = (EditText) findViewById(R.id.edt_lastname);
        edtEnteremail = (TextView) findViewById(R.id.edt_enteremail);
        edtPhonenumber = (CustomEditTextFont) findViewById(R.id.edt_phonenumber);
        spinnerpreferedlanguage = (Spinner) findViewById(R.id.spinnerpreferedlanguage);
        btnSave = (CustomButtonBold) findViewById(R.id.btn_save);

        btnSave.setOnClickListener(this);
        backIcon.setOnClickListener(this);
        rrCamera.setOnClickListener(this);

        progressDialog = new MyProgressDialog(this);
        progressDialog.setCancelable(false);

        Bitmap default_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defalut_bg);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageForEmptyUri(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageOnFail(new BitmapDrawable(context.getResources(), default_bitmap))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        autoFillData();

        setSpinnerPreferredLang();

    }

    private void setSpinnerPreferredLang() {
        String[] items = {"English", "Arabic"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_row, items);
        spinnerpreferedlanguage.setAdapter(adapter);
    }

    private void autoFillData() {
        edtFirstname.setText(MySharedPreferences.getFirstName(context));
        edtLastname.setText(MySharedPreferences.getLastName(context));
        edtEnteremail.setText(MySharedPreferences.getUserEmail(context));
        if (MySharedPreferences.getLanguage(context).equalsIgnoreCase("eng")) {
            spinnerpreferedlanguage.setSelection(0);

        } else {
            spinnerpreferedlanguage.setSelection(1);
        }
        Util.setProfilePic(this, userProfleimg);
        lat = MySharedPreferences.getLatitude(context);
        lng = MySharedPreferences.getLongitude(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                hideSoftKeyboard();

                if(Util.isConnectingToInternet(mContext))
                submit();
                else
                    Util.errorToast(mContext,mContext.getString(R.string.no_internet_connection));

                break;
            case R.id.back_icon:
                hideSoftKeyboard();
                finish();

                break;
            case R.id.rr_camera:
                if (Build.VERSION.SDK_INT >= 23)
                    checkRequestPermission();
                else
                    openImageIntent();
                break;
        }
    }

    private void checkRequestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                    }, 1);
        } else {
            openImageIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], int[] grantResults) {
        int i = 0;
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageIntent();
            }
        }
    }

    private void submit() {
        // validate
        String fname = edtFirstname.getText().toString().trim();
        if (TextUtils.isEmpty(fname)) {
            Util.Toast(context, getString(R.string.enter_first_name));
            return;
        }
        String lname = edtLastname.getText().toString().trim();
        String enteremail = edtEnteremail.getText().toString().trim();

        if (!TextUtils.isEmpty(enteremail) && !Util.isValidEmail(enteremail)) {
            Util.Toast(context, getString(R.string.validation_error));
            return;
        }
        languageValue = spinnerpreferedlanguage.getSelectedItem().equals("English") ? "eng" : "ara";
        editProfileWebservice(fname, lname, enteremail, languageValue);
    }

    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void openImageIntent() {
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator
                + "Wayndr" + File.separator);
        root.mkdirs();
        fname = Util.getUniqueImageName();
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EditProfile.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("path", String.valueOf(outputFileUri));
        editor.apply();
        Log.e("SetUpProfile", "Uri is " + outputFileUri);
        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        startActivityForResult(chooserIntent, AutoSpaConstant.YOUR_SELECT_PICTURE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AutoSpaConstant.YOUR_SELECT_PICTURE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EditProfile.this);
                    String value = prefs.getString("path", "error");
                    selectedImageUri = Uri.parse(value);
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }

                if (selectedImageUri == null) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EditProfile.this);
                    String value = prefs.getString("path", "error");
                    selectedImageUri = Uri.parse(value);
                }

                Bitmap bitmap = null;
                Log.d("SetUpProfile", "Uri cropped is " + outputFileUri);
                //     bitmap = Util.getBitmap(EditProfile.this, selectedImageUri);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    photoFile = new File(Util.getPath(context, selectedImageUri));
                    userProfleimg.setImageBitmap(Util.rotateImageBitmap(photoFile.getAbsolutePath(), bitmap));
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
    }

    // Call webservice
    private void editProfileWebservice(final String fname, final String lname, final String enteremail, final String language) {
        progressDialog.show();

        HashMap<String, RequestBody> map = null;
        Call<ResponseBody> call = null;
        try {
            map = new HashMap<>();
            map.put("user_id", setMultipart(MySharedPreferences.getUserId(context)));
            map.put("device_id", setMultipart(MySharedPreferences.getDeviceId(context)));
            map.put("device_type", setMultipart(getString(R.string.android)));
            map.put("first_name", setMultipart(fname));
            map.put("last_name", setMultipart(lname));
            map.put("email", setMultipart(enteremail));
            map.put("latitude", setMultipart(lat));
            map.put("longitude", setMultipart(lng));
            map.put("language", setMultipart(language));
            MultipartBody.Part firstImage = null;
            if (photoFile != null) {
                RequestBody requestFileWall1 = RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
                firstImage = MultipartBody.Part.createFormData("image", photoFile.getName(), requestFileWall1);
            }

            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            if (firstImage != null)
                call = myApiEndpointInterface.editProfile(map, firstImage);
            else
                call = myApiEndpointInterface.editProfileWithoutImage(map);

            final MultipartBody.Part finalFirstImage = firstImage;
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        JSONObject mJsonObj = new JSONObject(response.body().string());
                        Log.e("tg", "response from server = " + mJsonObj.toString());
                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                            Util.Toast(context, mJsonObj.getJSONObject("response").getString("message"));
                            MySharedPreferences.setLanguage(context, language);
                            AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                            finish();
                        } else {
                            Util.Toast(context, mJsonObj.getJSONObject("response").getString("message"));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Util.dismissPrograssDialog(progressDialog);
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            Util.dismissPrograssDialog(progressDialog);
            Log.e(TAG, e.toString());
        }
    }

    public RequestBody setMultipart(String data) {
        RequestBody part_data = RequestBody.create(MediaType.parse("multipart/form-data"), data);
        return part_data;
    }

    /*private void VerfyMobileWebService(final String phonenumber) {
        progressDialog.show();
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("contact", phonenumber);

            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.generateOtp(new ConvertJsonToMap().jsonToMap(jsonObject));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        JSONObject mJsonObj = new JSONObject(new String(response.body().bytes()));
                        Log.e("tg", "response from server = " + mJsonObj.toString());

                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                            Intent intent = new Intent(context, PhoneNumberVerify.class);
                            intent.putExtra("number", phonenumber);
                            intent.putExtra("otp", mJsonObj.getJSONObject("response").getJSONArray("data").getJSONObject(0).getString("otp"));

                            startActivity(intent);
                            finish();

                        } else {
                            Util.Toast(context, mJsonObj.getJSONObject("response").getString("message"));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Util.dismissPrograssDialog(progressDialog);
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            Util.dismissPrograssDialog(progressDialog);
            e.printStackTrace();
        }
    }*/
}