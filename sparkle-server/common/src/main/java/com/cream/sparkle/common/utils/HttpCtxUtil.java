package com.cream.sparkle.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * http上下文工具类
 */
public class HttpCtxUtil {

    /**
     * 登录过后的每次http请求可以通过这个方法获取uid
     */
    public static long getUid() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
