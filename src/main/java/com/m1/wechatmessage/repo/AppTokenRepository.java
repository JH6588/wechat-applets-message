package com.m1.wechatmessage.repo;


import com.m1.wechatmessage.domain.AppToken;

import java.util.Map;

public interface AppTokenRepository {
    void addAppletToken(AppToken appToken);
    void updateAppletToken(AppToken appToken);
    void deleteAppToken(String appletId);
    AppToken getAppTokenById(String appletId);
    Map<String ,AppToken> getAllAppToken();
}

