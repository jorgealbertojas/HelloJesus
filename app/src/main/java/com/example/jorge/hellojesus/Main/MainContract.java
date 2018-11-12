package com.example.jorge.hellojesus.main;

import android.support.annotation.NonNull;

import com.example.jorge.hellojesus.BasePresenter;
import com.example.jorge.hellojesus.BaseView;
import com.example.jorge.hellojesus.data.local.help.Help;
import com.example.jorge.hellojesus.data.onLine.main.model.Main;
import com.example.jorge.hellojesus.helpApp.HelpContract;

import java.util.List;

/**
 * Created by jorge on 22/02/2018.
 */

public interface MainContract extends HelpContract {

    interface View extends BaseView<UserActionsListener> {

        void showMain(List<Main> mainList);

        void openViewTopic(@NonNull Main main);
    }

    interface UserActionsListener  extends BasePresenter {

        void loadingMain();

        void saveHelp(Help help);

        void openTopic(@NonNull Main main);




    }
}

