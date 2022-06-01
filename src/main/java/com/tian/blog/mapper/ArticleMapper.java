package com.tian.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tian.blog.entity.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("select state from article where id=#{articleId}")
    int queryArticleState(@Param("articleId") String articleId);
}
