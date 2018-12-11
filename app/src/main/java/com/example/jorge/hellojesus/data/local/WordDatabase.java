package com.example.jorge.hellojesus.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.jorge.hellojesus.data.local.control.Control;
import com.example.jorge.hellojesus.data.local.helloWord.HelloWord;
import com.example.jorge.hellojesus.data.local.help.Help;

/**
 * Created by jorge on 11/04/2018.
 * Get Instance SQLLite
 */

@Database(entities = {Word.class,Help.class, Control.class, HelloWord.class}, version = 1)
public abstract class WordDatabase  extends RoomDatabase {

    private static WordDatabase INSTANCE;

    public abstract WordDao wordDao();

    private static final Object sLock = new Object();

    public static WordDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        WordDatabase.class, "Word.db")
                        .build();
            }
            return INSTANCE;
        }
    }

}
