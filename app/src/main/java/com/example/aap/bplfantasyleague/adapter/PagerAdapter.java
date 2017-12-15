package com.example.aap.bplfantasyleague.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.aap.bplfantasyleague.fragment.Tab1;
import com.example.aap.bplfantasyleague.fragment.Tab2;
import com.example.aap.bplfantasyleague.fragment.Tab3;
import com.example.aap.bplfantasyleague.fragment.Tab4;
import com.example.aap.bplfantasyleague.fragment.Tab5;

/**
 * Created by AAP on 11/6/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int tabNo;
    String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PagerAdapter(FragmentManager fm, int tabNo,String userId) {
        super(fm);
        this.tabNo=tabNo;
        this.userId=userId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Tab1 tab1 = new Tab1();
                tab1.setUserId(userId);
                return tab1;
            case 1:
                Tab2 tab2 = new Tab2();
                tab2.setUserId(userId);
                return tab2;
            case 2:
                Tab3 tab3 = new Tab3();
                tab3.setUserId(userId);
                return tab3;
            case 3:
                Tab4 tab4 = new Tab4();
                tab4.setUserId(userId);
                return tab4;
            case 4:
                Tab5 tab5 = new Tab5();
                tab5.setUserId(userId);
                return tab5;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabNo;
    }
}
