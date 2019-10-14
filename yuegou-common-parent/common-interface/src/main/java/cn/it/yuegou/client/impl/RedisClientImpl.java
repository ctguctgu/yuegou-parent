package cn.it.yuegou.client.impl;

import cn.it.yuegou.client.RedisClient;

public class RedisClientImpl implements RedisClient {
    @Override
    public String get(String key) {
        return "{\"message\":\"服务器异常\"！}";
    }

    @Override
    public void set(String key, String value) {

    }
}
