package com.poshwash.android.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poshwash.android.Beans.BeanPromoCodes;
import com.poshwash.android.R;

import java.util.List;

/**
 * Created by anandj on 3/17/2017.
 */

public class AppliedPromocodeAdapter extends RecyclerView.Adapter<AppliedPromocodeAdapter.MyViewHolder> {

    private List<BeanPromoCodes> promoList;
    Context context;
    boolean isDelete = false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RelativeLayout parent_rl;
        public ImageView crossIv;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_promocodename);
            parent_rl = (RelativeLayout) view.findViewById(R.id.parent_rl);
            crossIv = (ImageView) view.findViewById(R.id.crossIv);
        }
    }

    public AppliedPromocodeAdapter(Context context, List<BeanPromoCodes> promoList, boolean isDelete) {
        this.promoList = promoList;
        this.context = context;
        this.isDelete = isDelete;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.applied_promocode_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.title.setText(promoList.get(position).getPromoCode() + ": " + context.getString(R.string.congratulation_you_have) + " " + promoList.get(position).getDiscount() + context.getString(R.string.discount_on_your_car_wash));

        if (isDelete) {
            holder.crossIv.setVisibility(View.VISIBLE);
        } else {
            holder.crossIv.setVisibility(View.GONE);
        }
        holder.crossIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return promoList.size();
    }
}