package com.softjads.jorge.hellojesus.ad;

import android.content.Context;
import android.support.annotation.NonNull;

import com.softjads.jorge.hellojesus.BasePresenter;
import com.softjads.jorge.hellojesus.BaseView;
import com.softjads.jorge.hellojesus.data.local.help.Help;
import com.softjads.jorge.hellojesus.data.onLine.ad.model.Ad;
import com.softjads.jorge.hellojesus.helpApp.HelpContract;

import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

public interface AdContract extends HelpContract {

    interface View extends BaseView<AdContract.UserActionsListener> {

        void showAd(List<Ad> adList);

    }

    interface UserActionsListener  extends BasePresenter {
        void loadingAd(ShimmerLayout shimmerText);

        void loadWordWrong(@NonNull final Context context);

        void loadWordCorrect(@NonNull final Context context);

        void saveHelp(Help help);

    }
}

