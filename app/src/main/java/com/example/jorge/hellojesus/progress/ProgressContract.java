package com.example.jorge.hellojesus.progress;

import com.example.jorge.hellojesus.BasePresenter;
import com.example.jorge.hellojesus.BaseView;

import java.util.List;

/**
 * Created by jorge on 11/04/2018.
 */

public interface ProgressContract {

    interface View extends BaseView<UserActionsListener> {

    }

    interface UserActionsListener extends BasePresenter {

        void saveWord(List<String> stringList, String type,  String sourceName, String countTime,  String statusSaid, String statusWrite);

    }
}
