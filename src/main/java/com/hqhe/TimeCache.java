package com.hqhe;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TimeCache {
    private ConcurrentHashMap<String, TimeObject> keyToTimeObject;
    private PriorityBlockingQueue<TimeObject> priorityQueue;

    public TimeCache() {
        this.keyToTimeObject = new ConcurrentHashMap<>();
        this.priorityQueue = new PriorityBlockingQueue<>();
    }

    public void put(String key, TimeObject value) {
        if (keyToTimeObject.contains(key)) keyToTimeObject.put(key, value);
        else {
            priorityQueue.add(value);
            keyToTimeObject.put(key, value);
        }
    }

    public List<TimeObject> get(Long deadLine) {
        List<TimeObject> resList = new ArrayList<>();
        while(priorityQueue.size() > 0 && priorityQueue.peek().timeBucket <= deadLine) {
            TimeObject poll = priorityQueue.poll();
            keyToTimeObject.remove(poll.key);
            resList.add(priorityQueue.poll());
        }
        return resList;
    }

    /**
     * 实现一个缓存，存放的对象有一个时间桶，每一个给定一个deadLine 从cache中移除
     * 小于deadLine的缓存对象
     */
    public static class TimeObject implements Comparable<TimeObject> {
        private Long timeBucket;
        private String key;
        private String value;

        public TimeObject(Long timeBucket, String key, String value) {
            this.timeBucket = timeBucket;
            this.key = key;
            this.value = value;
        }

        public Long getTimeBucket() {
            return timeBucket;
        }

        public void setTimeBucket(Long timeBucket) {
            this.timeBucket = timeBucket;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int compareTo(TimeObject o) {
            return Long.compare(o.timeBucket, timeBucket);
        }

        @Override
        public String toString() {
            return "TimeObject{" +
                    "timeBucket=" + timeBucket +
                    ", key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }


    public static void main(String[] args) {
        Task task = new Task();
        task.start();
        while(true) {
            long currentTimeMillis = System.currentTimeMillis();
            TimeObject timeObject = new TimeObject(currentTimeMillis, String.valueOf(currentTimeMillis), String.valueOf(currentTimeMillis));

        }
    }
}
