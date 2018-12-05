package com.example.jorge.hellojesus.topic;

import android.Manifest;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jorge.hellojesus.R;

import java.util.List;

import static com.example.jorge.hellojesus.main.MainFragment.EXTRA_BUNDLE_MAIN;
import static com.example.jorge.hellojesus.main.MainFragment.EXTRA_MAIN;
import static com.example.jorge.hellojesus.main.MainFragment.EXTRA_STRING_TITLE;


public class TopicActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;

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

        requestPermission();

    }


    private void initTabLayout(){

        // Create an adapter that knows which fragment should be shown on each page
        TopicFragmentPagerAdapter adapter = new TopicFragmentPagerAdapter(this, getSupportFragmentManager(),mTitle.getText().toString());

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        // tabLayout.setupWithViewPager(mViewPager);

        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.category_bible)).setIcon(R.mipmap.ic_bible));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.category_music)).setIcon(R.mipmap.ic_music));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.category_write)).setIcon(R.mipmap.ic_write));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.category_exercise)).setIcon(R.mipmap.ic_exercise));

        // Set the adapter onto the view pager
        mViewPager.setAdapter(adapter);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.background), (int) getResources().getDimension(R.dimen.background));



        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_bible);
        imageView.setTransitionName("imagem1");
        imageView.setLayoutParams(layoutParams);
        imageView.setPadding((int) getResources().getDimension(R.dimen.background_padding),(int) getResources().getDimension(R.dimen.background_padding), (int) getResources().getDimension(R.dimen.background_padding), (int) getResources().getDimension(R.dimen.background_padding));
        imageView.setBackground(getDrawable(R.drawable.duplicates_notif));

        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.mipmap.ic_music);
        imageView2.setTransitionName("imagem2");
        imageView2.setLayoutParams(layoutParams);
        imageView2.setPadding((int) getResources().getDimension(R.dimen.background_padding),(int) getResources().getDimension(R.dimen.background_padding), (int) getResources().getDimension(R.dimen.background_padding), (int) getResources().getDimension(R.dimen.background_padding));
        imageView2.setBackground(getDrawable(R.drawable.duplicates_notif));

        ImageView imageView3 = new ImageView(this);
        imageView3.setImageResource(R.mipmap.ic_write);
        imageView3.setTransitionName("imagem3");
        imageView3.setLayoutParams(layoutParams);
        imageView3.setPadding((int) getResources().getDimension(R.dimen.background_padding),(int) getResources().getDimension(R.dimen.background_padding), (int) getResources().getDimension(R.dimen.background_padding), (int) getResources().getDimension(R.dimen.background_padding));
        imageView3.setBackground(getDrawable(R.drawable.duplicates_notif));

        ImageView imageView4 = new ImageView(this);
        imageView4.setImageResource(R.mipmap.ic_exercise);
        imageView4.setTransitionName("imagem4");
        imageView4.setLayoutParams(layoutParams);
        imageView4.setPadding((int) getResources().getDimension(R.dimen.background_padding),(int) getResources().getDimension(R.dimen.background_padding), (int) getResources().getDimension(R.dimen.background_padding), (int) getResources().getDimension(R.dimen.background_padding));
        imageView4.setBackground(getDrawable(R.drawable.duplicates_notif));

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

    /**
     * Request Permission download for the user .
     */
    private void requestPermission(){

        ActivityCompat.requestPermissions( this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_RECORD_AUDIO_PERMISSION);


    }






}
