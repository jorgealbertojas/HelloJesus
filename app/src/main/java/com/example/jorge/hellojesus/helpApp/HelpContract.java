package com.example.jorge.hellojesus.helpApp;

import com.example.jorge.hellojesus.BasePresenter;
import com.example.jorge.hellojesus.BaseView;
import com.example.jorge.hellojesus.data.local.help.Help;


import java.util.List;

public interface HelpContract {

    interface View extends BaseView<HelpContract.UserActionsListener> {

    }

    interface UserActionsListener extends BasePresenter {


    }
}