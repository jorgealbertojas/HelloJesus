package com.softjads.jorge.hellojesus.menuActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.softjads.jorge.hellojesus.R;
import com.softjads.jorge.hellojesus.topic.fragmentTab.QuestionFragment;

public class MenuWordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_word);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, QuestionFragment.newInstance());
        transaction.commit();
    }
}
