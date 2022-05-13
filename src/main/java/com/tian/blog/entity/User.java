package com.tian.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@TableName("user")
public class User {
    private String id;
    private String name;
    @JsonIgnore
    private String password;
    private String nickname;
    private String avatar;
    private String email;
    private String phone;
    private String address;
    private String createTime;
}
