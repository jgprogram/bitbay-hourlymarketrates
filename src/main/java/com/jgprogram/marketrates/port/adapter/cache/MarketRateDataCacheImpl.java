package com.jgprogram.marketrates.port.adapter.cache;

import com.jgprogram.marketrates.bitbay.MarketRateDataLoaded;
import com.jgprogram.marketrates.cache.MarketRateDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class MarketRateDataCacheImpl implements MarketRateDataCache {
    private RedisTemplate<String, Object> redisTemplate;

    private HashOperations hashOperations;

    @Autowired
    public MarketRateDataCacheImpl(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void add(MarketRateDataLoaded marketRateDataLoaded) {
        hashOperations.put(marketRateDataLoaded.requestId(), marketRateDataLoaded.date(), marketRateDataLoaded);
        redisTemplate.expire(marketRateDataLoaded.requestId(), 1, TimeUnit.DAYS);
    }

    @Override
    public List<MarketRateDataLoaded> findByRequestId(String requestId) {
        return hashOperations.values(requestId);
    }

    @Override
    public void removeByRequestId(String requestId) {
        redisTemplate.delete(requestId);
    }
}
