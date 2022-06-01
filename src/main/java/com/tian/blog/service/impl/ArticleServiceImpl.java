package com.tian.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tian.blog.common.Result;
import com.tian.blog.common.dto.ArticleDTO;
import com.tian.blog.common.vo.ArticleVo;
import com.tian.blog.common.vo.PageVo;
import com.tian.blog.entity.Article;
import com.tian.blog.entity.Tag;
import com.tian.blog.entity.User;
import com.tian.blog.mapper.ArticleMapper;
import com.tian.blog.mapper.CategoryMapper;
import com.tian.blog.mapper.TagMapper;
import com.tian.blog.mapper.UserMapper;
import com.tian.blog.service.ArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result<PageVo<ArticleVo>> getArticleList(Integer current,
                                                    Integer size,
                                                    String id,
                                                    String title,
                                                    String author,
                                                    Integer state,
                                                    List<String> tags,
                                                    String createTime) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(id)) {
            queryWrapper.eq(Article::getId, id);
        }
        if (StringUtils.isNotEmpty(title)) {
            queryWrapper.like(Article::getTitle, title);
        }
        if (StringUtils.isNotEmpty(author)) {
            User user = userMapper.queryByUsername(author);
            if (user != null) {
                queryWrapper.eq(Article::getAuthor, user.getId());
            } else {
                queryWrapper.last("limit 0");
            }
        }
        if (state != null) {
            queryWrapper.eq(Article::getState, state);
        }
        if (tags != null && !tags.isEmpty()) {
//            // 按标签搜索
//            List<Tag> tagList = tagMapper.queryTagListByName(tags);
//            List<String> tagIds = tagList.stream().map(Tag::getId).collect(Collectors.toList());
//            List<String> articlesId = tagMapper.queryArticleIdByTagId(tagIds);
//            queryWrapper.in(Article::getId, articlesId);
        }
        if (StringUtils.isNotEmpty(createTime)) {
            // TODO: 时间条件
        }
        Page<Article> articlePage = articleMapper.selectPage(new Page<>(current, size), queryWrapper);
        List<Article> articleList = articlePage.getRecords();
        PageVo<ArticleVo> articleVoPageVo = new PageVo<>(articlePage.getCurrent(),
                articlePage.getSize(),
                articlePage.getTotal(),
                articlePage.hasPrevious(),
                articlePage.hasNext(),
                valueOfArticleVoList(articleList));
        return Result.success(articleVoPageVo);
    }

    @Override
    public Result<ArticleVo> getArticleById(String id) {
        Article article = articleMapper.selectById(id);
        return Result.success(valueOfArticleVo(article));
    }

    @Override
    public Result<String> saveArticle(ArticleDTO articleDTO) {
        Article article = valueOfArticle(articleDTO);
        articleMapper.insert(article);
        if (articleDTO.getTags().size() > 0) {
            List<Tag> tagsId = articleDTO.getTags();
            // 将所有标签存入 article_tag 表
            tagsId.forEach(item -> {
                tagMapper.insertArticleTag(article.getId(), item.getId());
            });
        }
        return Result.success(article.getId());
    }

    @Override
    public Result<Integer> updateArticle(ArticleDTO articleDTO) {
        Article article = valueOfArticle(articleDTO);
        if (articleMapper.selectById(article.getId()) == null) {
            return Result.fail("文章不存在");
        }
        int update = articleMapper.updateById(article);
        return Result.success(update);
    }

    @Override
    public Result<Integer> deleteArticle(List<String> idList) {
        for (String id : idList) {
            if (articleMapper.queryArticleState(id) != 0) {
                Article article = articleMapper.selectById(id);
                return Result.fail("文章”" + article.getTitle() + "“已发布，无法删除");
            }
        }
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Article::getId, idList);
        int delete = articleMapper.delete(queryWrapper);
        return Result.success(delete);
    }

    @Override
    public Result<Integer> updateState(String id, Integer state) {
        Article article = new Article();
        article.setId(id);
        article.setState(state);
        int update = articleMapper.updateById(article);
        return Result.success(update);
    }

    public List<ArticleVo> valueOfArticleVoList(List<Article> articleList) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        articleList.forEach(item -> articleVoList.add(valueOfArticleVo(item)));
        return articleVoList;
    }

    public ArticleVo valueOfArticleVo(Article article) {
        if (article == null) {
            return null;
        }
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setAuthor(userMapper.selectById(article.getAuthor()));
        // 通过文章 id 查询出，文章的标签并设置给 articleVo
        List<String> tagsId = tagMapper.queryTagIdByArticleId(article.getId());
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Tag::getId, tagsId);
        if (tagsId.size() > 0) {
            articleVo.setTags(tagMapper.selectList(queryWrapper));
        }
        // 设置 articleVo 的 category
        articleVo.setCategory(categoryMapper.selectById(article.getCategory()));
        return articleVo;
    }

    public Article valueOfArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article);
        if (articleDTO.getTags().size() > 0) {
            // 先删除当前文章对应标签的所有记录
            tagMapper.deleteByArticle(article.getId());
            // 将所有标签存入 article_tag 表
            List<Tag> tagsId = articleDTO.getTags();
            tagsId.forEach(item -> {
                tagMapper.insertArticleTag(article.getId(), item.getId());
            });
        }
        return article;
    }
}
