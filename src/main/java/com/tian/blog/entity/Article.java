package com.tian.blog.entity;

import lombok.Data;

@Data
public class Article {
    private String id;
    private String title;
    private Integer type;
    private Integer state;
    private String author;
    private String category;
    private String summary;
    private String originUrl;
    private Integer great;
    private Integer view;
    private Integer comment;
    private String html;
    private String text;
    private String createTime;
}
