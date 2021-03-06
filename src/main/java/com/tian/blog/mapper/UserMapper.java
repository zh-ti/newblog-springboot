package com.tian.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tian.blog.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username=#{username}")
    User queryByUsername(@Param("username") String username);

}