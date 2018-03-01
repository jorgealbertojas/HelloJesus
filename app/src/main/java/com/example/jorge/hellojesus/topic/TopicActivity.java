package com.example.jorge.hellojesus.topic;

import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jorge.hellojesus.R;

import java.util.List;

import static com.example.jorge.hellojesus.main.MainFragment.EXTRA_BUNDLE_MAIN;
import static com.example.jorge.hellojesus.main.MainFragment.EXTRA_MAIN;

public class TopicActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    private static Bundle mBundle;

    private static List<Integer> mTopic;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        // Find the view pager that will allow the user to swipe between fragments
        mViewPager = (ViewPager) findViewById(R.id.viewpager);



        mBundle = getIntent().getBundleExtra(EXTRA_BUNDLE_MAIN);
        mTopic = (List<Integer>) mBundle.getSerializable(EXTRA_MAIN);


    //   QuestionFragment bibleFragment =
    //           (QuestionFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

    //    if (bibleFragment == null) {
            // Create the fragment
    //        bibleFragment = QuestionFragment.newInstance(mTopic);
    //        ActivityUtils.addFragmentToActivity(
    //               getSupportFragmentManager(), bibleFragment, R.id.contentFrame);
    //    }


        initTabLayout();

    }


    private void initTabLayout(){

        // Create an adapter that knows which fragment should be shown on each page
        TopicFragmentPagerAdapter adapter = new TopicFragmentPagerAdapter(this, getSupportFragmentManager());

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        // tabLayout.setupWithViewPager(mViewPager);

        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.category_bible)).setIcon(R.mipmap.ic_bible));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.category_music)).setIcon(R.mipmap.ic_music));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.category_interpretation)).setIcon(R.mipmap.ic_interpretation));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.category_exercise)).setIcon(R.mipmap.ic_exercise));

        // Set the adapter onto the view pager
        mViewPager.setAdapter(adapter);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        tabLayout.setTabTextColors(getResources().getColor(R.color.NoAccent),getResources().getColor(R.color.colorAccent));
        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.NoAccent), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.NoAccent), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.NoAccent), PorterDuff.Mode.SRC_IN);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.NoAccent), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }





}
