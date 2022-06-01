package com.tian.blog.service;

import com.tian.blog.common.Result;
import com.tian.blog.entity.Category;

import java.util.List;

public interface CategoryService {
        Result<List<Category>> getCategoryList();
}
