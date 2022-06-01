package com.tian.blog.intercetpor;

import com.alibaba.fastjson.JSON;
import com.tian.blog.common.Result;
import com.tian.blog.utils.JWTUtils;
import com.tian.blog.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class TokenHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        boolean flag; // 判断中途是否有条件不满足
        if (request.getMethod().equalsIgnoreCase("options")) {
            return true;
        }
        String token = request.getHeader("token");
        Claims claims = null;
        flag = token != null; // 判断 token 是否不为空
        if (flag) {
            try {
                claims = JWTUtils.parseJWT(token);
            } catch (ExpiredJwtException e) {
                log.error("JWT 已过期");
                flag = false;
            }
        }
        if (flag) {
            Object id = claims.get("id");
            String key = "tblog:user:" + id;
            long start = System.currentTimeMillis();
            String userJson = RedisUtils.readString(redisTemplate, key);
            long end = System.currentTimeMillis();
            log.info("redis 检索已登录用户信息耗时：{} ms", end - start);
            if (userJson == null) {
                flag = false;
            }
        }
        if (!flag) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(Result.fail("请重新登录！")));
            return false;
        }
        return true;
    }

}
