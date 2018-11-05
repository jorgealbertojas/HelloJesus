package com.example.jorge.hellojesus.word;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.content.ContentFragment;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.util.Common;

import java.util.List;

import static com.example.jorge.hellojesus.word.WordFragment.EXTRA_BUNDLE_WORD;
import static com.example.jorge.hellojesus.word.WordFragment.EXTRA_WORD;


/**
 * Created by jorge on 21/04/2018.
 */

public class WordActivity extends AppCompatActivity

    {

        private List<String> mListString;
        private Bundle mBundle;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        mBundle = getIntent().getBundleExtra(EXTRA_BUNDLE_WORD);
        mListString = (List<String>) mBundle.getSerializable(EXTRA_WORD);

        if (null == savedInstanceState) {
            if (Common.isOnline(this)) {
                initFragment(WordFragment.newInstance(mListString));
            }
        }

    }

    private void initFragment(Fragment productFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.wordFrame, productFragment);
        transaction.commit();
    }

}
