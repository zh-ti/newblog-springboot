package com.tian.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tian.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
