package com.hqhe.caffeine_use;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.hqhe.entity.User;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CaffeineWriteStrategy {
    /**
     * 手动加载
     */
    public void insertByHand() {
        Cache<User, List<User>> cache = Caffeine.newBuilder()
                // 10 s expire
                .expireAfterWrite(Duration.ofSeconds(10))
                .removalListener((User key, List<User> value, RemovalCause cause) -> {
                    System.out.println(String.format("key:%s, value size: %d, cause: %s",key.getName(), value.size(), cause));
                })
                .maximumSize(10_000)
                .build();

        for(int i = 0; i < 10_000; i ++ ) {
            User user = new User(i, UUID.randomUUID().toString());
            // recommended
            // cache.get(key, k -> value) 操作来在缓存中不存在该key对应的缓存元素的时候进行计算生成并直接写入至缓存内，
            // 而当该key对应的缓存元素存在的时候将会直接返回存在的缓存值
            // Tip : 当缓存的元素无法生成或者在生成的过程中抛出异常而导致生成元素失败，cache.get 也许会返回 null
            List<User> users = cache.get(user, k -> k.createUserList());
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
     * 自动加载
     */
    public void insertByAuto() {
        LoadingCache<User, List<User>> cache = Caffeine.newBuilder()
                // 10 s expire
                .expireAfterWrite(Duration.ofSeconds(10))
                .removalListener((User key, List<User> value, RemovalCause cause) -> {
                    System.out.println(String.format("key:%s, value size: %d, cause: %s", key.getName(), value.size(), cause));
                })
                .maximumSize(10_000)
                .build(User::createUserList);

        for(int i = 0; i < 10_000; i ++ ) {
            User user = new User(i, UUID.randomUUID().toString());
            // recommended
            // cache.get(key, k -> value) 操作来在缓存中不存在该key对应的缓存元素的时候进行计算生成并直接写入至缓存内，
            // 而当该key对应的缓存元素存在的时候将会直接返回存在的缓存值
            // Tip : 当缓存的元素无法生成或者在生成的过程中抛出异常而导致生成元素失败，cache.get 也许会返回 null
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
    public static void main(String[] args) throws InterruptedException {
        new CaffeineWriteStrategy().insertByAuto();
    }
}
