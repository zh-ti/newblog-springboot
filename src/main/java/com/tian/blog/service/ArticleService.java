package com.tian.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tian.blog.common.Result;
import com.tian.blog.common.dto.ArticleDTO;
import com.tian.blog.common.vo.ArticleVo;
import com.tian.blog.common.vo.PageVo;
import com.tian.blog.entity.Article;

import java.util.List;


public interface ArticleService extends IService<Article> {
    Result<PageVo<ArticleVo>> getArticleList(Integer current,
                                             Integer size,
                                             String id,
                                             String title,
                                             String author,
                                             Integer state,
                                             List<String> tags,
                                             String createTime);

    Result<ArticleVo> getArticleById(String id);

    Result<String> saveArticle(ArticleDTO articleDTO);

    Result<Integer> updateArticle(ArticleDTO article);

    Result<Integer> deleteArticle(List<String> idList);

    Result<Integer> updateState(String id, Integer state);
}
