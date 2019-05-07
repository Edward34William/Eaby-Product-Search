package com.example.aninterface;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import static com.android.volley.VolleyLog.d;

public class fragAdapter extends FragmentStatePagerAdapter {
    private String[] tabs = new String[]{"search", "wish list"};

    public fragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int i) {
        if(i == 0){
            Search search = new Search();
            Bundle bundle = new Bundle();
            bundle.putString("message", "Hello From Page: " + i++);
            search.setArguments(bundle);
            return search;
        }else{
            Wish wish = new Wish();
            d("wish id", wish.getId());
            Bundle bundle = new Bundle();
            bundle.putString("message", "Hello From Page: " + i++);
            wish.setArguments(bundle);
            return wish;
        }
    }

    @Override
    public int getCount() {
        return tabs.length;

    }
}
