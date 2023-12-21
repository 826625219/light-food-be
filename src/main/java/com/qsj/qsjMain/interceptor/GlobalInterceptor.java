package com.qsj.qsjMain.interceptor;

import com.qsj.qsjMain.service.impl.UserServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Component
public class GlobalInterceptor implements HandlerInterceptor {

    private final UserServiceImpl userService;

    public GlobalInterceptor(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String xPrivCredential = request.getHeader("x-priv-credential");

        if ("146b64fe49e705a7da31f2a44ef7bf73".equals(xPrivCredential)) {
            String userId = request.getHeader("x-user-id");
            if (userId != null) {
                request.setAttribute("userId", Long.parseLong(userId));
            } else {
                request.setAttribute("userId", 0L); // 如果使用了超级管理员凭据，而且没有显式设定用户id，那么设置为管理员id
            }
            return true;
        }

        String encryptedCredential = request.getHeader("x-credential");
        if (encryptedCredential == null) {
            response.setStatus(401);
            response.getWriter().write("Unauthorized");
            return false;
        }

        Long userId = userService.verifyAndExtract(encryptedCredential);
        if (userId != null && userId > 0) {
            request.setAttribute("userId", userId);
            return true;

        }
        response.setStatus(401);
        response.getWriter().write("Unauthorized");
        return false;
    }
}
