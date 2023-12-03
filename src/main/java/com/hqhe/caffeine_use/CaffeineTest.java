package com.hqhe.caffeine_use;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.hqhe.entity.User;

import java.time.Duration;
import java.util.UUID;

public class CaffeineTest {
    public static void main(String[] args) throws InterruptedException {
        Cache<Integer, User> cache = Caffeine.newBuilder()
                // 元素写入 10 秒后过期
                .expireAfterWrite(Duration.ofSeconds(10))
                .removalListener((Integer id, User value, RemovalCause cause) -> {
                    System.out.printf("Key %s was evicted (%s)%n", id, cause);
                })
                // 缓存的最大容量是 10000
                .maximumSize(10_000)
                .build();
        User hhq = new User(1, "hhq");


        int i = 0;

        while(true) {
            User user = new User(i ++ , UUID.randomUUID().toString());
            cache.put(user.getId(), user);
            Thread.sleep(1000);
        }
        //System.out.println(traceSegments.size());
    }

}
