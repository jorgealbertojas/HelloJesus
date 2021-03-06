package com.softjads.jorge.hellojesus.word;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.softjads.jorge.hellojesus.R;
import com.softjads.jorge.hellojesus.util.Common;

import java.util.List;

import static com.softjads.jorge.hellojesus.word.WordFragment.EXTRA_BUNDLE_WORD;
import static com.softjads.jorge.hellojesus.word.WordFragment.EXTRA_WORD;
import static com.softjads.jorge.hellojesus.word.WordFragment.EXTRA_WORD_CHECK;


/**
 * Created by jorge on 21/04/2018.
 */

public class WordActivity extends AppCompatActivity

    {

        private List<String> mListString;
        private String opcao;
        private Bundle mBundle;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        mBundle = getIntent().getBundleExtra(EXTRA_BUNDLE_WORD);
        mListString = (List<String>) mBundle.getSerializable(EXTRA_WORD);
        opcao = mBundle.getString(EXTRA_WORD_CHECK);
        if (opcao == null){
            opcao = "0";
        }

        if (null == savedInstanceState) {
            if (Common.isOnline(this)) {
                initFragment(WordFragment.newInstance(mListString,opcao));
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
