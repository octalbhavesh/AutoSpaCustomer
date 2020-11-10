/*
 * Copyright (c) 2014-2015 Amberfog.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.poshwash.android.countryList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.R;

import java.util.ArrayList;


public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder>
{

    private ArrayList<Country> countrylist;
    Context context;
    CountrySelectListener countrySelectListener;

    public void onclickListner(CountrySelectListener countrySelectListener)
    {
        this.countrySelectListener=countrySelectListener;
    }

    public CountryAdapter(Context context, ArrayList<Country> countrylist)
    {
        this.context=context;
        this.countrylist=countrylist;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mImageView;
        public TextView mNameView;
        public TextView mCodeView;
        public LinearLayout row;

        public MyViewHolder(View view) {
            super(view);

            mImageView = (ImageView) view.findViewById(R.id.image);
            mNameView = (TextView) view.findViewById(R.id.country_name);
            mCodeView = (TextView) view.findViewById(R.id.country_code);
            row = (LinearLayout) view.findViewById(R.id.row);

            row.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.row:
                    countrySelectListener.onCountryClick(getAdapterPosition());

                    break;
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_country_drop, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        Country country = countrylist.get(position);
        if (country != null) {
            holder.mNameView.setText(country.getName());
            holder.mCodeView.setText(country.getCountryCodeStr());
            holder.mImageView.setImageResource(country.getResId());
        }

    }

    @Override
    public int getItemCount() {
        return countrylist.size();
    }
}