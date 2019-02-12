package com.softjads.jorge.hellojesus.helpApp;

import com.softjads.jorge.hellojesus.BasePresenter;
import com.softjads.jorge.hellojesus.BaseView;

public interface HelpContract {

    interface View extends BaseView<HelpContract.UserActionsListener> {

    }

    interface UserActionsListener extends BasePresenter {


    }
}