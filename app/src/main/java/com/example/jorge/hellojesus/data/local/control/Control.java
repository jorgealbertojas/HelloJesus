package com.example.jorge.hellojesus.data.local.control;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

    @Entity(tableName = "Control")
    public class Control {


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
        @ColumnInfo(name = "status1")
        @SerializedName("status1")
        private final String mstatus1;

        @NonNull
        @ColumnInfo(name = "status2")
        @SerializedName("status2")
        private final String mstatus2;

        @NonNull
        @ColumnInfo(name = "status3")
        @SerializedName("status3")
        private final String mstatus3;

        @NonNull
        @ColumnInfo(name = "status4")
        @SerializedName("status4")
        private final String mstatus4;

        @Ignore
        public Control(@NonNull String mkey, @NonNull String mstatus1, @NonNull String mstatus2, @NonNull String mstatus3, @NonNull String mstatus4  ) {
            this(UUID.randomUUID().toString(),mkey,mstatus1,mstatus2, mstatus3,mstatus4);
        }

        public Control(@NonNull String mId, @NonNull String mkey, @NonNull String mstatus1, @NonNull String mstatus2, @NonNull String mstatus3, @NonNull String mstatus4 ) {
            this.mId = mId;
            this.mkey = mkey;
            this.mstatus1 = mstatus1;
            this.mstatus2 = mstatus2;
            this.mstatus3 = mstatus3;
            this.mstatus4 = mstatus4;
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
        public String getMstatus1() {
            return mstatus1;
        }

        @NonNull
        public String getMstatus2() {
            return mstatus2;
        }

        @NonNull
        public String getMstatus3() {
            return mstatus3;
        }

        @NonNull
        public String getMstatus4() {
            return mstatus4;
        }
    }
