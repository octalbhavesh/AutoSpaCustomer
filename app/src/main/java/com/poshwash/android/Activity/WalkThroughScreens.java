package com.poshwash.android.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Fragment.WalkThroughFragment;
import com.poshwash.android.R;
import com.poshwash.android.Utils.MySharedPreferences;
import com.viewpagerindicator.CirclePageIndicator;


/**
 * Created by abhinava on 1/11/2017
 */

public class WalkThroughScreens extends AppCompatActivity implements View.OnClickListener {

    Context context;
    private TextView done;
    private ViewPager mPager;
    private CirclePageIndicator mIndicator;

    private ImagePagerAdapter mAdapter;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.walkthrough);

        initializeView();
    }

    private void initializeView() {

        mIndicator = (CirclePageIndicator) findViewById(R.id.mIndicator);
        mPager = (ViewPager) findViewById(R.id.mPager);

        done = (TextView) findViewById(R.id.done);

        done.setOnClickListener(this);

        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), 3);
        mPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mPager);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    done.setVisibility(View.VISIBLE);
                } else {
                    done.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // Fragment mCurrentFragment = tabsPageradapter.getRegisteredFragment(pager.getCurrentItem());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                if (getIntent().getBooleanExtra("jump_screen_splash", false)) {
                    Intent intent = new Intent(WalkThroughScreens.this,
                            Login.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    finish();
                } else {
                    finish();
                }
                break;
        }
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public ImagePagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int index) {

            return WalkThroughFragment.newInstance(index);
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return mNumOfTabs;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
    }

    @Override
    public void onBackPressed() {

    }
}
