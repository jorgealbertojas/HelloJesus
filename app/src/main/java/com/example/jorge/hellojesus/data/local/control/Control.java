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
        @ColumnInfo(name = "status")
        @SerializedName("status")
        private final String mstatus;

        @Ignore
        public Control(@NonNull String mkey, @NonNull String mstatus  ) {
            this(UUID.randomUUID().toString(),mkey,mstatus);
        }

        public Control(@NonNull String mId, @NonNull String mkey, @NonNull String mstatus ) {
            this.mId = mId;
            this.mkey = mkey;
            this.mstatus = mstatus;
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
        public String getMstatus() {
            return mstatus;
        }
    }
