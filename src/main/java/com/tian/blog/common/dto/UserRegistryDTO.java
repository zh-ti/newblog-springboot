package com.tian.blog.common.dto;

import lombok.Data;

@Data
public class UserRegistryDTO {
    private String nickname;
    private String account;
    private String password;
    private String registryType;
}
