package com.example.jorge.hellojesus.data.local;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import java.util.UUID;

/**
 * Created by jorge on 11/04/2018.
 * Immutable model class for a control Word.
 */

@Entity(tableName = "Word")
public final class Word {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private final String mId;


    @NonNull
    @ColumnInfo(name = "word")
    private final String mWord;

    @NonNull
    @ColumnInfo(name = "type")
    private final String mType;

    @Nullable
    @ColumnInfo(name = "counttime")
    private final String mCountTime;

    @Nullable
    @ColumnInfo(name = "sourcename")
    private final String mSourceName;

    @Nullable
    @ColumnInfo(name = "statussaid")
    private final String mStatusSaid;

    @Nullable
    @ColumnInfo(name = "statuswrite")
    private final String mStatusWrite;



    @Ignore
    public Word(@Nullable String word, @Nullable String  type, @Nullable String countTime, @Nullable String sourceName, @Nullable String statusSaid, @Nullable String statusWrite) {
        this(word, type, countTime, sourceName, statusSaid, statusWrite, UUID.randomUUID().toString());
    }

    public Word(@Nullable String word,  @Nullable String  type, @Nullable String countTime, @Nullable String sourceName, @Nullable String statusSaid, @Nullable String statusWrite,
                    @NonNull String id) {
        mId = id;
        mWord = word;
        mType = type;
        mCountTime = countTime;
        mSourceName = sourceName;
        mStatusSaid = statusSaid;
        mStatusWrite = statusWrite;
    }





    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getWord() {
        return mWord;
    }

    @NonNull
    public String getType() {
        return mType;
    }

    @Nullable
    public String getCountTime() {
        return mCountTime;
    }

    @Nullable
    public String getSourceName() {
        return mSourceName;
    }

    @Nullable
    public String getStatusSaid() {
        return mStatusSaid;
    }

    @Nullable
    public String getStatusWrite() {
        return mStatusWrite;
    }



    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mId);
    }

    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(mWord)) {
            return mWord;
        } else {
            return mId;
        }
    }

    public boolean isCompleted() {
        return (mWord == null);
    }





}
