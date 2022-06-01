package com.tian.blog.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

public class JWTUtilsTest {
    @Test
    public void test(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NTM1NTU1MjUsImlzcyI6InRpYW4iLCJzdWIiOiJoZWxsbyIsImV4cCI6MTY1MzY0MTkyNSwiZXhwaXJlVGltZSI6MTY1MzY0MTkyNTI4MywiaWQiOiIxNTI4OTQyNjc0MjI2NjI2NTYxIiwidHlwZSI6MCwidXNlcm5hbWUiOiJhZG1pbiJ9.8Kc1l8OtMJ6TGhGmy1Vj-EPKf9O0uen-TXTOHTyJX8k";
        Claims claims = JWTUtils.parseJWT(token);
        System.out.println(claims);
    }
}
