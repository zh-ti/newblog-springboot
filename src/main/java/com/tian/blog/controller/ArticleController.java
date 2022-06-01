package com.tian.blog.controller;

import com.tian.blog.common.Result;
import com.tian.blog.common.dto.ArticleDTO;
import com.tian.blog.common.vo.ArticleVo;
import com.tian.blog.common.vo.PageVo;
import com.tian.blog.entity.Article;
import com.tian.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping
    public Result<PageVo<ArticleVo>> articleList(@RequestParam(value = "page", defaultValue = "1") Integer current,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                 @Nullable @RequestParam("id") String id,
                                                 @Nullable @RequestParam("title") String title,
                                                 @Nullable @RequestParam("author") String author,
                                                 @Nullable @RequestParam("state") Integer state,
                                                 @Nullable @RequestParam("tag") String tagString,
                                                 @Nullable @RequestParam("createTime") String createTime){
        List<String> tagList = new ArrayList<>();
        if(tagString != null){
            tagList = Arrays.stream(tagString.split(",")).collect(Collectors.toList());
        }
        return articleService.getArticleList(current, size, id, title, author, state, tagList, createTime);
    }

    @PostMapping
    public Result<String> save(@RequestBody ArticleDTO articleDTO){
        return articleService.saveArticle(articleDTO);
    }

    @PutMapping
    public Result<Integer> update(@RequestBody ArticleDTO articleDTO){
        return articleService.updateArticle(articleDTO);
    }

    @DeleteMapping
    public Result<Integer> delete(@RequestParam("ids") String idString){
        List<String> idList = Arrays.stream(idString.split(",")).collect(Collectors.toList());
        return articleService.deleteArticle(idList);
    }

    @PatchMapping
    public Result<Integer> setState(@RequestParam("id") String id, @RequestParam("state") Integer state){
        return articleService.updateState(id, state);
    }
}
