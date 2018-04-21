package com.example.jorge.hellojesus;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.jorge.hellojesus.data.local.WordDatabase;
import com.example.jorge.hellojesus.data.local.WordsDataSource;
import com.example.jorge.hellojesus.data.local.WordsLocalDataSource;
import com.example.jorge.hellojesus.data.local.WordsRepository;
import com.example.jorge.hellojesus.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

    /**
     ATENÇÃO ESTES PARAMETROS SÓ SÃO USUADOS NOS TESTE, QUANDO IMPLEMENTADOS NO SISTEMA
     * Created by jorge on 19/03/2018.
     * This function is make for the test with android
     * Have objective create data Fake in this Class for Test All the system
     * Jorge Alberto in 23/03/2018
     */

    public class Injection {
        public static WordsRepository provideWordsRepository(@NonNull Context context) {
            checkNotNull(context);
            WordDatabase database = WordDatabase.getInstance(context);

            return WordsRepository.getInstance(FakeWorksRemoteLocalDataSource.getInstance(),
                    WordsLocalDataSource.getInstance(new AppExecutors(),
                            database.wordDao(),context));
        }
    }
