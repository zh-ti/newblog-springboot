package com.tian.blog.service.impl;

import com.alibaba.fastjson.JSON;
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
import com.tian.blog.utils.JWTUtils;
import com.tian.blog.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<User> getUserById(String id) {
        return Result.success(userMapper.selectById(id));
    }

    @Override
    public Result<PageVo<User>> getUserList(Integer current, Integer size, String username, String phone, String email, String address) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(username)) {
            queryWrapper.like(User::getUsername, username);
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
        List<User> userList = queryUser(user);
        String reason = getReason(userList, user);
        if (reason != null) {
            return Result.fail(reason);
        }
        int insert = userMapper.insert(user);
        return Result.success(insert);
    }

    @Override
    public Result<Integer> updateUser(User user) {
        List<User> userList = queryUser(user);
        String reason = getReason(userList, user);
        if (reason != null) {
            return Result.fail(reason);
        }
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
    public Result<Map<String, Object>> login(UserLoginDTO userLoginDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getUsername, userLoginDTO.getUsername())
                .eq(User::getPassword, userLoginDTO.getPassword());
        User user = userMapper.selectOne(queryWrapper);
        try {
            // TODO: 反应速度过快，延时一秒
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("loginUser: "+user);
        if (user != null) {
            return Result.success(getResponseOfLoginOrRegistry(user));
        }
        return Result.fail("用户名或密码错误");
    }

    @Override
    public Result<Map<String, Object>> registry(UserRegistryDTO userRegistryDTO) {
        String registryType = userRegistryDTO.getRegistryType();
        User user = new User();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 判断注册方式是否正确和选定的注册方式是否已经被注册
        if ("phone".equals(registryType)) {
            user.setPhone(userRegistryDTO.getAccount());
            queryWrapper.eq(User::getPhone, userRegistryDTO.getAccount());
            User resUser = userMapper.selectOne(queryWrapper);
            if (resUser != null) return Result.fail("电话已被注册");
        } else if ("email".equals(registryType)) {
            user.setEmail(userRegistryDTO.getAccount());
            queryWrapper.eq(User::getEmail, userRegistryDTO.getAccount());
            User resUser = userMapper.selectOne(queryWrapper);
            if (resUser != null) return Result.fail("邮箱已被注册");
        } else {
            return Result.fail("请选择正确的注册方式");
        }
        // 判断账号名是否已存在
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userRegistryDTO.getUsername());
        User resUser = userMapper.selectOne(queryWrapper);
        System.out.println(resUser);
        if (resUser != null) {
            return Result.fail("账号已被注册，换一个吧");
        }
        try {
            // TODO: 反应速度过快，延时一秒
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 添加账号
        user.setUsername(userRegistryDTO.getUsername());
        user.setPassword(userRegistryDTO.getPassword());
        user.setType(2); // 默认设置为 2，表示普通用户
        userMapper.insert(user);
        Map<String, Object> result = getResponseOfLoginOrRegistry(user);
        return Result.success(result);
    }

    @Override
    public Result<Boolean> logout(String id) {
        return Result.success(RedisUtils.delete(redisTemplate, "tblog:user:"+id));
    }

    /**
     * 查询具有相同账户、手机号、和邮箱的用户
     *
     * @param user 查询的用户信息
     * @return 满足条件的用户集合
     */
    public List<User> queryUser(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(user.getUsername())) {
            queryWrapper.eq(User::getUsername, user.getUsername());
        }
        if (StringUtils.isNotEmpty(user.getPhone())) {
            queryWrapper.or().eq(User::getPhone, user.getPhone());
        }
        if (StringUtils.isNotEmpty(user.getEmail())) {
            queryWrapper.or().eq(User::getEmail, user.getEmail());
        }
        return userMapper.selectList(queryWrapper);
    }

    /**
     * 判断是否满足添加条件，不满足返回原因，满足返回 null
     *
     * @param userList 具有相同账户或电话或邮箱的用户
     * @param user     添加的用户
     * @return 不满足的原因或 null
     */
    public String getReason(List<User> userList, User user) {
        if (userList.size() > 0) {
            for (User resUser : userList) {
                // 判断 id 是否相同，如果不相同则继续判断账号、邮箱、电话是否相同
                if (!resUser.getId().equals(user.getId())) {
                    if (resUser.getUsername().equals(user.getUsername())) {
                        return "账号已被注册，换一个吧";
                    } else if (StringUtils.isNotEmpty(resUser.getEmail())
                            && resUser.getEmail().equals(user.getEmail())) {
                        return "邮箱已被注册";
                    } else if (StringUtils.isNotEmpty(resUser.getPhone())
                            && resUser.getPhone().equals(user.getPhone())) {
                        return "电话已被注册";
                    }
                }
            }
        }
        return null;
    }

    /**
     * 登录或注册公共的操作和返回值
     *
     * @param user 登录或注册的用户
     * @return 返回指定的值
     */
    public Map<String, Object> getResponseOfLoginOrRegistry(User user) {
        // 获取 token 有效期的时间对象
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zonedDateTime = now.plusDays(1).atZone(ZoneId.systemDefault());
        Date expireTime = new Date(zonedDateTime.toInstant().toEpochMilli());
        // 设置返回的数据
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("username", user.getUsername());
        userMap.put("type", user.getType());
        userMap.put("expireTime", expireTime);
        String token = JWTUtils.createJWT(userMap, expireTime);
        userMap.put("token", token);
        // 写入 redis
        String key = "tblog:user:" + user.getId();
        // 有效时长：有效期 - 当前时间
        long timeout = expireTime.toInstant().toEpochMilli() - System.currentTimeMillis();
        RedisUtils.set(redisTemplate, key, userMap, timeout);
        return userMap;
    }

}
