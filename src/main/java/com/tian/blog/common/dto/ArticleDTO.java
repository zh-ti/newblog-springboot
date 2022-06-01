package com.tian.blog.common.dto;

import com.tian.blog.entity.Tag;
import lombok.Data;

import java.util.List;

@Data
public class ArticleDTO {
    private String id;
    private String title;
    private Integer type;
    private Integer state;
    private String author;
    private String category;
    private String originUrl;
    private List<Tag> tags;
    private String summary;
    private String html;
    private String text;
}
