package com.example.jorge.hellojesus.topic;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.topic.fragmentTab.BibleFragment;
import com.example.jorge.hellojesus.topic.fragmentTab.ExcerciseFragemnt;
import com.example.jorge.hellojesus.topic.fragmentTab.InterpretationFragment;
import com.example.jorge.hellojesus.topic.fragmentTab.MusicFragment;
import com.example.jorge.hellojesus.topic.fragmentTab.WriteFragment;

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
            return new ExcerciseFragemnt();
        } else if (position == 3){
            return new InterpretationFragment();
        } else {
            return new WriteFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 5;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.category_bible);
            case 1:
                return mContext.getString(R.string.category_music);
            case 2:
                return mContext.getString(R.string.category_interpretation);
            case 3:
                return mContext.getString(R.string.category_exercise);
            case 4:
                return mContext.getString(R.string.category_write);
            default:
                return null;
        }
    }

}
