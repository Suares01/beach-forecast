package com.beachforecast.api.beachforecastapi.infra.cache;

import javax.cache.CacheManager;
import javax.cache.Caching;

import org.redisson.config.Config;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.giffing.bucket4j.spring.boot.starter.config.cache.SyncCacheResolver;
import com.giffing.bucket4j.spring.boot.starter.config.cache.jcache.JCacheCacheResolver;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;

@Configuration
@EnableCaching
public class RedisConfiguration {
  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;

  @Value("${spring.data.redis.password}")
  private String password;

  @Value("${api.rate-limit.cache-name}")
  private String cacheName;

  @Bean
  public Config config() {
    Config config = new Config();
    config
        .useSingleServer()
        .setAddress("redis://" + host + ":" + port)
        .setPassword(password);
    return config;
  }

  @Bean(name = "RedissonCacheManager")
  public CacheManager cacheManager(Config config) {
    CacheManager manager = Caching.getCachingProvider().getCacheManager();
    manager.createCache(cacheName, RedissonConfiguration.fromConfig(config));
    return manager;
  }

  @Bean
  ProxyManager<String> proxyManager(@Qualifier("RedissonCacheManager") CacheManager cacheManager) {
    return new JCacheProxyManager<>(cacheManager.getCache(cacheName));
  }

  @Bean
  @Primary
  public SyncCacheResolver bucket4jCacheResolver(CacheManager cacheManager) {
    return new JCacheCacheResolver(cacheManager);
  }

  @Bean(name = "redisTemplate")
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);

    return template;
  }
}
