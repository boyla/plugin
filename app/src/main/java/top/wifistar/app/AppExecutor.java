package top.wifistar.app;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by boyla on 2019/9/7.
 */

public final class AppExecutor {

    private static int NUMBER_OF_CORES;
    // unlimit
    private static ExecutorService workService;
    // single thread
    private static ExecutorService backgroundService;
    // main handler
    private Handler handler;

    private AppExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();
        NUMBER_OF_CORES = cores > 1 ? cores : 1;
        workService = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES + 1,
                3L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
        backgroundService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    private static class InstanceHolder {
        private static final AppExecutor instance = new AppExecutor();
    }

    public static AppExecutor getInstance() {
        return InstanceHolder.instance;
    }

    public void postBackground(Runnable runnable) {
        backgroundService.submit(runnable);
    }

    public void postWork(Runnable runnable) {
        workService.submit(runnable);
    }

    public void postMain(Runnable runnable) {
        postMain(runnable, 0);
    }

    public void postMain(Runnable runnable, long delayMills) {
        if (handler != null) {
            handler.postDelayed(runnable, delayMills);
        }
    }
}
