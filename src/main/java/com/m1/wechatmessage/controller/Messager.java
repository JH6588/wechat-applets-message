package com.m1.wechatmessage.controller;


import com.m1.wechatmessage.domain.AppToken;
import com.m1.wechatmessage.service.AppTokenService;
import com.m1.wechatmessage.service.MsgTask;
import com.m1.wechatmessage.utils.SHA1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class Messager {

    @Resource
    private AppTokenService appTokenService;
    @Resource
    private MsgTask msgTask;

    @PostMapping("/a{app}/messager")
    public String receivedMsg(@RequestBody HashMap<String, String> msg, @RequestParam HashMap<String, String> requestParams,
                              @PathVariable String app
            , HttpServletRequest request) {
        System.out.println("RECEIVED  --- " + new Date() + msg);
        System.out.println("URL 参数 --- " + requestParams);
        String openId = msg.get("FromUserName");

        System.out.println("OPENID -- APP --- " + openId + app);
        AppToken appToken = appTokenService.getAppTokenById(app);
        try {
            if (appToken != null) {
                msgTask.sendMsg(appToken, openId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "success";
    }

    @GetMapping("token/update/a{app}")
    public AppToken updateToken(@PathVariable String app) throws IOException {

        return appTokenService.processAppToken(appTokenService.getAppTokenById(app));
    }

    @GetMapping("/token/update/all")
    public Map<String, AppToken> updateAllToken() throws IOException {
        Map<String, AppToken> allToken = appTokenService.getAllAppToken();
        for (String key : allToken.keySet()) {
            allToken.put(key, appTokenService.processAppToken(allToken.get(key)));

        }
        return allToken;
    }

    @GetMapping("/token/a{app}")
    public AppToken getAppStatus(@PathVariable String app, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        return appTokenService.getAppTokenById(app);

    }

    @GetMapping("/token/all")
    public Map<String, AppToken> getAllToken() {
        return appTokenService.getAllAppToken();
    }

    @PostMapping("/token/delete/a{app}")
    public void deleteAppToken(@PathVariable String app) {
        appTokenService.deleteAppToken(app);
    }

    @PostMapping("/hello")
    public String sayHello(@RequestBody HashMap<String, String> res, HttpServletRequest request) {
        System.out.println(res);
        System.out.println(request.getContentType());
        return "HELLO WORLD";
    }

    @ExceptionHandler(value = Exception.class)
    public String msgExceptionHandle(HttpServletResponse response ,Exception exception) {
        exception.printStackTrace();
        return "You Get a Exception";
    }



}
