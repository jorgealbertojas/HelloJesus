package com.example.jorge.hellojesus.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RemoteViews;

import com.example.jorge.hellojesus.Injection;
import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.data.local.Word;
import com.example.jorge.hellojesus.data.local.WordsDataSource;
import com.example.jorge.hellojesus.data.local.WordsRepository;
import com.example.jorge.hellojesus.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyWidgetProvider extends AppWidgetProvider {

    private static Context mContext;
    private static AppWidgetManager mAppWidgetManager;
    private static int[] mAppWidgetIds;

    private static WordsRepository mWordsRepository;

    private static RemoteViews remoteViews;

    private static String word = "Jesus";
    private static List<String> arrayList;

    public final String WIDGET_WORD = "WIDGET_WORD";
    public final String WIDGET_OPCAO = "WIDGET_OPCAO";

    private static final String SYNC_CLICKED1    = "automaticWidgetSyncButtonClick1";
    private static final String SYNC_CLICKED2    = "automaticWidgetSyncButtonClick2";
    private static final String SYNC_CLICKED3    = "automaticWidgetSyncButtonClick3";
    private static final String SYNC_CLICKED4    = "automaticWidgetSyncButtonClick4";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        /* Once all of our views are setup, we can load the weather data. */
        mContext = context;
        mAppWidgetManager = appWidgetManager;
        mAppWidgetIds = appWidgetIds;
        remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);


        mWordsRepository = Injection.provideWordsRepository(context.getApplicationContext());
        loadWord();



    }

    private void loadWord() {

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment();

        mWordsRepository.getWordsWrong(new WordsDataSource.LoadWordCallback() {

            @Override
            public void onWordLoaded(List<Word> wordList) {
               arrayList = new ArrayList<String>();

                int i = 0;
                for (Word word : wordList) {
                       arrayList.add(i,word.getWord());
                    i++;
                }

                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }


                putDataWidget(mContext, mAppWidgetManager, mAppWidgetIds, wordList);

            }

            @Override
            public void onDataNotAvailable() {

            }

        });
    }




    private void putDataWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, List<Word> listWord) {
        // Get all ids
        if (listWord.size() != 0) {
            if (context != null && appWidgetManager != null) {
                ComponentName thisWidget = new ComponentName(context,
                        MyWidgetProvider.class);
                int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);


                int x = 0;


                for (int widgetId : allWidgetIds) {
                    // create some random data

                    x = (new Random().nextInt(listWord.size() - 1));
                    word = listWord.get(x).getWord();
                    // Set the text
                    remoteViews.setTextViewText(R.id.update, (Integer.toString(listWord.size())));

                    remoteViews.setTextViewText(R.id.empty_view, (word));

                    // Register an onClickListener
                    Intent intent = new Intent(context, MyWidgetProvider.class);


                    registeOnClick(context, intent, appWidgetIds);

                    appWidgetManager.updateAppWidget(widgetId, remoteViews);
                }
            }
        }
    }


    public void registeOnClick(Context context, Intent intent, int[] appWidgetIds){
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.empty_view, pendingIntent);

        remoteViews.setOnClickPendingIntent(R.id.button_enter1, getPendingSelfIntent(context, SYNC_CLICKED1));
        remoteViews.setOnClickPendingIntent(R.id.button_enter2, getPendingSelfIntent(context, SYNC_CLICKED2));
        remoteViews.setOnClickPendingIntent(R.id.button_enter3, getPendingSelfIntent(context, SYNC_CLICKED3));
        remoteViews.setOnClickPendingIntent(R.id.button_ok, getPendingSelfIntent(context, SYNC_CLICKED4));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);

        if (SYNC_CLICKED1.equals(intent.getAction())) {
            loadWord2(context,1);
        }else if (SYNC_CLICKED2.equals(intent.getAction())) {
            loadWord2(context,2);
        }else if (SYNC_CLICKED3.equals(intent.getAction())) {
                loadWord2(context,3);
        }else if (SYNC_CLICKED4.equals(intent.getAction())) {
            registeOnClick(context,intent,mAppWidgetIds);
            Word wordnew = new Word(word, "", "", "", "","");
            mWordsRepository = Injection.provideWordsRepository(context.getApplicationContext());
            mWordsRepository.saveWordSaid(wordnew, "1");
            RotateAnimation rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(1000);
            rotate.setInterpolator(new LinearInterpolator());

            onUpdate(context,mAppWidgetManager,mAppWidgetIds);

        }
    }


    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }


    private void loadWord2(final Context context, int opcao) {
        Bundle bundle = new Bundle();
        bundle.putString(WIDGET_WORD, word);
        bundle.putInt(WIDGET_OPCAO, opcao);
        context.startActivity(new Intent(context, WidgetActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtras(bundle));
    }

}