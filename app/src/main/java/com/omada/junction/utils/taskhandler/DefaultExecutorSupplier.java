package com.omada.junction.utils.taskhandler;

import android.os.Process;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//to use this get any excecutor and post or submit a runnable to it

public class DefaultExecutorSupplier {

    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private final ThreadPoolExecutor backgroundTaskExecutor;
    private final Executor mMainThreadExecutor;

    private static DefaultExecutorSupplier sInstance;


    public static DefaultExecutorSupplier getInstance() {
        if (sInstance == null) {
            synchronized (DefaultExecutorSupplier.class) {
                sInstance = new DefaultExecutorSupplier();
            }
        }
        return sInstance;
    }

    private DefaultExecutorSupplier() {

            ThreadFactory backgroundPriorityThreadFactory = new
                    PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND);

            backgroundTaskExecutor = new ThreadPoolExecutor(
                    NUMBER_OF_CORES * 2,
                    NUMBER_OF_CORES * 2,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(),
                    backgroundPriorityThreadFactory
            );

            mMainThreadExecutor = new MainThreadExecutor();
        }

        public ThreadPoolExecutor getBackgroundTaskExecutor(){
            return backgroundTaskExecutor;
        }

        public Executor getMainThreadExecutor() {
            return mMainThreadExecutor;
        }
}
