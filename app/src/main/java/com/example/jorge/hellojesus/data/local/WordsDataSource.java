package com.example.jorge.hellojesus.data.local;

/**
 * Created by jorge on 11/04/2018.
 */

import android.support.annotation.NonNull;

import com.example.jorge.hellojesus.data.onLine.topic.model.Content;

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
    interface LoadWordCallback {

        void onWordLoaded(List<Word> contentList);

        void onDataNotAvailable();

    }

    interface GetWordCallback {

        void onWordLoaded(Word word);

        void onDataNotAvailable();
    }

    void getWords(@NonNull LoadWordCallback callback);

    void getWord(@NonNull String word , @NonNull GetWordCallback callback);

    void saveWord(@NonNull Word word);

    void activateWord(@NonNull String productId, String Quantity);

    void activateWord(@NonNull Word word, String Quantity);

    void refreshWord(List<Word> wordList);

    void deleteAllWords();

    void deleteWord(@NonNull String word);

    void completeWord(@NonNull Word word, @NonNull String quantity);

    void completeWord(@NonNull String word);

    void showMessageEventLog(String message);

    void finalizeWords(String date);



}
