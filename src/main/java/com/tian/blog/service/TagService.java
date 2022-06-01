package com.tian.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tian.blog.common.Result;
import com.tian.blog.entity.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {
    Result<List<Tag>> getTagList(String keyword, Integer limit);
    Result<Integer> saveTag(Tag tag);
    Result<Integer> deleteTag(String tagId);
}
