package com.couragehe.gmall.search;

import com.alibaba.dubbo.config.annotation.Reference;
import com.couragehe.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;


@SpringBootTest
class GmallSearchServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
