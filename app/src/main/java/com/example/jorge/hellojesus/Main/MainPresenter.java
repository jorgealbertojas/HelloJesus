package com.example.jorge.hellojesus.main;

import android.support.annotation.NonNull;

import com.example.jorge.hellojesus.data.onLine.main.MainServiceApi;
import com.example.jorge.hellojesus.data.onLine.main.model.ListMain;
import com.example.jorge.hellojesus.data.onLine.main.model.Main;

/**
 * Created by jorge on 22/02/2018.
 */

public class MainPresenter implements MainContract.UserActionsListener {

    private final MainServiceApi mMainServiceApi;
    private final MainContract.View mMainContractView;

    public MainPresenter(MainServiceApi mMainServiceApi, MainContract.View mainContract_View) {
        this.mMainServiceApi = mMainServiceApi;
        this.mMainContractView = mainContract_View;
    }

    @Override
    public void loadingMain() {

        mMainServiceApi.getMain(new MainServiceApi.MainServiceCallback<ListMain<Main>>() {
            @Override
            public void onLoaded(ListMain<Main> mainListMain) {
                    mMainContractView.showMain(mainListMain.items);
            }


        });

    }

    @Override
    public void openTopic(@NonNull Main main) {

    }

    @Override
    public void start() {
       // loadTasks(false);
    }
}
