package com.m1.wechatmessage.repo;

import com.m1.wechatmessage.domain.AppToken;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class AppTokenRepositoryImpl implements AppTokenRepository {
    private RedisTemplate<String,AppToken> redisTemplate;
    private HashOperations hashOperations;
    private String hashKey ="APP";
    public AppTokenRepositoryImpl(RedisTemplate<String,AppToken> redisTemplate){
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();

    }


    @Override
    public void addAppletToken(AppToken appToken) {
        hashOperations.put(hashKey, appToken, appToken);
    }

    @Override
    public void updateAppletToken(AppToken appToken) {
        hashOperations.put(hashKey, appToken.getAppletId() , appToken);
    }

    @Override
    public void deleteAppToken(String appletId) {
        hashOperations.delete(hashKey, appletId);
    }

    @Override
    public AppToken getAppTokenById(String appletId) {
        return  (AppToken) hashOperations.get(hashKey,appletId);
    }

    @Override
    public Map<String ,AppToken> getAllAppToken() {
        return hashOperations.entries(hashKey);
    }
}
