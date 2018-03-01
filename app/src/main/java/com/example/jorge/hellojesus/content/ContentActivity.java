package com.example.jorge.hellojesus.content;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.util.Common;

import java.util.List;

import static com.example.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_BUNDLE_CONTENT;
import static com.example.jorge.hellojesus.topic.fragmentTab.BibleFragment.EXTRA_CONTENT;

public class ContentActivity extends AppCompatActivity {

    private List<Content> mContents;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        mBundle = getIntent().getBundleExtra(EXTRA_BUNDLE_CONTENT);
        mContents = (List<Content>) mBundle.getSerializable(EXTRA_CONTENT);

        if (null == savedInstanceState) {
            if (Common.isOnline(this)) {
                initFragment(ContentFragment.newInstance(mContents));
            }
        }

    }

    private void initFragment(Fragment productFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, productFragment);
        transaction.commit();


    }
}
