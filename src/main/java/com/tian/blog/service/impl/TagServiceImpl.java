package com.tian.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tian.blog.common.Result;
import com.tian.blog.entity.Tag;
import com.tian.blog.mapper.TagMapper;
import com.tian.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public Result<List<Tag>> getTagList(String keyword, Integer limit) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.like(Tag::getName, keyword);
        }
        queryWrapper.last("limit " + limit);
        List<Tag> tags = tagMapper.selectList(queryWrapper);
        return Result.success(tags);
    }

    @Override
    public Result<Integer> saveTag(Tag tag) {
        if (tagMapper.queryTagByName(tag.getName()) != null) {
            return Result.fail("标签”" + tag.getName() + "“已存在");
        }
        return Result.success(tagMapper.insert(tag));
    }

    @Override
    public Result<Integer> deleteTag(String tagId) {
        int count = tagMapper.queryArticleUseCount(tagId);
        if (count > 0) {
            return Result.fail("该标签已被文章引用无法删除");
        } else {
            return Result.success(tagMapper.deleteById(tagId));
        }
    }
}
