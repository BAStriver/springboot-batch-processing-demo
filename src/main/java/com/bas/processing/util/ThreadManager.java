package com.bas.processing.util;

import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {
    private static volatile ThreadManager threadManager = null;

    public static volatile Vector<String> threadNames = null;

    private static final int CORE_POOL_SIZE;     // maintain min size
    private static final int MAX_POOL_SIZE;     // maintain max size
    private static final int KEEP_ALIVE_TIME = 0;    // idea time

    private Queue<Runnable> threadQueue;

    static {
        //MAX_POOL_SIZE = CORE_POOL_SIZE = Math.max(1, Runtime.getRuntime().availableProcessors() * 3);
        CORE_POOL_SIZE = 50;/*120*/
        MAX_POOL_SIZE = 50;/*120*/
        System.out.printf("max_pool_size:{}\n", MAX_POOL_SIZE);
        System.out.printf("core_pool_size:{}\n", CORE_POOL_SIZE);
    }


    // the thread handle task
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(30), new ThreadPoolExecutor.CallerRunsPolicy());

    private ThreadManager() {
        threadQueue = new LinkedBlockingQueue<Runnable>();
    }

    public synchronized static ThreadManager newInstance() {
        if (threadManager == null) {
            synchronized (ThreadManager.class) {
                if (threadManager == null) {
                    threadManager = new ThreadManager();
                    threadNames = new Vector<String>();
                }
            }
        }
        return threadManager;
    }

    public void addTask(Runnable r, String name) {
        threadNames.add(name);
        threadPool.execute(r);
        System.out.printf("-------------------threadNames:{}, activeThread Num : {}", getRunnningCount(), threadPool.getActiveCount());
    }

    public void addTask(Runnable r) {
        threadPool.execute(r);
    }

    public Boolean containsName(String name) {
        return threadNames.contains(name);
    }

    public void clearName(String name) {
        threadNames.remove(name);
    }

    public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    //provide a method to check thread executing
    public Boolean matchName(String regx) {
        if (threadNames.size() == 0) return Boolean.FALSE;
        for (String tName : threadNames) {
            if (tName.matches(regx)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Integer getRunnningCount() {
        return threadNames != null ? threadNames.size() : 0;
    }


    public Boolean startsWith(String name) {
        for (String tName : threadNames) {
            if (tName.startsWith(name)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
