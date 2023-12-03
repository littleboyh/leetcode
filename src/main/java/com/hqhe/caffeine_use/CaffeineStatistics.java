package com.hqhe.caffeine_use;


import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.hqhe.Task;
import com.hqhe.entity.User;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CaffeineStatistics {
    public void getRecordStats() {

        LoadingCache<User, List<User>> cache = Caffeine.newBuilder()
                .removalListener((User key, List<User> value, RemovalCause cause) -> {
                    // System.out.println(String.format("key:%s, value size: %d, cause: %s", key.getId(), value.size(), cause));
                })
                // 在access后的 5s 后没有再次被access 会被删除
                .expireAfterAccess(Duration.ofSeconds(5))
                .maximumSize(10_000)
                .recordStats()
                .build(User::createUserList);
        User user0 = null;
        for(int i = 0; i < 1000; i ++ ) {

            User user = new User(i, UUID.randomUUID().toString());
            if(i == 0) user0 = user;
            List<User> users = cache.get(user);
            if(users == null)
                cache.put(user, new ArrayList<>());
            else users.add(user);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            CacheStats stats = cache.stats();
            System.out.println("缓存的命中率:" + stats.hitRate());
            System.out.println("新值被载入的平均耗时:" + stats.averageLoadPenalty());
            System.out.println("被驱逐的缓存数量:" + stats.evictionCount());
        }
    }

    public static void main(String[] args) {
        new CaffeineStatistics().getRecordStats();
    }
}
