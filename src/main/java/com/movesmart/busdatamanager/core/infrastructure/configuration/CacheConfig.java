package com.movesmart.busdatamanager.core.infrastructure.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import lombok.Generated;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Generated
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("stop", "route", "stopsbyroute", "vehicle");
        cacheManager.setCaffeine(
                Caffeine.newBuilder().expireAfterWrite(6, TimeUnit.HOURS).maximumSize(1000));
        return cacheManager;
    }
}
