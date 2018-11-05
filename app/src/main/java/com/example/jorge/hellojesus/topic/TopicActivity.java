package com.example.jorge.hellojesus.topic;

import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.hellojesus.R;

import java.util.List;

import static com.example.jorge.hellojesus.main.MainFragment.EXTRA_BUNDLE_MAIN;
import static com.example.jorge.hellojesus.main.MainFragment.EXTRA_MAIN;
import static com.example.jorge.hellojesus.main.MainFragment.EXTRA_STRING_TITLE;

public class TopicActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    private static Bundle mBundle;

    private static List<Integer> mTopic;

    public static TabLayout tabLayout;

    public static TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        // Find the view pager that will allow the user to swipe between fragments
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTitle = (TextView) findViewById(R.id.tv_main_title);

        mBundle = getIntent().getBundleExtra(EXTRA_BUNDLE_MAIN);
        mTopic = (List<Integer>) mBundle.getSerializable(EXTRA_MAIN);
        mTitle.setText(getIntent().getStringExtra(EXTRA_STRING_TITLE));





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
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        // tabLayout.setupWithViewPager(mViewPager);

        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.category_bible)).setIcon(R.mipmap.ic_bible));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.category_music)).setIcon(R.mipmap.ic_music));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.category_write)).setIcon(R.mipmap.ic_write));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.category_exercise)).setIcon(R.mipmap.ic_exercise));

        // Set the adapter onto the view pager
        mViewPager.setAdapter(adapter);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_bible);
        imageView.setTransitionName("imagem1");

        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.mipmap.ic_music);
        imageView2.setTransitionName("imagem2");

        ImageView imageView3 = new ImageView(this);
        imageView3.setImageResource(R.mipmap.ic_write);
        imageView3.setTransitionName("imagem3");

        ImageView imageView4 = new ImageView(this);
        imageView4.setImageResource(R.mipmap.ic_exercise);
        imageView4.setTransitionName("imagem4");

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        tabLayout.setTabTextColors(getResources().getColor(R.color.NoAccent2),getResources().getColor(R.color.colorAccent));
        tabLayout.getTabAt(0).setCustomView(imageView);
        tabLayout.getTabAt(1).setCustomView(imageView2);
        tabLayout.getTabAt(2).setCustomView(imageView3);
        tabLayout.getTabAt(3).setCustomView(imageView4);
        ((ImageView) tabLayout.getTabAt(0).getCustomView()).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        ((ImageView) tabLayout.getTabAt(1).getCustomView()).setColorFilter(getResources().getColor(R.color.NoAccent2), PorterDuff.Mode.SRC_IN);
        ((ImageView) tabLayout.getTabAt(2).getCustomView()).setColorFilter(getResources().getColor(R.color.NoAccent2), PorterDuff.Mode.SRC_IN);
        ((ImageView) tabLayout.getTabAt(3).getCustomView()).setColorFilter(getResources().getColor(R.color.NoAccent2), PorterDuff.Mode.SRC_IN);
       // tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        //tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.NoAccent), PorterDuff.Mode.SRC_IN);
       // tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.NoAccent), PorterDuff.Mode.SRC_IN);
       // tabLayout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.NoAccent), PorterDuff.Mode.SRC_IN);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition());
                mViewPager.setCurrentItem(tab.getPosition());
                //tab.getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                ((ImageView) tabLayout.getTabAt(tab.getPosition()).getCustomView()).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
               // tab.getIcon().setColorFilter(getResources().getColor(R.color.NoAccent), PorterDuff.Mode.SRC_IN);
                ((ImageView) tabLayout.getTabAt(tab.getPosition()).getCustomView()).setColorFilter(getResources().getColor(R.color.NoAccent2), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }





}
