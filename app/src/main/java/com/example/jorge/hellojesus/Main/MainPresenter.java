package com.example.jorge.hellojesus.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.jorge.hellojesus.data.local.WordsDataSource;
import com.example.jorge.hellojesus.data.local.WordsRepository;
import com.example.jorge.hellojesus.data.local.help.Help;
import com.example.jorge.hellojesus.data.onLine.main.MainServiceApi;
import com.example.jorge.hellojesus.data.onLine.main.model.ListMain;
import com.example.jorge.hellojesus.data.onLine.main.model.Main;
import com.example.jorge.hellojesus.data.onLine.topic.model.Content;
import com.example.jorge.hellojesus.helpApp.HelpAppActivity;
import com.example.jorge.hellojesus.util.Common;
import com.example.jorge.hellojesus.util.EspressoIdlingResource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 22/02/2018.
 */

public class MainPresenter implements MainContract.UserActionsListener {

    private final MainServiceApi mMainServiceApi;
    private final MainContract.View mMainContractView;
    private final WordsRepository mWordsRepository;

    public static int[] locationInScreen = new int[]{0,0};

    public MainPresenter(WordsRepository wordsRepository, MainServiceApi mMainServiceApi, MainContract.View mainContract_View) {
        this.mMainServiceApi = mMainServiceApi;
        this.mMainContractView = mainContract_View;
        mWordsRepository = checkNotNull(wordsRepository, "tasksRepository cannot be null");
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
    public void saveHelp(Help help) {
        mWordsRepository.saveHelp(help);
    }

    @Override
    public void openTopic(@NonNull Main main) {

    }

    @Override
    public void start() {
       // loadTasks(false);
    }

    @Override
    public void loadHelp(View root, Context context) {
        EspressoIdlingResource.increment();

        mWordsRepository.getHelp(new WordsDataSource.LoadHelpCallback() {

            @Override
            public void onHelpLoaded(List<Help> helpList, View root, Context context) {
                List<String> stringList = new ArrayList<String>();
                List<Integer> stringListX = new ArrayList<Integer>();
                List<Integer> stringListY = new ArrayList<Integer>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                // We filter the tasks based on the requestType
                int i = 0;
                for (Help help : helpList) {
                    int valueID = 0;
                    valueID = Common.getResourceString(help.getMkey());
                    if (valueID != 0) {
                        View vievHelp2 = (View) root.findViewById(valueID);
                        if (vievHelp2  != null) {
                            vievHelp2.getLocationOnScreen(locationInScreen);
                            stringList.add(help.getMkey());
                            stringListX.add(locationInScreen[0]);
                            stringListY.add(locationInScreen[1] - (vievHelp2.getHeight() / 2));
                        }
                    }
                    i++;
                }

                Intent intent = new Intent(context, HelpAppActivity.class);
                intent.putExtra("HELP_ID", (Serializable) stringList);
                intent.putExtra("HELP_X", (Serializable) stringListX);
                intent.putExtra("HELP_Y", (Serializable) stringListY);

                context.startActivity(intent);

/*                // The view may not be able to handle UI updates anymore
                if (!mTopicContractView.isActive()) {
                    return;
                }
                //if (showLoadingUI) {
                if (true) {
                    mTopicContractView.setLoadingIndicator(false);
                }

                processWords(arrayList);*/
            }


            @Override
            public void onDataNotAvailable() {
/*                // The view may not be able to handle UI updates anymore
                if (!mTopicContractView.isActive()) {
                    return;
                }
                mTopicContractView.showLoadingShoppingError();*/
            }

        },root,context);
    }
}
