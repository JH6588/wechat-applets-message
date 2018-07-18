package com.m1.wechatmessage;


import com.m1.wechatmessage.utils.SHA1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;


//@RunWith(SpringRunner.class)
//@SpringBootTest(classes={WechatMessageApplication.class})
public class WechatMessageApplicationTests {

    @Test
    public void contextLoads() throws IOException, NoSuchAlgorithmException {

        String cont ="{\n" +
                "    \"touser\":\"OPENID\",\n" +
                "    \"msgtype\":\"text\",\n" +
                "    \"text\":\n" +
                "    {\n" +
                "         \"content\":\"Hello World\"\n" +
                "    }\n" +
                "}";

    }

}
