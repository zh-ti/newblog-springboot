package com.tian.blog.common.vo;

import com.tian.blog.entity.Category;
import com.tian.blog.entity.Tag;
import com.tian.blog.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {
    private String id;
    private String title;
    private Integer type;
    private Integer state;
    private User author;
    private Category category;
    private List<Tag> tags;
    private String summary;
    private String originUrl;
    private Integer great;
    private Integer view;
    private Integer comment;
    private String html;
    private String text;
    private String createTime;
}
