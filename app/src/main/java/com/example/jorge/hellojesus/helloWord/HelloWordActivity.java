package com.example.jorge.hellojesus.helloWord;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.local.helloWord.HelloWord;
import com.example.jorge.hellojesus.util.Common;
import com.example.jorge.hellojesus.word.WordFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.jorge.hellojesus.menu_cycle.CycleActivity.EXTRA_BUNDLE_HELLO_WORD;
import static com.example.jorge.hellojesus.menu_cycle.CycleActivity.EXTRA_HELLO_WORD;
import static com.example.jorge.hellojesus.menu_cycle.CycleActivity.EXTRA_HELLO_WORD_TIP;
import static com.example.jorge.hellojesus.word.WordFragment.EXTRA_BUNDLE_WORD;
import static com.example.jorge.hellojesus.word.WordFragment.EXTRA_WORD;

public class HelloWordActivity extends AppCompatActivity {

    private ArrayList<HelloWord> mListString;
    private Bundle mBundle;
    private String mTip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_word);

        mBundle = getIntent().getBundleExtra(EXTRA_BUNDLE_HELLO_WORD);
        mListString = mBundle.getParcelableArrayList(EXTRA_HELLO_WORD);
        mTip = mBundle.getString(EXTRA_HELLO_WORD_TIP);



        if (null == savedInstanceState) {
            if (Common.isOnline(this)) {
                initFragment(HelloWordFragment.newInstance(mListString,mTip));
            }
        }

    }

    private void initFragment(Fragment productFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.helloWordFrame, productFragment);
        transaction.commit();
    }


}
