package com.tian.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@TableName("user")
public class User {
    private String id;
    private String username;
    @JsonIgnore // 向前端响应时对象转JSON忽略密码
    private String password;
    private String avatar;
    private String email;
    private String phone;
    private Integer type;
    private String address;
    private String createTime;
}
