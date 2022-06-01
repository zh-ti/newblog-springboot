package com.tian.blog.service.impl;

import com.tian.blog.common.Result;
import com.tian.blog.entity.Category;
import com.tian.blog.mapper.CategoryMapper;
import com.tian.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result<List<Category>> getCategoryList() {
        List<Category> categoryList = categoryMapper.selectList(null);
        return Result.success(categoryList);
    }
}
