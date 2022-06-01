package com.tian.blog.entity;

import lombok.Data;

@Data
public class ArticleTag {
    private String id;
    private String articleId;
    private String tagId;
}
