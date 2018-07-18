package com.m1.wechatmessage.service;

import com.m1.wechatmessage.domain.AppToken;
import com.m1.wechatmessage.utils.HttpRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.IOException;

import static com.m1.wechatmessage.utils.HttpRequest.CONTENT_TYPE_JSON;
import static com.m1.wechatmessage.utils.HttpRequest.HEADER_CONTENT_TYPE;

@Service
public class MsgTask {
    @Resource
    private AppTokenService appTokenService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Async
    public void sendMsg(@NotNull AppToken appToken, String openId) throws IOException {

        String msg = getMsgContent(appToken, openId);
        AppToken updateAppToken = appTokenService.processAppToken(appToken);
        if (appToken.getStatus().startsWith(AppTokenService.ERR_MARK)) {
            return;
        }

        String body = HttpRequest.post("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
                + updateAppToken.getToken()).header(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON).send(msg).body();


        System.out.println("发送消息:" + body);
    }

    private String getMsgContent(@NotNull AppToken appToken, String openId) {
        String msg;
        if ("success".equals(appToken.getStatus().toLowerCase())) {
            msg = redisTemplate.opsForValue().get("checkedMsg");
        } else {
            msg = redisTemplate.opsForValue().get("uncheckedMsg");
        }

        return msg.replaceFirst("OPENID", openId);
    }
}
