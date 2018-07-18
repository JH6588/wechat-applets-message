package com.m1.wechatmessage.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.m1.wechatmessage.domain.AppToken;
import com.m1.wechatmessage.repo.AppTokenRepository;
import com.m1.wechatmessage.utils.HttpRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AppTokenService {


    @Resource
    private AppTokenRepository appTokenRepository;
    private static final int ACCESS_TOKEN_EXPIRE_TIME = 60 * 90;
    static final String ERR_MARK = "err";

    public AppToken getAppTokenById(String appletId) {
        return appTokenRepository.getAppTokenById(appletId);
    }

    public Map<String, AppToken> getAllAppToken() {
        return appTokenRepository.getAllAppToken();
    }

    public void deleteAppToken(String appletId) {
        appTokenRepository.deleteAppToken(appletId);
    }

    //根据AppToken的情况来更新
    public AppToken processAppToken(@NotNull AppToken appToken) throws IOException {
        String appStatus = appToken.getStatus();
        if (("forzen").equals(appStatus.toLowerCase())) {
            // appTokenRepository.deleteAppToken(appToken);
            return null;
        }

        if (appToken.getToken() == null || appToken.getUpdateTime() == null ||
                System.currentTimeMillis() / 1000 - appToken.getUpdateTime() / 1000 > ACCESS_TOKEN_EXPIRE_TIME) {
            Map<String, String> tokenMap = getAccessToken(appToken.getAppid(), appToken.getSecret());
            if (tokenMap.get("errcode") != null) {
                appToken.setStatus(ERR_MARK + tokenMap.get("errcode"));
            }
            appToken.setToken(tokenMap.get("access_token"));
            appToken.setUpdateTime(System.currentTimeMillis());
            appTokenRepository.updateAppletToken(appToken);

        }
        System.out.println("NEW APPTOKEN");
        return appToken;
    }


    //获取最新的AccessToken
    private static Map<String, String> getAccessToken(String appid, String secret) throws IOException {


        String atUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credent" +
                "ial&appid=%s&secret=%s", appid, secret);
        String response = HttpRequest.get(atUrl).body();
        System.out.println("TOKEN RESPONSE : " + response);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> tokenMap = new HashMap<>();

        try {
            tokenMap = mapper.readValue(response, new TypeReference<HashMap<String, String>>() {
            });
        } catch (IOException e) {
            /**
             * io错误装载到tokenMap
             */
            tokenMap.put("errcode", ERR_MARK + "ioerror");
        }
        System.out.println("RESMAP ---------" + tokenMap);
        return tokenMap;
        // return "1_iqtWqlPifwZJBNzAIyXP9SD8Z74wj0VXI283h7jluni3tMNLwIB-ieVAfrr8ISq4qmmUcgDaHXoeGJq5fYBHWfDORDZ6KziytdvGJ1V7Sdr4bU7kkU_lX8IN-WIXs0gtLTwmcZhBQVE1xOWIIJZiAEAZAQ";
    }


}
