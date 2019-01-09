package com.example.jorge.hellojesus.tipWord;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.util.Common;

import java.util.List;

public class TipWordActivity extends AppCompatActivity {

    private int[] locationInScreen = new int[]{0,0};

    private String EXTRA_X = "EXTRA_X";
    private String EXTRA_Y = "EXTRA_Y";
    private String EXTRA_WIDTH = "EXTRA_WIDTH";
    private String EXTRA_HELLO_WORD = "EXTRA_HELLO_WORD";
    private String EXTRA_POSITION_LIST = "EXTRA_POSITION_LIST";

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
        List<String> stringList = (List<String>) getIntent().getExtras().getSerializable(EXTRA_HELLO_WORD);
        int position = (int) getIntent().getExtras().getInt(EXTRA_POSITION_LIST);

        if (null == savedInstanceState) {
            if (Common.isOnline(this)) {
                initFragment(TipWordFragment.newInstance(locationInScreen[0],locationInScreen[1],widht,stringList,position));
            }
        }

    }

    private void initFragment(Fragment productFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.tipWordFrame, productFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }





}
