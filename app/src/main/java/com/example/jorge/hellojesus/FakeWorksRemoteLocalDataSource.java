package com.example.jorge.hellojesus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.jorge.hellojesus.data.local.Word;
import com.example.jorge.hellojesus.data.local.WordsDataSource;
import com.example.jorge.hellojesus.data.local.help.Help;

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
    public void getHelp(@NonNull LoadHelpCallback callback, @NonNull View root, @NonNull Context context) {

    }


    @Override
    public void getWord(@NonNull String word, @NonNull GetWordCallback callback) {

    }

    @Override
    public void saveWord(@NonNull Word word) {

    }

    @Override
    public void saveHelp(@NonNull Help help) {

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
    public void deleteAllWords() {

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
