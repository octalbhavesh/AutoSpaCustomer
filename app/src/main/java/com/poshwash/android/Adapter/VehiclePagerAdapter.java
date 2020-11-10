package com.poshwash.android.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poshwash.android.Beans.BeanCars;
import com.poshwash.android.Constant.GlobalControl;
import com.poshwash.android.R;
import com.poshwash.android.customViews.CustomTexViewRegular;

import java.util.List;

/**
 * Created by abhinava on 3/1/2018.
 */

public class VehiclePagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater mLayoutInflater;
    private List<BeanCars> vehicleList;
    DisplayImageOptions options;

    public VehiclePagerAdapter(Context context, List<BeanCars> vehicleList) {

        this.vehicleList = vehicleList;
        this.context = context;

        Bitmap default_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.aboutusbanner);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageForEmptyUri(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageOnFail(new BitmapDrawable(context.getResources(), default_bitmap))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        mLayoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return vehicleList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.vehicle_detail_row, container, false);

        AppCompatImageView vehicleIv;
        CustomTexViewRegular vehicleNameTv;
        CustomTexViewRegular vehicleNumberTv;
        CustomTexViewRegular colorTv;
        LinearLayout colorLl;

        vehicleIv = (AppCompatImageView) itemView.findViewById(R.id.vehicleIv);
        vehicleNameTv = (CustomTexViewRegular) itemView.findViewById(R.id.vehicleNameTv);
        vehicleNumberTv = (CustomTexViewRegular) itemView.findViewById(R.id.vehicleNumberTv);
        colorTv = (CustomTexViewRegular) itemView.findViewById(R.id.colorTv);
        colorLl = (LinearLayout) itemView.findViewById(R.id.colorLl);

        vehicleNameTv.setText(vehicleList.get(position).getCar_name());

        if (vehicleList.get(position).getCar_color().compareTo("") == 0) {
            colorLl.setVisibility(View.GONE);
        } else {
            colorLl.setVisibility(View.VISIBLE);
            colorTv.setText(vehicleList.get(position).getCar_color());
        }

        vehicleNumberTv.setText(vehicleList.get(position).getCar_address());

        if (!vehicleList.get(position).getCar_image().equals("")) {
            ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL + vehicleList.get(position).getCar_image(), vehicleIv, options);
        } else {
            vehicleIv.setImageResource(R.drawable.aboutusbanner);
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
