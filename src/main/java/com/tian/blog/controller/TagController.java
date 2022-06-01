package com.tian.blog.controller;

import com.tian.blog.common.Result;
import com.tian.blog.entity.Tag;
import com.tian.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public Result<List<Tag>> list(@Nullable @RequestParam(value = "keyword") String keyword,
                                  @Nullable @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return tagService.getTagList(keyword, limit);
    }

    @PostMapping
    public Result<Integer> save(@RequestBody Tag tag){
        return tagService.saveTag(tag);
    }

    @DeleteMapping
    public Result<Integer> delete(@RequestParam("id") String id){
        return tagService.deleteTag(id);
    }
}
