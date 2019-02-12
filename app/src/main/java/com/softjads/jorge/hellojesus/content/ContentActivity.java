package com.softjads.jorge.hellojesus.content;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.softjads.jorge.hellojesus.R;
import com.softjads.jorge.hellojesus.data.onLine.topic.model.Content;
import com.softjads.jorge.hellojesus.util.Common;

import java.util.List;

import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_BUNDLE_CONTENT;
import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT;
import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT_LAST;
import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT_MP3;
import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT_NAME;
import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT_STATUS;
import static com.softjads.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT_TIME;
import static com.softjads.jorge.hellojesus.topic.fragmentTab.MusicFragment.EXTRA_CONTENT_NAME_NOT_SHOW_TRANSLATE;

public class ContentActivity extends AppCompatActivity  {

    private List<Content> mContents;
    private Bundle mBundle;
    private int mTime;
    private String mMp3;

    private String mName;
    private String mStatusSave;
    private String mStatus;

    private String mShowTranslete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        mBundle = getIntent().getBundleExtra(EXTRA_BUNDLE_CONTENT);
        mContents = (List<Content>) mBundle.getSerializable(EXTRA_CONTENT);
        mTime = (int) mBundle.getInt(EXTRA_CONTENT_TIME);
        mMp3 = (String) mBundle.getString(EXTRA_CONTENT_MP3);

        mName = (String) mBundle.getString(EXTRA_CONTENT_NAME);
        mStatusSave = (String) mBundle.getString(EXTRA_CONTENT_LAST);

        mStatus = (String) mBundle.getString(EXTRA_CONTENT_STATUS);

        mShowTranslete = (String) mBundle.getString(EXTRA_CONTENT_NAME_NOT_SHOW_TRANSLATE);


        if (null == savedInstanceState) {
            if (Common.isOnline(this)) {
                initFragment(ContentFragment.newInstance(mContents,mTime,mMp3,mStatusSave,mName,mStatus, mShowTranslete));
            }
        }

    }

    private void initFragment(Fragment productFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, productFragment);
        transaction.commit();
   }

    @Override
    protected void onDestroy() {
        this.finish();

        super.onDestroy();
    }
}
