package com.qsj.qsjMain.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class PickupSerialUtils {
    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    public String generate(Long shopId) {
        String shopRedisKey = "shop_pickup_" + shopId.toString();

        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();
        if (Boolean.FALSE.equals(redisTemplate.hasKey(shopRedisKey))) {
            ops.set(shopRedisKey, 100);
        } else {
            Integer currCounter = ops.get(shopRedisKey);
            if (currCounter == null) {
                currCounter = 100;
            }
            ops.set(shopRedisKey, currCounter + 1);
        }
        Integer counter = ops.get(shopRedisKey);
        if (counter == null) {
            counter = 100;
        }
        counter = counter % 1000;
        return "A" + counter;
    }
}
