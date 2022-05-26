package com.tian.blog.utils;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisUtilsTest {

    @Autowired
    StringRedisTemplate template;

    @Test
    void testUtils(){
        String userJson = RedisUtils.readString(template,"user");
        User1 user = JSON.parseObject(userJson, User1.class);
        System.out.println(user);
    }

    @Test
    void testDel(){
        String key = "tblog:user:1528942674226626561";
        System.out.println(template.delete(key));
    }
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class User1{
    private String name;
    private Integer age;
}