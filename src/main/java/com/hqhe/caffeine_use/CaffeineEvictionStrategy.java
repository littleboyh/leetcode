package com.hqhe.caffeine_use;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.hqhe.Task;
import com.hqhe.entity.User;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Caffeine 提供了三种驱逐策略，分别是基于容量，基于时间和基于引用三种类型
 */
public class CaffeineEvictionStrategy {
    /**
     * 基于容量
     */
    public void evictionByCapacity() {
        LoadingCache<User, List<User>> cache = Caffeine.newBuilder()
                .removalListener((User key, List<User> value, RemovalCause cause) -> {
                    System.out.println(String.format("key:%s, value size: %d, cause: %s", key.getName(), value.size(), cause));
                })
                // 当缓存容量超过 maxCapacity 缓存会通过基于 LRU 和 LFU 的算法 evicted 某个元素
                .maximumSize(10)
                .build(User::createUserList);

        for(int i = 0; i < 1000; i ++ ) {
            User user = new User(i, UUID.randomUUID().toString());
            List<User> users = cache.get(user);
            if(users == null)
                cache.put(user, new ArrayList<>());
            else users.add(user);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 基于时间
     */
    public void evictionByAbsoluteTimeAfterWrite() {
        LoadingCache<User, List<User>> cache = Caffeine.newBuilder()
                .removalListener((User key, List<User> value, RemovalCause cause) -> {
                    System.out.println(String.format("key:%s, value size: %d, cause: %s", key.getName(), value.size(), cause));
                })
                // 在写入后的 5s 后被expire
                .expireAfterWrite(Duration.ofSeconds(5))
                .maximumSize(10_000)
                .build(User::createUserList);
        for(int i = 0; i < 1000; i ++ ) {
            User user = new User(i, UUID.randomUUID().toString());
            List<User> users = cache.get(user);
            if(users == null)
                cache.put(user, new ArrayList<>());
            else users.add(user);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void evictionByAbsoluteTimeAfterAccess() {
        LoadingCache<User, List<User>> cache = Caffeine.newBuilder()
                .removalListener((User key, List<User> value, RemovalCause cause) -> {
                    System.out.println(String.format("key:%s, value size: %d, cause: %s", key.getId(), value.size(), cause));
                })
                // 在access后的 5s 后没有再次被access 会被删除
                .expireAfterAccess(Duration.ofSeconds(5))
                .maximumSize(10_000)
                .build(User::createUserList);
        User user0 = null;
        for(int i = 0; i < 1000; i ++ ) {

            User user = new User(i, UUID.randomUUID().toString());
            if(i == 0) user0 = user;
            List<User> users = cache.get(user);
            if(users == null)
                cache.put(user, new ArrayList<>());
            else users.add(user);
            cache.get(user0);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void evictionAfterAccess() {
        Task task = new Task();
        LoadingCache<User, List<User>> cache = Caffeine.newBuilder()
                .removalListener((User key, List<User> value, RemovalCause cause) -> {
                    task.in();
                    System.out.println(String.format("key:%s, value size: %d, cause: %s", key.getId(), value.size(), cause));
                })
                // 在access后的 5s 后没有再次被access 会被删除
                .expireAfterAccess(Duration.ofSeconds(5))
                .maximumSize(10_000)
                .build(User::createUserList);
        User user0 = null;
        for(int i = 0; i < 1000; i ++ ) {

            User user = new User(i, UUID.randomUUID().toString());
            if(i == 0) user0 = user;
            List<User> users = cache.get(user);
            if(users == null)
                cache.put(user, new ArrayList<>());
            else users.add(user);
            cache.get(user0);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        new CaffeineEvictionStrategy().evictionByAbsoluteTimeAfterAccess();
    }
}
