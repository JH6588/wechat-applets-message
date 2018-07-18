package com.m1.wechatmessage.controller;

import com.m1.wechatmessage.utils.SHA1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
接口签名验证
 */
@Component
public class SignatureFilter implements Filter {
    @Autowired
    public Environment env;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String reqPath = request.getServletPath();
            //目前只对messager接口进行验证
            if (reqPath.matches("/a\\d+?/messager$")) {
                String token = env.getProperty("wechat.token");
                String signature = request.getParameter("signature");
                String timestamp = request.getParameter("timestamp");
                String nonce = request.getParameter("nonce");

                try {
                    String sha1Result = SHA1.getString2SHA1(token, timestamp, nonce);
                    if (sha1Result.equals(signature)) {
                        if ("get".equals(request.getMethod().toLowerCase())) {
                            String echostr = request.getParameter("echostr");
                            response.getWriter().write(echostr);
                            return;
                        }
                    } else {
                        response.getWriter().write("false");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.getWriter().println
                            ("<h1>BAD REQUEST</h1>");
                    return;
                }


            }
            filterChain.doFilter(servletRequest, servletResponse);

        }
    }

    @Override
    public void destroy() {

    }
}
