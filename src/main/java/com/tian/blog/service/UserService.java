package com.tian.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tian.blog.common.Result;
import com.tian.blog.common.dto.UserLoginDTO;
import com.tian.blog.common.dto.UserRegistryDTO;
import com.tian.blog.entity.User;
import com.tian.blog.common.vo.PageVo;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    Result<User> getUserById(String id);
    Result<PageVo<User>> getUserList(Integer current, Integer size, String name, String phone, String email, String address);
    Result<Integer> saveUser(User user);
    Result<Integer> updateUser(User user);
    Result<Integer> deleteUsers(List<String> idList);
    Result<Map<String, Object>> login(UserLoginDTO userLoginDTO);
    Result<Map<String, Object>> registry(UserRegistryDTO userRegistryDTO);
    Result<Boolean> logout(String id);
}
