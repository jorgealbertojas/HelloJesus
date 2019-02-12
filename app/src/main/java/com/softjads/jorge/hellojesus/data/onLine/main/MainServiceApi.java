package com.softjads.jorge.hellojesus.data.onLine.main;

import com.softjads.jorge.hellojesus.data.onLine.main.model.ListMain;
import com.softjads.jorge.hellojesus.data.onLine.main.model.Main;

/**
 * Created by jorge on 22/02/2018.
 */

public interface MainServiceApi {

    interface MainServiceCallback<T> {

    void onLoaded(ListMain<Main> mainListMain);
}

    void getMain(MainServiceApi.MainServiceCallback<ListMain<Main>> callback);

}