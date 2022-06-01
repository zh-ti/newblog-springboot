package com.tian.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tian.blog.entity.Tag;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper extends BaseMapper<Tag> {
    @Select("select * from tag where name in (#{names})")
    List<Tag> queryTagListByName(@Param("names") List<String> names);

    @Select("select * from tag where name=#{name}")
    Tag queryTagByName(@Param("name") String name);

    @Select("select article_id articleId from article_tag where tag_id in (#{tagsId})")
    List<String> queryArticleIdByTagId(@Param("tagsId") List<String> tagsId);

    @Select("select tag_id tagId from article_tag where article_id in (#{articleId})")
    List<String> queryTagIdByArticleId(@Param("articleId") String articleId);

    @Select("insert into article_tag(article_id, tag_id) values(#{articleId},#{tagId})")
    void insertArticleTag(@Param("articleId") String articleId, @Param("tagId") String tagId);

    @Select("delete from article_tag where article_id=#{articleId}")
    void deleteByArticle(@Param("articleId") String articleId);

    @Select("select count(*) from article_tag where tagId=#{tagId}")
    int queryArticleUseCount(@Param("tagId") String tagId);
}
