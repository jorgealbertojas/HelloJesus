package com.softjads.jorge.hellojesus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.softjads.jorge.hellojesus.data.local.Word;
import com.softjads.jorge.hellojesus.data.local.WordsDataSource;
import com.softjads.jorge.hellojesus.data.local.control.Control;
import com.softjads.jorge.hellojesus.data.local.helloWord.HelloWord;
import com.softjads.jorge.hellojesus.data.local.help.Help;

import java.util.List;

/**
 * Created by jorge on 13/04/2018.
 */

public class FakeWorksRemoteLocalDataSource implements WordsDataSource {

    private static FakeWorksRemoteLocalDataSource INSTANCE;

    // Prevent direct instantiation.
    private FakeWorksRemoteLocalDataSource() {}

    public static FakeWorksRemoteLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeWorksRemoteLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getWords(@NonNull LoadWordCallback callback) {

    }

    @Override
    public void getWordsWrong(@NonNull LoadWordCallback callback) {

    }

    @Override
    public void getWordsCorrect(@NonNull LoadWordCallback callback) {

    }

    @Override
    public void getHelp(@NonNull LoadHelpCallback callback, @NonNull View root, @NonNull Context context) {

    }


    @Override
    public void getWord(@NonNull String word, @NonNull GetWordCallback callback) {

    }

    @Override
    public void getHelloWord(@NonNull LoadHelloWordCallback callback, @NonNull String tip1, Context context) {

    }


    @Override
    public void getControlStatus1(@NonNull String key, @NonNull GetControlCallback callback) {

    }

    @Override
    public void saveWord(@NonNull Word word) {

    }

    @Override
    public void saveWordQuantity(@NonNull Word word) {

    }

    @Override
    public void saveWordWrite(@NonNull Word word, @NonNull String quantity) {

    }

    @Override
    public void saveWordSaid(@NonNull Word word, @NonNull String quantity) {

    }

    @Override
    public void saveHelp(@NonNull Help help) {

    }

    @Override
    public void saveControl(@NonNull Control control) {

    }

    @Override
    public void saveHelloWord(@NonNull HelloWord helloWord) {

    }

    @Override
    public void activateWord(@NonNull String productId, String Quantity) {

    }

    @Override
    public void activateWord(@NonNull Word word, String Quantity) {

    }

    @Override
    public void refreshWord(List<Word> wordList) {

    }

    @Override
    public void updateControlStatus1(@NonNull Control control, String status1) {

    }

    @Override
    public void updateControlStatus2(@NonNull Control control, String status2) {

    }

    @Override
    public void updateControlStatus3(@NonNull Control control, String status3) {

    }

    @Override
    public void updateControlStatus4(@NonNull Control control, String status4) {

    }

    @Override
    public void deleteAllWords() {

    }

    @Override
    public void deleteAllHelloWords() {

    }

    @Override
    public void deleteAllHelps() {

    }

    @Override
    public void deleteWord(@NonNull String word) {

    }

    @Override
    public void completeWord(@NonNull Word word, @NonNull String quantity) {

    }

    @Override
    public void completeWord(@NonNull String word) {

    }

    @Override
    public void showMessageEventLog(String message) {

    }

    @Override
    public void finalizeWords(String date) {

    }
}
