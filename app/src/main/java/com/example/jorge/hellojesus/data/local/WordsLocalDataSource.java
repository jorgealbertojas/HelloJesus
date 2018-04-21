package com.example.jorge.hellojesus.data.local;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.widget.Toast;

import com.example.jorge.hellojesus.R;
import com.example.jorge.hellojesus.util.AppExecutors;

import java.util.LinkedHashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static com.example.jorge.hellojesus.util.ConstTagKey.TAG_COMPLETE;
import static com.example.jorge.hellojesus.util.ConstTagKey.TAG_ERROR;
import static com.example.jorge.hellojesus.util.ConstTagKey.TAG_NORMAL;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 13/04/2018.
 */

public class WordsLocalDataSource implements WordsDataSource {

    private static volatile WordsLocalDataSource INSTANCE;

    private WordDao mWordsDao;

    private AppExecutors mAppExecutors;

    private Context mContext;


    // Prevent direct instantiation.
    private WordsLocalDataSource(@NonNull AppExecutors appExecutors,
                                    @NonNull WordDao shoppingDao, Context context) {
        mAppExecutors = appExecutors;
        mWordsDao = shoppingDao;
        mContext = context;
    }

    public static WordsLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                      @NonNull WordDao shoppingDao, Context context) {
        if (INSTANCE == null) {
            synchronized (WordsLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WordsLocalDataSource(appExecutors, shoppingDao, context);
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public void getWords(@NonNull final LoadWordCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Word> wordList = mWordsDao.getWord();

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (wordList != null) {
                            callback.onWordLoaded(wordList);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);

    }

    @Override
    public void getWord(@NonNull final String word, @NonNull final GetWordCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Word purchase = mWordsDao.getWordByName(word);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (purchase != null) {


                            callback.onWordLoaded(purchase);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveWord(@NonNull final Word word) {
        checkNotNull(word);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                Word wordNew = mWordsDao.getWordByCarId(word.getWord());
                if (wordNew == null) {
                    observeInsertWord(word).subscribe(subscriberInsert);
                }else{

                    observeUpdate(word.getWord()).subscribe(subscriberUpdate);

                }
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);

    }

    @Override
    public void activateWord(@NonNull String productId, String Quantity) {

    }


    @Override
    public void activateWord(@NonNull Word purchase, String Quantity) {

    }

    @Override
    public void refreshWord(List<Word> wordList) {

    }


    @Override
    public void deleteAllWords() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mWordsDao.deleteAllWords();

            }
        };
        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteWord(@NonNull final String word) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mWordsDao.deleteWordByName(word);
                observeDeleteWord(word).subscribe(subscriberDelete);
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }


    @Override
    public void completeWord(@NonNull final Word purchase,@NonNull final String quantity) {
  /*      Runnable completeRunnable = new Runnable() {
            @Override
            public void run() {
                mWordsDao.updateQuantity(purchase.getId(),purchase.getQuantity());
            }
        };

        mAppExecutors.diskIO().execute(completeRunnable);*/
    }

    @Override
    public void completeWord(@NonNull String productId) {

    }

    @Override
    public void showMessageEventLog(final String message){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public void finalizeWords(final String date) {
/*        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mWordsDao.updateFinalizeWords(date);
                observeFinalizeWords(date).subscribe(subscriberFinalize);
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);*/
    }

    public Observable<Integer> observeFinalizeWords(@NonNull String date){
        return Observable.just(callObserveFinalizeWords(date));
    }

    public Observable<Integer> observeUpdate(@NonNull String word ){
        return Observable.just(callObserveUpdate(word));
    }

    public Observable<Long> observeInsertWord(@NonNull Word purchase){
        return Observable.just(callObserveInsertWord(purchase));
    }

    public Observable<Integer> observeDeleteWord(@NonNull String shoppingId){
        return Observable.just(callObserveDeleteWord(shoppingId));
    }

    private int callObserveFinalizeWords(@NonNull String date){
     //  int i = mWordsDao.updateFinalizeWords(date);
     //   subscriberUpdate.onNext(i);
     //   subscriberUpdate.onCompleted();
     //   return i;

        return 0;
    }

    private int callObserveUpdate(@NonNull String word){
     //   int i = mWordsDao.updateQuantity(shoppingId, quantity);
     //   subscriberUpdate.onNext(i);
     //   subscriberUpdate.onCompleted();
     //   return i;

        return 0;
    }

    private long callObserveInsertWord(@NonNull Word purchase){
        long statusCommit = mWordsDao.insertWord(purchase);
        subscriberInsert.onNext(statusCommit);
        subscriberInsert.onCompleted();
        return statusCommit;
    }

    private int callObserveDeleteWord(@NonNull String shoppingId){
      //  int statusCommit = mWordsDao.deleteWordById(shoppingId);
     //   subscriberDelete.onNext(statusCommit);
     //   subscriberDelete.onCompleted();
     //   return statusCommit;

        return 0;
    }

    Subscriber<Integer> subscriberUpdate = new Subscriber<Integer>(){
        @Override
        public void onCompleted() {
            Log.e(TAG_COMPLETE, mContext.getResources().getString(R.string.msg_success_return_update));
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG_ERROR, mContext.getResources().getString(R.string.msg_error_return));
            showMessageEventLog(mContext.getResources().getString(R.string.msg_error_return));
        }

        @Override
        public void onNext(Integer names) {
            Log.e(TAG_NORMAL,mContext.getResources().getString(R.string.msg_next_return));
            if (names > 0){
                showMessageEventLog(mContext.getResources().getString(R.string.msg_success_return_update));
            }
        }
    };

    Subscriber<Long> subscriberInsert = new Subscriber<Long>(){
        @Override
        public void onCompleted() {
            Log.e(TAG_COMPLETE, mContext.getResources().getString(R.string.msg_success_return_insert));

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG_ERROR, mContext.getResources().getString(R.string.msg_error_return));
            showMessageEventLog(mContext.getResources().getString(R.string.msg_error_return));
        }

        @Override
        public void onNext(Long names) {
            Log.e(TAG_NORMAL,mContext.getResources().getString(R.string.msg_next_return));
            if (names > 0){
                showMessageEventLog(mContext.getResources().getString(R.string.msg_success_return_insert));
            }
        }
    };

    Subscriber<Integer> subscriberDelete = new Subscriber<Integer>(){
        @Override
        public void onCompleted() {
            Log.e(TAG_COMPLETE, mContext.getResources().getString(R.string.msg_success_return_delete));

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG_ERROR, mContext.getResources().getString(R.string.msg_error_return));
            showMessageEventLog(mContext.getResources().getString(R.string.msg_error_return));
        }

        @Override
        public void onNext(Integer names) {
            Log.e(TAG_NORMAL,mContext.getResources().getString(R.string.msg_next_return));
            if (names == 0){
                showMessageEventLog(mContext.getResources().getString(R.string.msg_success_return_delete));
            }
        }
    };

    Subscriber<Integer> subscriberFinalize = new Subscriber<Integer>(){
        @Override
        public void onCompleted() {
            Log.e(TAG_COMPLETE, mContext.getResources().getString(R.string.msg_success_return_update));
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG_ERROR, mContext.getResources().getString(R.string.msg_error_return));
            showMessageEventLog(mContext.getResources().getString(R.string.msg_error_return));
        }

        @Override
        public void onNext(Integer names) {
            Log.e(TAG_NORMAL,mContext.getResources().getString(R.string.msg_next_return));
            if (names > 0){
                showMessageEventLog(mContext.getResources().getString(R.string.msg_success_return_update));
            }
        }
    };


    @VisibleForTesting
    static void clearInstance() {
        INSTANCE = null;
    }
}

