package com.couragehe.gmall.util;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @PackageName:com.couragehe.gmall.util
 * @ClassName:GmallRedissonConfig
 * @Description:Redisson配置文件，生成Bean
 * @Autor:CourageHe
 * @Date: 2020/1/6 13:14
 */
@Configuration
public class GmallRedissonConfig {
    //读取配置文件中的redis的ip地址
    @Value("${spring.redis.host:disabled}")
    private String host;
    @Value("${spring.redis.port:0}")
    private int port;

    @Bean
    public RedissonClient redissonClient() {
        if (host.equals("disabled")) {
            return null;
        }
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port);
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
