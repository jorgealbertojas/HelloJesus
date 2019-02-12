package com.softjads.jorge.hellojesus.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.softjads.jorge.hellojesus.BasePresenter;
import com.softjads.jorge.hellojesus.BaseView;
import com.softjads.jorge.hellojesus.data.local.help.Help;
import com.softjads.jorge.hellojesus.data.onLine.main.model.Main;
import com.softjads.jorge.hellojesus.helpApp.HelpContract;

import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

/**
 * Created by jorge on 22/02/2018.
 */

public interface MainContract extends HelpContract {

    interface View extends BaseView<UserActionsListener> {

        void showMain(List<Main> mainList);

        void openViewTopic(@NonNull Main main);
    }

    interface UserActionsListener  extends BasePresenter {

        void loadWordWrong(@NonNull final Context context);

        void loadWordCorrect(@NonNull final Context context);

        void loadingMain(ShimmerLayout shimmerText);

        void saveHelp(Help help);

        void openTopic(@NonNull Main main);

        void getControlStatus1(@NonNull String key, ImageView imageView, Context context, String status);

        void getControlStatusAll(@NonNull String key, ImageView imageView, Context context);




    }
}

