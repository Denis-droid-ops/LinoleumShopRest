package com.kuznetsov.linoleumShopRest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Configuration
@EnableCaching
public class CacheConfig {

    @Autowired
    private Environment env;

    public CacheConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public CacheManager cacheManager() {
        if(Arrays.stream(env.getActiveProfiles()).toList().contains("noCache")) {
            return new NoOpCacheManager();
        }
        final SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("readLinoleumDto")));
        return cacheManager;
    }

}
