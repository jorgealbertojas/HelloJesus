package com.example.jorge.hellojesus.topic;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jorge.hellojesus.topic.fragmentTab.BibleFragment;
import com.example.jorge.hellojesus.topic.fragmentTab.ExerciseFragment;

import com.example.jorge.hellojesus.topic.fragmentTab.MusicFragment;
import com.example.jorge.hellojesus.topic.fragmentTab.MusicSingFragment;

/**
 * Created by jorge on 23/02/2018.
 */

public class TopicFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public TopicFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new BibleFragment();
        } else if (position == 1){
            return new MusicFragment();
        } else if (position == 2){
            return new ExerciseFragment();
        } else if (position == 3){
            return new MusicSingFragment();
        } else {
            return null;
        }


    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 4;
    }











}
