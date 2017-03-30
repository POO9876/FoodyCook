package com.example.hilary.foodycook.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.hilary.foodycook.MyFoodsFragment;
import com.example.hilary.foodycook.PostFoodFragment;

/**
 * Created by hilary on 3/29/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PostFoodFragment tab1 = new PostFoodFragment();
                return tab1;
            case 1:
                MyFoodsFragment tab2 = new MyFoodsFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
