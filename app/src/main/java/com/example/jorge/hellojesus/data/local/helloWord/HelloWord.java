package com.example.jorge.hellojesus.data.local.helloWord;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

@Entity(tableName = "HelloWord")
public class HelloWord implements Parcelable{

    @PrimaryKey
    @NonNull
    @SerializedName("entryid")
    @ColumnInfo(name = "entryid")
    private final String mId;


    @NonNull
    @ColumnInfo(name = "wordName")
    @SerializedName("wordName")
    private final String mwordName;

    @NonNull
    @ColumnInfo(name = "tip1")
    @SerializedName("tip1")
    private final String mtip1;

    @NonNull
    @ColumnInfo(name = "tip2")
    @SerializedName("tip2")
    private final String mtip2;

    @NonNull
    @ColumnInfo(name = "tip3")
    @SerializedName("tip3")
    private final String mtip3;


    @Ignore
    public HelloWord(@NonNull String mwordName, @NonNull String mtip1, @NonNull String mtip2, @NonNull String mtip3) {
        this(UUID.randomUUID().toString(), mwordName, mtip1, mtip2, mtip3);
    }

    public HelloWord(@NonNull String mId, @NonNull String mwordName, @NonNull String mtip1, @NonNull String mtip2, @NonNull String mtip3) {
        this.mId = mId;
        this.mwordName = mwordName;
        this.mtip1 = mtip1;
        this.mtip2 = mtip2;
        this.mtip3 = mtip3;
    }

    protected HelloWord(Parcel in) {
        mId = in.readString();
        mwordName = in.readString();
        mtip1 = in.readString();
        mtip2 = in.readString();
        mtip3 = in.readString();
    }

    public static final Creator<HelloWord> CREATOR = new Creator<HelloWord>() {
        @Override
        public HelloWord createFromParcel(Parcel in) {
            return new HelloWord(in);
        }

        @Override
        public HelloWord[] newArray(int size) {
            return new HelloWord[size];
        }
    };

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getMwordName() {
        return mwordName;
    }

    @NonNull
    public String getMtip1() {
        return mtip1;
    }

    @NonNull
    public String getMtip2() {
        return mtip2;
    }

    @NonNull
    public String getMtip3() {
        return mtip3;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mwordName);
        dest.writeString(mtip1);
        dest.writeString(mtip2);
        dest.writeString(mtip3);
    }
}
