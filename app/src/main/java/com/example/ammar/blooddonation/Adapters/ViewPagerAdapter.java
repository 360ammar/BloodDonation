package com.example.ammar.blooddonation.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ammar.blooddonation.AvailableDonors;
import com.example.ammar.blooddonation.BloodRequest;
import com.example.ammar.blooddonation.RequiredBlood;

/**
 * Created by Ammar on 11/16/2017.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new RequiredBlood();
        }
        else if (position == 1)
        {
            fragment = new BloodRequest();
        }
        else if (position == 2)
        {
            fragment = new AvailableDonors();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Blood Request";
        }
        else if (position == 1)
        {
            title = "Donate Blood";
        }
       else if (position == 2)
        {
            title = "Available Donors";
        }
        return title;
    }
}
