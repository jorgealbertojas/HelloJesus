package com.example.jorge.hellojesus;

import android.content.Context;
import android.view.View;

/**
 * Created by jorge on 22/02/2018.
 */

public interface BasePresenter {
    void start();

    void loadHelp(View root, Context context);
}

