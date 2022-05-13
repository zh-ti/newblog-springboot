package com.tian.blog.controller;

import com.tian.blog.common.Result;
import com.tian.blog.common.dto.UserLoginDTO;
import com.tian.blog.common.dto.UserRegistryDTO;
import com.tian.blog.entity.User;
import com.tian.blog.service.UserService;
import com.tian.blog.common.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Result<PageVo<User>> userList(@RequestParam(value = "page", defaultValue = "1") Integer current,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size,
                                         @Nullable @RequestParam(value = "name") String name,
                                         @Nullable @RequestParam(value = "phone") String phone,
                                         @Nullable @RequestParam(value = "email") String email,
                                         @Nullable @RequestParam(value = "address") String address) {
        return userService.getUserList(current, size, name, phone, email, address);
    }

    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable("id") String id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public Result<Integer> save(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping
    public Result<Integer> update(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping
    public Result<Integer> delete(@RequestParam("ids") String idString) {
        List<String> idList = Arrays.stream(idString.split(",")).collect(Collectors.toList());
        return userService.deleteUsers(idList);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginDTO userLoginDTO){
        return userService.login(userLoginDTO);
    }

    @PostMapping("/registry")
    public Result<String> registry(@RequestBody UserRegistryDTO userRegistryDTO){
        return userService.registry(userRegistryDTO);
    }
}
