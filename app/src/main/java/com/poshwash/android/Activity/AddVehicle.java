package com.poshwash.android.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Constant.I;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.AppUtils;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.customViews.CustomTexViewBold;
import com.poshwash.android.response.AddVehicleResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVehicle extends BaseActivity {

    private final String TAG = "AddVehicle.java";
    int inputSelection = -1;
    String vehicleType = "";
    private Button btn_submit;
    private ImageView ivUpload;
    private RelativeLayout layoutUpload;
    private EditText etPlateNumber;
    private Spinner spn_make, spn_model, spn_color;
    private ArrayList<String> list_make = new ArrayList<>();
    private ArrayList<String> list_model = new ArrayList<>();
    private ArrayList<AddVehicleResponse> list_model1 = new ArrayList<>();
    private String userId, modelId, makeId, vehicleId, color, plateNumber;
    private String fname;
    private Uri outputFileUri;
    private CustomTexViewBold tvTitle;
    private File photoFile = null;
    private ImageView ivBack;
    private String type, mSelectedColor, mVehicleId;
    private long btnNext = 0;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.add_vehicle);
        type = getIntent().getExtras().getString("type");

        initView();
    }


    private void initView() {
        tvTitle = (CustomTexViewBold) findViewById(R.id.title_tv);
        ivBack = (ImageView) findViewById(R.id.toggle_icon);
        layoutUpload = (RelativeLayout) findViewById(R.id.layout_upload);
        ivUpload = (ImageView) findViewById(R.id.btn_browse);
        spn_make = (Spinner) findViewById(R.id.spinner_make);
        spn_model = (Spinner) findViewById(R.id.spinner_model);
        spn_color = (Spinner) findViewById(R.id.spinner_color);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        etPlateNumber = (EditText) findViewById(R.id.et_platenumber);

        try {
            if (type.equalsIgnoreCase("add")) {
                spn_make.setEnabled(true);
                spn_model.setEnabled(true);
                etPlateNumber.setEnabled(true);
                layoutUpload.setEnabled(true);
                tvTitle.setText(getString(R.string.add_vehicle));
                //  mVehicleId = getIntent().getExtras().getString("id");
            } else {
                tvTitle.setText(getString(R.string.edit_vehicle));
                spn_make.setEnabled(false);
                spn_model.setEnabled(false);
                etPlateNumber.setEnabled(false);
                layoutUpload.setEnabled(false);
                String number = getIntent().getExtras().getString("plate_number");
                mSelectedColor = getIntent().getExtras().getString("color");
                mVehicleId = getIntent().getExtras().getString("id");
                etPlateNumber.setText(number);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            callPackageDetailsApi(CountrySelectListener.APIRequest.get_package_details(
                    MySharedPreferences.getUserId(mContext), MySharedPreferences.getLanguage(mContext)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        layoutUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23)
                    checkRequestPermission();
                else
                    openImageIntent();
            }
        });

        ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23)
                    checkRequestPermission();
                else
                    openImageIntent();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - btnNext < 1000) {
                    return;
                }
                btnNext = SystemClock.elapsedRealtime();
                if (TextUtils.isEmpty(etPlateNumber.getText().toString().trim())) {
                    errorToast(mContext, "Enter plate number");
                } else {
                    int platSize = etPlateNumber.getText().toString().trim().length();
                    boolean valueContain = etPlateNumber.getText().toString().contains("-");
                    Log.e("value", String.valueOf(valueContain));
                    if (valueContain) {
                        errorToast(mContext, "Enter valid plate number");
                    } else {
                        if (type.equalsIgnoreCase("edit")) {
                            callWebServiceEditVehicles();
                        } else {

                            callWebServiceAddVehicles();
                        }
                    }


                }
            }
        });

    }

    private void checkRequestPermission() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(mContext), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA,
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

    private void openImageIntent() {
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "tripstatus" + File.separator);
        root.mkdirs();
        fname = AppUtils.getUniqueImageName();
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("path", String.valueOf(outputFileUri));
        editor.apply();
        Log.e("SetUpProfile", "Uri is " + outputFileUri);


        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = mContext.getPackageManager();
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
        final Intent chooserIntent = Intent.createChooser(galleryIntent, getResources().getString(R.string.select_source));

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        startActivityForResult(chooserIntent, I.YOUR_SELECT_PICTURE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == I.YOUR_SELECT_PICTURE_REQUEST_CODE) {
                selectImage(data);
            }
        }
    }

    public void selectImage(Intent data) {
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
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            String value = prefs.getString("path", "error");
            selectedImageUri = Uri.parse(value);
        } else {
            selectedImageUri = data.getData();
        }

        if (selectedImageUri == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            String value = prefs.getString("path", "error");
            selectedImageUri = Uri.parse(value);
        }

        Bitmap bitmap = null;
        Log.d("SetUpProfile", "Uri cropped is " + outputFileUri);
        bitmap = AppUtils.getBitmap(mContext, selectedImageUri);

        if (bitmap != null) {
            Uri tempUri = null;
            try {
                tempUri = getImageUri(getApplicationContext(), bitmap);
                photoFile = new File(getRealPathFromURI(tempUri));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // photoFile = new File(Utils.getPath(mContext, selectedImageUri));

            try {
                ivUpload.setImageBitmap(bitmap);
            } catch (Exception e) {

            }

        }


    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(img, 270);
            default:
                return img;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void callWebServiceAddVehicles() {
        showProgress(mContext);
        Call<ResponseBody> call = null;
        JSONObject jsonObject1 = null;

        HashMap<String, RequestBody> map = null;
        String pNumber1 = etPlateNumber.getText().toString().trim();

        Log.e("plat_number", pNumber1);

        try {
            jsonObject1 = new JSONObject();
            map = new HashMap<>();
            map.put("user_id", setMultipart(MySharedPreferences.getUserId(mContext)));
            map.put("vehicle_model_id", setMultipart(modelId));
            map.put("make_id", setMultipart(makeId));
            map.put("plate_number", setMultipart(pNumber1));
            map.put("color", setMultipart(mSelectedColor));
            map.put("vehicle_type_id", setMultipart(vehicleId));
            map.put("language", setMultipart(MySharedPreferences.getLanguage(mContext)));
            MultipartBody.Part firstImage = null;
            if (photoFile != null) {
                RequestBody requestFileWall1 = RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
                firstImage = MultipartBody.Part.createFormData("vehicle_picture", photoFile.getName(), requestFileWall1);
            }
            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().
                    getRequestQueue().create(MyApiEndpointInterface.class);
            if (type.equalsIgnoreCase("add")) {
                if (photoFile != null)
                    call = myApiEndpointInterface.addVehicle(map, firstImage);
                else
                    call = myApiEndpointInterface.addVehicle(map);

            } else {
                if (photoFile != null)
                    call = myApiEndpointInterface.updateVehicle(map, firstImage);
                else
                    call = myApiEndpointInterface.updateVehicle(map);
            }

            /*
             */
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    btn_submit.setEnabled(true);
                    try {
                        hideProgress();
                        JSONObject jsonObject1 = new JSONObject(response.body().string());
                        String message = jsonObject1.getJSONObject("response").getString("message");
                        if (jsonObject1.getJSONObject("response").getBoolean("status")) {
                            sucessToast(mContext, message);
                            finish();
                        } else {
                            errorToast(mContext, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {

        }
    }

    private void callWebServiceEditVehicles() {
        showProgress(mContext);
        Call<ResponseBody> call = null;
        JSONObject jsonObject1 = null;
        HashMap<String, RequestBody> map = null;
        try {
            jsonObject1 = new JSONObject();
            map = new HashMap<>();
            map.put("user_id", setMultipart(MySharedPreferences.getUserId(mContext)));
            map.put("color", setMultipart(mSelectedColor));
            map.put("vehicle_id", setMultipart(mVehicleId));
            map.put("language", setMultipart(MySharedPreferences.getLanguage(mContext)));
            MultipartBody.Part firstImage = null;
            if (photoFile != null) {
                RequestBody requestFileWall1 = RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
                firstImage = MultipartBody.Part.createFormData("vehicle_picture", photoFile.getName(), requestFileWall1);
            }
            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().
                    getRequestQueue().create(MyApiEndpointInterface.class);
            if (type.equalsIgnoreCase("add")) {
                if (photoFile != null)
                    call = myApiEndpointInterface.addVehicle(map, firstImage);
                else
                    call = myApiEndpointInterface.addVehicle(map);

            } else {
                if (photoFile != null)
                    call = myApiEndpointInterface.updateVehicle(map, firstImage);
                else
                    call = myApiEndpointInterface.updateVehicle(map);
            }

            /*
             */
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    btn_submit.setEnabled(true);
                    try {
                        hideProgress();
                        JSONObject jsonObject1 = new JSONObject(response.body().string());
                        String message = jsonObject1.getJSONObject("response").getString("message");
                        if (jsonObject1.getJSONObject("response").getBoolean("status")) {
                            sucessToast(mContext, message);
                            finish();
                        } else {
                            errorToast(mContext, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {

        }
    }

    public RequestBody setMultipart(String data) {
        RequestBody part_data = RequestBody.create(MediaType.parse("multipart/form-data"), data);
        return part_data;
    }


    /*APIs*/
    private void callPackageDetailsApi(Map<String, Object> package_details) {
        showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.get_make_modal(package_details);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    hideProgress();
                    list_make.clear();
                    list_model1.clear();
                    String response1 = response.body().string();
                    JSONObject jsonObject = new JSONObject(response1);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("response");
                    boolean status1 = jsonObject1.optBoolean("status");

                    if (status1) {
                        JSONObject jsonObject2 = jsonObject1.optJSONObject("data");
                        JSONArray jsonArray = jsonObject2.optJSONArray("makes");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject3 = jsonArray.optJSONObject(i);
                            JSONObject jsonMake = jsonObject3.optJSONObject("Make");
                            String make = jsonMake.optString("name");
                            list_make.add(make);

                            AddVehicleResponse model = new AddVehicleResponse();
                            JSONArray jsonModel = jsonObject3.optJSONArray("VehicleModel");
                            model.setModel(String.valueOf(jsonModel));
                            list_model1.add(model);
                        }
                        setMakeData();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgress();
            }
        });
    }

    private void setMakeData() {
        list_model.clear();

        setColor();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_row, list_make);
        spn_make.setAdapter(adapter);

        spn_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedTextValue = adapterView.getItemAtPosition(position).toString();
                final String vehicleModel = list_model1.get(position).getModel();
                list_model.clear();
                try {
                    final JSONArray jsonArray = new JSONArray(vehicleModel);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        String model = jsonObject.optString("name");
                        list_model.add(model);
                    }

                    if (list_model.size() > 0) {
                        ArrayAdapter<String> adapterModel = new ArrayAdapter<String>(mContext, R.layout.spinner_row, list_model);
                        spn_model.setAdapter(adapterModel);

                        spn_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                String selectedTextValue = adapterView.getItemAtPosition(position).toString();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                                    String model = jsonObject.optString("name");
                                    if (selectedTextValue.equalsIgnoreCase(model)) {
                                        makeId = jsonObject.optString("make_id");
                                        modelId = jsonObject.optString("id");
                                        vehicleId = jsonObject.optString("vehicle_type_id");
                                    }
                                }
                                Log.e("model", vehicleId);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void setColor() {
        String[] list_color = {"Blue", "Brown", "Cyan", "Gray", "Green", "LightGray", "Orange", "Purple", "Red", "White", "Yellow", "Black"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_row, list_color);
        spn_color.setAdapter(adapter);
        if (mSelectedColor != null) {
            int spinnerPosition = adapter.getPosition(mSelectedColor);
            spn_color.setSelection(spinnerPosition);
        }
        spn_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedTextValue = adapterView.getItemAtPosition(position).toString();
                mSelectedColor = selectedTextValue;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}
