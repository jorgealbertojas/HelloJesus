package com.example.jorge.hellojesus.data.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 11/04/2018.
 *  Repository for do access with the data base
 *  in this class have insert, update and delete function
 */

public class WordsRepository implements WordsDataSource {

    private static WordsRepository INSTANCE = null;

    private final WordsDataSource mWordsRemoteDataSource;

    private final WordsDataSource mWordsLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Word> mCachedWords;

    boolean mCacheIsDirty = false;

    private WordsRepository(@NonNull WordsDataSource wordsRemoteDataSource, @NonNull WordsDataSource wordsDataSource) {
        mWordsRemoteDataSource = checkNotNull(wordsRemoteDataSource);
        mWordsLocalDataSource = checkNotNull(wordsDataSource);
    }

    public static WordsRepository getInstance(WordsDataSource wordsRemoteDataSource,
                                              WordsDataSource wordsDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new WordsRepository(wordsRemoteDataSource, wordsDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }



    @Override
    public void getWords(@NonNull final LoadWordCallback callback) {
        checkNotNull(callback);

        // Query the local storage if available. If not, query the network.
        mWordsLocalDataSource.getWords(new LoadWordCallback() {

            @Override
            public void onWordLoaded(List<Word> wordList) {
                refreshCache(wordList);
                callback.onWordLoaded(new ArrayList<>(mCachedWords.values()));
            }

            @Override
            public void onDataNotAvailable() {
                getWordsFromRemoteDataSource(callback);
            }
        });

    }

    @Override
    public void getWord(@NonNull final String shoppingId, @NonNull final GetWordCallback callback) {
        checkNotNull(shoppingId);
        checkNotNull(callback);

        final Word cachedWord = getWordWithId(shoppingId);

        mWordsLocalDataSource.getWord(shoppingId, new GetWordCallback() {
            @Override
            public void onWordLoaded(Word purchase) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedWords == null) {
                    mCachedWords = new LinkedHashMap<>();
                }
                mCachedWords.put(purchase.getWord(), cachedWord);
                callback.onWordLoaded(cachedWord);
            }

            @Override
            public void onDataNotAvailable() {
                mWordsRemoteDataSource.getWord(shoppingId, new GetWordCallback() {
                    @Override
                    public void onWordLoaded(Word word) {
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedWords == null) {
                            mCachedWords = new LinkedHashMap<>();
                        }
                        mCachedWords.put(word.getWord(), word);
                        callback.onWordLoaded(word);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void saveWord(@NonNull Word word) {
        checkNotNull(word);
        mWordsRemoteDataSource.saveWord(word);
        mWordsLocalDataSource.saveWord(word);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedWords == null) {
            mCachedWords = new LinkedHashMap<>();
        }
        mCachedWords.put(word.getId(), word);
    }

    @Override
    public void activateWord(@NonNull String productId, String quantity) {

        if (!quantity.toString().equals("0")) {
            checkNotNull(productId);

            activateWord(getWordWithId(productId), quantity);

            Word word = getWordWithId(productId);

            mWordsLocalDataSource.activateWord(word.getWord(), quantity);
        }else{
            mWordsLocalDataSource.deleteWord(productId);
        }

    }



    @Override
    public void activateWord(@NonNull Word word, String quantity) {
        checkNotNull(word);
        mWordsRemoteDataSource.activateWord(word,quantity);
        mWordsLocalDataSource.activateWord(word, quantity);

        Word activeWord = new Word(word.getWord(), word.getType(),word.getCountTime(),word.getSourceName(),word.getStatusSaid(),word.getStatusWrite(), word.getId());

        // Do in memory cache update to keep the app UI up to date
        if (mCachedWords == null) {
            mCachedWords = new LinkedHashMap<>();
        }
        mCachedWords.put(word.getId(), activeWord);
    }


    @Override
    public void refreshWord(List<Word> wordList) {
        refreshCache(wordList);
        mCacheIsDirty = true;
    }



    @Override
    public void deleteAllWords() {
        mWordsRemoteDataSource.deleteAllWords();
        mWordsLocalDataSource.deleteAllWords();

        if (mCachedWords == null) {
            mCachedWords = new LinkedHashMap<>();
        }
        mCachedWords.clear();
    }



    @Override
    public void deleteWord(@NonNull String shoppingId) {
        mWordsRemoteDataSource.deleteWord(checkNotNull(shoppingId));
        mWordsLocalDataSource.deleteWord(checkNotNull(shoppingId));
        mCachedWords.remove(shoppingId);
    }

    @Override
    public void completeWord(@NonNull Word word, @NonNull String quantity) {
        checkNotNull(word);
        mWordsRemoteDataSource.completeWord(word,quantity);
        mWordsLocalDataSource.completeWord(word, quantity);

        Word completedWord = new Word(word.getWord(), word.getType(),word.getCountTime(),word.getSourceName(),word.getStatusSaid(),word.getStatusWrite(), word.getId());

        // Do in memory cache update to keep the app UI up to date
        if (mCachedWords == null) {
            mCachedWords = new LinkedHashMap<>();
        }
        mCachedWords.put(word.getId(), completedWord);
    }




    @Override
    public void completeWord(@NonNull String shoppingID) {
        checkNotNull(shoppingID);
        completeWord(shoppingID);
    }

    @Override
    public void showMessageEventLog(String message) {

    }

    @Override
    public void finalizeWords(String date) {
        mWordsRemoteDataSource.finalizeWords(date);
        mWordsLocalDataSource.finalizeWords(date);

        if (mCachedWords == null) {
            mCachedWords = new LinkedHashMap<>();
        }
        mCachedWords.clear();

    }


    private void getWordsFromRemoteDataSource(@NonNull final LoadWordCallback callback) {
        mWordsRemoteDataSource.getWords(new LoadWordCallback() {
            @Override
            public void onWordLoaded(List<Word> wordList) {
                refreshCache(wordList);
                refreshLocalDataSource(wordList);
                callback.onWordLoaded(new ArrayList<>(mCachedWords.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Word> wordList) {
        if (mCachedWords == null) {
            mCachedWords = new LinkedHashMap<>();
        }
        // For Test
        mCachedWords.clear();
        for (Word word : wordList) {
            mCachedWords.put(word.getId(), word);
        }
        mCacheIsDirty = false;
    }

    @Nullable
    private Word getWordWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedWords == null || mCachedWords.isEmpty()) {
            return null;
        } else {
            return mCachedWords.get(id);
        }
    }

    private void refreshLocalDataSource(List<Word> wordList) {
        // For Test
        mWordsLocalDataSource.deleteAllWords();
        for (Word word : wordList) {
            mWordsLocalDataSource.saveWord(word);
        }
    }


}
