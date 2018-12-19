package com.example.jorge.hellojesus.data.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.jorge.hellojesus.data.local.control.Control;
import com.example.jorge.hellojesus.data.local.helloWord.HelloWord;
import com.example.jorge.hellojesus.data.local.help.Help;

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
    Map<String, Help> mCachedHelp;
    Map<String, Control> mCachedControl;
    Map<String, HelloWord> mCachedHelloWords;

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
                refreshCacheWord(wordList);
                callback.onWordLoaded(new ArrayList<>(mCachedWords.values()));
            }

            @Override
            public void onDataNotAvailable() {
                getWordsFromRemoteDataSource(callback);
            }
        });

    }

    @Override
    public void getWordsWrong(@NonNull final LoadWordCallback callback) {
        checkNotNull(callback);

        // Query the local storage if available. If not, query the network.
        mWordsLocalDataSource.getWordsWrong(new LoadWordCallback() {

            @Override
            public void onWordLoaded(List<Word> wordList) {
                refreshCacheWord(wordList);
                callback.onWordLoaded(new ArrayList<>(mCachedWords.values()));
            }

            @Override
            public void onDataNotAvailable() {
                getWordsFromRemoteDataSource(callback);
            }
        });

    }

    @Override
    public void getHelp(@NonNull final LoadHelpCallback callback, final View root, final Context context) {
        checkNotNull(callback);

        // Query the local storage if available. If not, query the network.
        mWordsLocalDataSource.getHelp(new LoadHelpCallback() {

              @Override
            public void onHelpLoaded(List<Help> contentList, View root, Context context) {
                refreshCacheHelp(contentList);
                callback.onHelpLoaded(new ArrayList<>(mCachedHelp.values()),root,context);
            }

            @Override
            public void onDataNotAvailable() {
                getHelpFromRemoteDataSource(callback,root,context);
            }
        },root,context);
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
    public void getControlStatus1(@NonNull String key, @NonNull final GetControlCallback callback) {
        checkNotNull(key);
        checkNotNull(callback);

        mWordsLocalDataSource.getControlStatus1(key, new GetControlCallback() {

            @Override
            public void onControlLoaded(Control control) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedControl == null) {
                    mCachedControl = new LinkedHashMap<>();
                }
                callback.onControlLoaded(control);
            }

            @Override
            public void onDataNotAvailable() {

            }


        });
    }


    @Override
    public void saveControl(@NonNull Control control) {
        checkNotNull(control);
        mWordsRemoteDataSource.saveControl(control);
        mWordsLocalDataSource.saveControl(control);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedControl == null) {
            mCachedControl = new LinkedHashMap<>();
        }
        mCachedControl.put(control.getId(), control);
    }

    @Override
    public void saveHelloWord(@NonNull HelloWord helloWord) {
        checkNotNull(helloWord);
        mWordsRemoteDataSource.saveHelloWord(helloWord);
        mWordsLocalDataSource.saveHelloWord(helloWord);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedHelloWords == null) {
            mCachedHelloWords = new LinkedHashMap<>();
        }
        mCachedHelloWords.put(helloWord.getId(), helloWord);
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
    public void saveWordQuantity(@NonNull Word word) {
        checkNotNull(word);
        mWordsRemoteDataSource.saveWordQuantity(word);
        mWordsLocalDataSource.saveWordQuantity(word);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedWords == null) {
            mCachedWords = new LinkedHashMap<>();
        }
        mCachedWords.put(word.getId(), word);
    }

    @Override
    public void saveWordWrite(@NonNull Word word, @NonNull String quantity) {
        checkNotNull(word);
        mWordsRemoteDataSource.saveWordWrite(word,quantity);
        mWordsLocalDataSource.saveWordWrite(word,quantity);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedWords == null) {
            mCachedWords = new LinkedHashMap<>();
        }
        mCachedWords.put(word.getId(), word);
    }

    @Override
    public void saveWordSaid(@NonNull Word word, @NonNull String quantity) {
        checkNotNull(word);
        mWordsRemoteDataSource.saveWordSaid(word,quantity);
        mWordsLocalDataSource.saveWordSaid(word,quantity);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedWords == null) {
            mCachedWords = new LinkedHashMap<>();
        }
        mCachedWords.put(word.getId(), word);
    }


    @Override
    public void saveHelp(@NonNull Help help) {
        checkNotNull(help);
        mWordsRemoteDataSource.saveHelp(help);
        mWordsLocalDataSource.saveHelp(help);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedHelp == null) {
            mCachedHelp = new LinkedHashMap<>();
        }
        mCachedHelp.put(help.getId(), help);
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
        refreshCacheWord(wordList);
        mCacheIsDirty = true;
    }

    @Override
    public void updateControlStatus1(Control control, String status1) {
        checkNotNull(control);
        mWordsRemoteDataSource.updateControlStatus1(control,status1);
        mWordsLocalDataSource.updateControlStatus1(control,status1);

        Control activeControl = new Control(control.getMkey(), status1,control.getMstatus2(),control.getMstatus3(),control.getMstatus4());

        // Do in memory cache update to keep the app UI up to date
        if (mCachedControl == null) {
            mCachedControl = new LinkedHashMap<>();
        }
        mCachedControl.put(control.getId(), activeControl);
    }

    @Override
    public void updateControlStatus2(@NonNull Control control, String status2) {
        checkNotNull(control);
        mWordsRemoteDataSource.updateControlStatus2(control,status2);
        mWordsLocalDataSource.updateControlStatus2(control,status2);

        Control activeControl = new Control(control.getMkey(), control.getMstatus1(),control.getMstatus2(),control.getMstatus3(),control.getMstatus4());

        // Do in memory cache update to keep the app UI up to date
        if (mCachedControl == null) {
            mCachedControl = new LinkedHashMap<>();
        }
        mCachedControl.put(control.getId(), activeControl);
    }

    @Override
    public void updateControlStatus3(@NonNull Control control, String status3) {
        checkNotNull(control);
        mWordsRemoteDataSource.updateControlStatus3(control,status3);
        mWordsLocalDataSource.updateControlStatus3(control,status3);

        Control activeControl = new Control(control.getMkey(), control.getMstatus1(),control.getMstatus2(),control.getMstatus3(),control.getMstatus4());

        // Do in memory cache update to keep the app UI up to date
        if (mCachedControl == null) {
            mCachedControl = new LinkedHashMap<>();
        }
        mCachedControl.put(control.getId(), activeControl);
    }

    @Override
    public void updateControlStatus4(@NonNull Control control, String status4) {
        checkNotNull(control);

        mWordsRemoteDataSource.updateControlStatus4(control,status4);
        mWordsLocalDataSource.updateControlStatus4(control,status4);

        Control activeControl = new Control(control.getMkey(), control.getMstatus1(),control.getMstatus2(),control.getMstatus3(),control.getMstatus4());

        // Do in memory cache update to keep the app UI up to date
        if (mCachedControl == null) {
            mCachedControl = new LinkedHashMap<>();
        }
        mCachedControl.put(control.getId(), activeControl);

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
    public void deleteAllHelloWords() {
        mWordsRemoteDataSource.deleteAllWords();
        mWordsLocalDataSource.deleteAllWords();

        if (mCachedHelloWords == null) {
            mCachedHelloWords = new LinkedHashMap<>();
        }
        mCachedHelloWords.clear();
    }

    @Override
    public void deleteAllHelps() {
        mWordsRemoteDataSource.deleteAllHelps();
        mWordsLocalDataSource.deleteAllHelps();

        if (mCachedHelp == null) {
            mCachedHelp = new LinkedHashMap<>();
        }
        mCachedHelp.clear();
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

    private void getHelloWordsFromRemoteDataSource(@NonNull final LoadHelloWordCallback callback, final String tip1, final Context context) {
        mWordsRemoteDataSource.getHelloWord(new LoadHelloWordCallback() {

            @Override
            public void onHelloWordLoaded(List<HelloWord> helloWordList, String tip1, Context context) {
                refreshCacheHelloWord(helloWordList);
                refreshLocalDataSourceHelloWord(helloWordList);
                callback.onHelloWordLoaded(new ArrayList<>(mCachedHelloWords.values()),tip1,context);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        },tip1,context);
    }

    private void getWordsFromRemoteDataSource(@NonNull final LoadWordCallback callback) {
        mWordsRemoteDataSource.getWords(new LoadWordCallback() {
            @Override
            public void onWordLoaded(List<Word> wordList) {
                refreshCacheWord(wordList);
                refreshLocalDataSourceWord(wordList);
                callback.onWordLoaded(new ArrayList<>(mCachedWords.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getHelpFromRemoteDataSource(@NonNull final LoadHelpCallback callback, final View root, final Context context) {
        mWordsRemoteDataSource.getHelp(new LoadHelpCallback() {

            @Override
            public void onHelpLoaded(List<Help> contentList, View root, Context context) {
                refreshCacheHelp(contentList);
                refreshLocalDataSourceHelp(contentList);
                callback.onHelpLoaded(new ArrayList<>(mCachedHelp.values()),root,context);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        },root,context);
    }

    private void refreshCacheWord(List<Word> wordList) {
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

    private void refreshCacheHelp(List<Help> helpList) {
        if (mCachedWords == null) {
            mCachedWords = new LinkedHashMap<>();
        }
        // For Test
        mCachedWords.clear();
        if (mCachedHelp == null) {
            mCachedHelp = new LinkedHashMap<>();
        }
        for (Help help : helpList) {
            mCachedHelp.put(help.getId(), help);
        }
        mCacheIsDirty = false;
    }

    private void refreshCacheHelloWord(List<HelloWord> helloWordList) {
        if (mCachedHelloWords == null) {
            mCachedHelloWords = new LinkedHashMap<>();
        }
        // For Test
        mCachedHelloWords.clear();
        for (HelloWord helloWord : helloWordList) {
            mCachedHelloWords.put(helloWord.getId(), helloWord);
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

    private void refreshLocalDataSourceWord(List<Word> wordList) {
        // For Test
        mWordsLocalDataSource.deleteAllWords();
        for (Word word : wordList) {
            mWordsLocalDataSource.saveWord(word);
        }
    }

    private void refreshLocalDataSourceHelloWord(List<HelloWord> helloWordList) {
        // For Test
        mWordsLocalDataSource.deleteAllHelloWords();
        for (HelloWord HelloWord : helloWordList) {
            mWordsLocalDataSource.saveHelloWord(HelloWord);
        }
    }

    private void refreshLocalDataSourceHelp(List<Help> helpList) {
        // For Test
        mWordsLocalDataSource.deleteAllHelps();
        for (Help help : helpList) {
            mWordsLocalDataSource.saveHelp(help);
        }
    }


    public void getHelloWord(final LoadHelloWordCallback loadHelloWordCallback, final String tip1, final Context context) {
        checkNotNull(loadHelloWordCallback);

        // Query the local storage if available. If not, query the network.
        mWordsLocalDataSource.getHelloWord(new LoadHelloWordCallback() {

            @Override
            public void onHelloWordLoaded(List<HelloWord> contentList, String tip1, Context context) {
                refreshCacheHelloWord(contentList);
                loadHelloWordCallback.onHelloWordLoaded(new ArrayList<>(mCachedHelloWords.values()), tip1, context);
            }

            @Override

            public void onDataNotAvailable() {
                getHelloWordsFromRemoteDataSource(loadHelloWordCallback,tip1,context);
            }
        },tip1,context);
    }
}
