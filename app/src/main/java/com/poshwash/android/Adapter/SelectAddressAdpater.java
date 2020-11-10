package com.poshwash.android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.poshwash.android.Beans.BeanAddresses;
import com.poshwash.android.R;

import java.util.ArrayList;


public class SelectAddressAdpater extends BaseAdapter {
    public ArrayList<BeanAddresses> list;
    Context context;
    int PLACE_PICKER_REQUEST = 200;

    public SelectAddressAdpater(Context context, ArrayList<BeanAddresses> list1) {

        this.context = context;
        this.list = list1;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    class ViewHolder {
        TextView week_day;
        TextView addNewLocationTv;
        ImageView check_img;
        RelativeLayout addLocationLayout;
        TextView spi_vehiclelocation;
        TextView addLocationTv;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.weekdays_list_row, null);
            ViewHolder holder = new ViewHolder();

            holder.week_day = (TextView) convertView.findViewById(R.id.week_day);
            holder.addNewLocationTv = (TextView) convertView.findViewById(R.id.addNewLocationTv);
            holder.spi_vehiclelocation = (TextView) convertView.findViewById(R.id.spi_vehiclelocation);
            holder.addLocationTv = (TextView) convertView.findViewById(R.id.addLocationTv);
            holder.check_img = (ImageView) convertView.findViewById(R.id.check_img);
            holder.addLocationLayout = (RelativeLayout) convertView.findViewById(R.id.addLocationLayout);
            convertView.setTag(holder);
        }

        final ViewHolder holder1 = (ViewHolder) convertView.getTag();

        holder1.week_day.setText(list.get(position).getAddress());

        if (list.get(position).is_selected()) {
            holder1.check_img.setImageResource(R.drawable.circleselect);
        } else {
            holder1.check_img.setImageResource(R.drawable.circleunselct);
        }

        if (position == (list.size() - 1)) {
            holder1.addNewLocationTv.setVisibility(View.VISIBLE);
        } else {
            holder1.addNewLocationTv.setVisibility(View.GONE);
        }

        holder1.addNewLocationTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    ((Activity) context).startActivityForResult(builder.build(((Activity) context)), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

              /*  holder1.addLocationLayout.setVisibility(View.VISIBLE);
                holder1.addNewLocationTv.setVisibility(View.GONE);*/
            }
        });

        holder1.spi_vehiclelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }


}
