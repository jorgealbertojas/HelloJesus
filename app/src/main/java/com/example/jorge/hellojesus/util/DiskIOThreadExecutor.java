package com.example.jorge.hellojesus.util;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by jorge on 13/04/2018.
 * Disk for Thread Executor
 */

public class DiskIOThreadExecutor implements Executor {
    private final Executor mDiskIO;

    public DiskIOThreadExecutor() {
        mDiskIO = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mDiskIO.execute(command);
    }
}
