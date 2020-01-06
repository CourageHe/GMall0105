package com.couragehe.gmall.manage;

import com.couragehe.gmall.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

@RunWith(SpringRunner.class)
@SpringBootTest
class GmallManageServiceApplicationTests {
    @Autowired
    RedisUtil redisUtil;


    @Test
    void contextLoads() {
//        RedisUtil redisUtil = new RedisUtil();
        Jedis jedis = redisUtil.getJedis();
        System.out.println(redisUtil);
    }

    public static Jedis cli_single(String host, int port) {
        try {
            return new Jedis(host, port);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String[] args) throws Exception {
//        Jedis jedis =cli_single("192.168.10.100",6379);
//        System.out.println(jedis);
////        RedisUtil redisUtil = new RedisUtil();
////        Jedis jedis = redisUtil.getJedis();
////        System.out.println(jedis);
//    }

}
