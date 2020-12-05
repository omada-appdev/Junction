package com.omada.junction.utils.taskhandler;

import android.os.Process;

import androidx.annotation.NonNull;

import java.util.concurrent.ThreadFactory;

public class PriorityThreadFactory implements ThreadFactory {

    private final int mThreadPriority;

    public PriorityThreadFactory(int threadPriority) {
        mThreadPriority = threadPriority;
    }

    @Override
    public Thread newThread(@NonNull final Runnable runnable) {
        Runnable wrapperRunnable = () -> {
            try {
                Process.setThreadPriority(mThreadPriority);
            } catch (Throwable t) {

            }
            runnable.run();
        };
        return new Thread(wrapperRunnable);
    }

}