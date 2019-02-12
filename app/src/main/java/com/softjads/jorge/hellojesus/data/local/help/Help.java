package com.softjads.jorge.hellojesus.data.local.help;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;


@Entity(tableName = "Help")
public final class Help {


    @PrimaryKey
    @NonNull
    @SerializedName("entryid")
    @ColumnInfo(name = "entryid")
    private final String mId;


    @NonNull
    @ColumnInfo(name = "key")
    @SerializedName("key")
    private final String mkey;

    @NonNull
    @ColumnInfo(name = "value")
    @SerializedName("value")
    private final String mvalue;

    @NonNull
    @ColumnInfo(name = "last")
    @SerializedName("last")
    private final String mlast;


    @Ignore
    public Help(@NonNull String mkey, @NonNull String mvalue, @NonNull String mlast  ) {
        this(UUID.randomUUID().toString(),mkey,mvalue, mlast);
    }

    public Help(@NonNull String mId, @NonNull String mkey, @NonNull String mvalue, @NonNull String mlast ) {
        this.mId = mId;
        this.mkey = mkey;
        this.mvalue = mvalue;
        this.mlast = mlast;

    }



    @NonNull
    public String getId() {
        return mId;
    }


    @NonNull
    public String getMkey() {
        return mkey;
    }

    @NonNull
    public String getMvalue() {
        return mvalue;
    }

    @NonNull
    public String getHelp() {
        return mkey;
    }

    @NonNull
    public String getMlast() {
        return mlast;
    }





}