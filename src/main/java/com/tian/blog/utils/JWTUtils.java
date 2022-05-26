package com.tian.blog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    private final static String salt = "Hello#$%"; // 密钥
    private final static long expireTime = 50000; // 过期时间
    private final static String issuer = "tian"; // 签发者

    public static String createJWT(Map<String, Object> claims, Date expireTime) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setIssuer(issuer)
                .setSubject("hello")
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, salt)
                .addClaims(claims)
                .compact();
    }

    public static Claims parseJWT(String token) {
        return Jwts.parser()
                .setSigningKey(salt)
                .parseClaimsJws(token)
                .getBody();
    }

    public static void testCreateJWT() {
        JwtBuilder builder = Jwts.builder()     // new 一个 JwtBuilder
                .setId("1415926")               // 设置 jti(JWT ID)：JWT的唯一标识，根据业务需要，可以设置为一个不重复的值，主要用来作为一次性 token,从而回避重放攻击
                .setIssuedAt(new Date())        // 设置签发日期
                .setIssuer(issuer)              // issuer：jwt签发者
                .setSubject("hello")            // 代表这个 JWT 的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么 userid 之类的，作为用户的唯一标志
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))  // 设置过期时间
                .signWith(SignatureAlgorithm.HS256, salt); // 设置签名使用的算法和秘钥 (salt)
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", "tian");
        userInfo.put("age", "20");
        builder.addClaims(userInfo);
        System.out.println(builder.compact());
    }
}
