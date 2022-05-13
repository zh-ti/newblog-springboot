package com.tian.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tian.blog.common.Result;
import com.tian.blog.common.dto.UserLoginDTO;
import com.tian.blog.common.dto.UserRegistryDTO;
import com.tian.blog.entity.User;
import com.tian.blog.mapper.UserMapper;
import com.tian.blog.service.UserService;
import com.tian.blog.common.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<User> getUserById(String id) {
        return Result.success(userMapper.selectById(id));
    }

    @Override
    public Result<PageVo<User>> getUserList(Integer current, Integer size, String name, String phone, String email, String address) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like(User::getName, name);
        }
        if (StringUtils.isNotEmpty(phone)) {
            queryWrapper.like(User::getPhone, phone);
        }
        if (StringUtils.isNotEmpty(email)) {
            queryWrapper.like(User::getEmail, email);
        }
        if (StringUtils.isNotEmpty(address)) {
            queryWrapper.like(User::getAddress, address);
        }
        queryWrapper.orderByDesc(User::getCreateTime);
        Page<User> userPage = userMapper.selectPage(new Page<>(current, size), queryWrapper);
        PageVo<User> userPageVo =
                new PageVo<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal(),
                        userPage.hasPrevious(), userPage.hasNext(), userPage.getRecords());
        return Result.success(userPageVo);
    }

    @Override
    public Result<Integer> saveUser(User user) {
        int insert = userMapper.insert(user);
        return Result.success(insert);
    }

    @Override
    public Result<Integer> updateUser(User user) {
        int update = userMapper.updateById(user);
        return Result.success(update);
    }

    @Override
    public Result<Integer> deleteUsers(List<String> idList) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(User::getId, idList);
        int delete = userMapper.delete(queryWrapper);
        return Result.success(delete);
    }

    @Override
    public Result<String> login(UserLoginDTO userLoginDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getPhone, userLoginDTO.getAccount())
                .or()
                .eq(User::getEmail, userLoginDTO.getAccount())
                .and(i -> i.eq(User::getPassword, userLoginDTO.getPassword()));
        User user = userMapper.selectOne(queryWrapper);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (user != null) {
            return Result.success("登录成功");
        }
        return Result.fail("用户名或密码错误");
    }

    @Override
    public Result<String> registry(UserRegistryDTO userRegistryDTO) {
        String registryType = userRegistryDTO.getRegistryType();
        User user = new User();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if ("phone".equals(registryType)) {
            user.setPhone(userRegistryDTO.getAccount());
            queryWrapper.eq(User::getPhone, userRegistryDTO.getAccount());
            User resUser = userMapper.selectOne(queryWrapper);
            if (resUser != null) return Result.fail("该电话已被注册");
        } else if ("email".equals(registryType)) {
            user.setEmail(userRegistryDTO.getAccount());
            queryWrapper.eq(User::getEmail, userRegistryDTO.getAccount());
            User resUser = userMapper.selectOne(queryWrapper);
            if (resUser != null) return Result.fail("该邮箱已被注册");
        } else {
            return Result.fail("请选择正确的注册方式");
        }
        user.setNickname(userRegistryDTO.getNickname());
        user.setPassword(userRegistryDTO.getPassword());
        userMapper.insert(user);
        return Result.success("注册成功");
    }
}
