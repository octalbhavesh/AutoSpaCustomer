package com.poshwash.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.poshwash.android.Beans.BeanCarsNew;
import com.poshwash.android.R;

import java.util.ArrayList;


public class SelectCarsAdpater extends BaseAdapter {
    public ArrayList<BeanCarsNew> list;
    Context context;

    public SelectCarsAdpater(Context context, ArrayList<BeanCarsNew> list1) {
        this.context = context;
        this.list = list1;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView car_name;
        RadioButton rb;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.choose_car_list_row, null);
            ViewHolder holder = new ViewHolder();

            holder.car_name = (TextView) convertView.findViewById(R.id.car_name);
            holder.rb = (RadioButton) convertView.findViewById(R.id.check_img);

            convertView.setTag(holder);
            //   list.clear();
        }
        final ViewHolder holder1 = (ViewHolder) convertView.getTag();

        String name = list.get(position).getName();
        holder1.car_name.setText(list.get(position).getName() + " - " + list.get(position).getPlate_number());

        return convertView;
    }


}
