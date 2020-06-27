package com.example.testone.Net;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskRunner {
    private static final String TAG = "TaskRunner";

    private static final int CORE_COUNT = Runtime.getRuntime().availableProcessors();

    private static ThreadPoolExecutor executor
            = new ThreadPoolExecutor(CORE_COUNT, CORE_COUNT, 10000,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    private TaskRunner(){
        throw new AssertionError("No com.jlu.chengjie.zhihu.util.TaskRunner instances for you!");
    }

    public static void execute(Runnable runnable) {
        try {
            executor.execute(runnable);
        } catch (Exception e) {
            Log.e(TAG, "execute runnable exception: "+" :"+ e);
        }
    }

}
