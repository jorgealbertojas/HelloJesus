package com.softjads.jorge.hellojesus.word;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.softjads.jorge.hellojesus.BasePresenter;
import com.softjads.jorge.hellojesus.BaseView;
import com.softjads.jorge.hellojesus.data.onLine.topic.model.Content;

import java.util.List;



/**
 * Created by jorge on 21/04/2018.
 * Contract for support View amd Listener User of the Word
 */

public class WordContract{

    interface View extends BaseView<WordContract.UserActionsListener> {

        void showWord(List<String> stringList);

        void showAllWord();

        void showWords(List<Content> contentList);

        void ShowInformationWord();


        void showLoadingWordError();

        boolean isActive();

        void setLoadingIndicator(boolean active);
    }

    interface UserActionsListener  extends BasePresenter {

        void saveWordQuantity(String wordName ,String type,  String sourceName, String countTime,  String statusSaid, String statusWrite);

        void saveWordWrite(String wordName ,String type,  String sourceName, String countTime,  String statusSaid, String statusWrite);

        void saveWordSaid(String wordName ,String type,  String sourceName, String countTime,  String statusSaid, String statusWrite);

        void loadingWords(List<String> stringList);

        void loadingWord(String stringList);

        void ShowFabButton(FloatingActionButton floatingActionButton, Animation animation, Button button);

        void HideFabButton(FloatingActionButton floatingActionButton, Animation animation, Button button);

        Boolean isActiveFabButton(FloatingActionButton floatingActionButton);

        void openBrowserImage(Context context, TextView word);

        void openBrowserExplanation(Context context, TextView word);

        void openBrowserTranslate(Context context, TextView word);

        void openDetail();

    }
}
