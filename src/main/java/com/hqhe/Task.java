package com.hqhe;


import java.sql.Time;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Task implements Runnable{
    ScheduledExecutorService scheduledExecutorService;
    TimeCache timeCache;

    public Task() {
        timeCache = new TimeCache();
        scheduledExecutorService =  Executors.newSingleThreadScheduledExecutor();
    }

    public void in() {
        //timeCache.put(key, value);
    }

    public void start() {
        scheduledExecutorService.scheduleAtFixedRate(this, 0, 10, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        long deadLine = System.currentTimeMillis();
        List<TimeCache.TimeObject> timeObjects = timeCache.get(deadLine);
        timeObjects.stream().forEach(System.out::println);
    }
}
