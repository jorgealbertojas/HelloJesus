package com.example.jorge.hellojesus.tipWord;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.local.helloWord.HelloWord;
import com.example.jorge.hellojesus.util.Common;

import java.io.Serializable;

public class TipWordActivity extends AppCompatActivity {

    private int[] locationInScreen = new int[]{0,0};

    private String EXTRA_X = "EXTRA_X";
    private String EXTRA_Y = "EXTRA_Y";
    private String EXTRA_WIDTH = "EXTRA_WIDTH";
    private String EXTRA_HELLO_WORD = "EXTRA_HELLO_WORD";

    private static View mContainer;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_word);

        locationInScreen[0] = getIntent().getExtras().getInt(EXTRA_X);
        locationInScreen[1] = getIntent().getExtras().getInt(EXTRA_Y);
        int widht = (int) getIntent().getExtras().getInt(EXTRA_WIDTH);
        HelloWord helloWord = (HelloWord) getIntent().getExtras().getParcelable(EXTRA_HELLO_WORD);

        if (null == savedInstanceState) {
            if (Common.isOnline(this)) {
                initFragment(TipWordFragment.newInstance(locationInScreen[0],locationInScreen[1],widht,helloWord));
            }
        }

    }

    private void initFragment(Fragment productFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.tipWordFrame, productFragment);
        transaction.commit();
    }





}
