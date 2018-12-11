package com.example.jorge.hellojesus.data.local;

/**
 * Created by jorge on 11/04/2018.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.jorge.hellojesus.data.local.control.Control;
import com.example.jorge.hellojesus.data.local.helloWord.HelloWord;
import com.example.jorge.hellojesus.data.local.help.Help;

import java.util.List;

/**
 * Created by jorge on 11/04/2018.
 *  * Main entry point for accessing Word data.
 * For simplicity, only getWord() and getWords() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new Purchase is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */

public interface WordsDataSource {

    // Table WORD
    interface LoadWordCallback {

        void onWordLoaded(List<Word> contentList);

        void onDataNotAvailable();

    }

    interface GetWordCallback {

        void onWordLoaded(Word word);

        void onDataNotAvailable();
    }

    interface GetControlCallback {

        void onControlLoaded(Control control);

        void onDataNotAvailable();
    }


    // Table HELP
    interface LoadHelpCallback {

        void onHelpLoaded(List<Help> contentList, View root, Context context);

        void onDataNotAvailable();

    }

    // Table HELP
    interface LoadHelloWordCallback {

        void onHelloWordLoaded(List<HelloWord> contentList, String tip1, Context context);

        void onDataNotAvailable();

    }


    void getWords(@NonNull LoadWordCallback callback);



    void getHelp(@NonNull LoadHelpCallback callback, View root, final Context context);

    void getWord(@NonNull String word , @NonNull GetWordCallback callback);

    void getHelloWord(@NonNull LoadHelloWordCallback callback, @NonNull String tip1, final Context context);

    void getControlStatus1(@NonNull String key , @NonNull GetControlCallback callback);

    void saveWord(@NonNull Word word);

    void saveHelp(@NonNull Help help);

    void saveControl(@NonNull Control control);

    void saveHelloWord(@NonNull HelloWord helloWord);

    void activateWord(@NonNull String productId, String Quantity);

    void activateWord(@NonNull Word word, String Quantity);

    void refreshWord(List<Word> wordList);

    void updateControlStatus1(@NonNull Control control, String status1);

    void updateControlStatus2(@NonNull Control control, String status2);

    void updateControlStatus3(@NonNull Control control, String status3);

    void updateControlStatus4(@NonNull Control control, String status4);

    void deleteAllWords();

    void deleteAllHelloWords();

    void deleteAllHelps();

    void deleteWord(@NonNull String word);

    void completeWord(@NonNull Word word, @NonNull String quantity);

    void completeWord(@NonNull String word);

    void showMessageEventLog(String message);

    void finalizeWords(String date);



}
