package com.tian.blog.entity;

import lombok.Data;

@Data
public class Tag {
    private String id;
    private String name;
    private Integer heat;
    private String createTime;
}
