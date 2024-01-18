package com.wxy.blog.vo;

import lombok.Data;

@Data
public class UserVo {
    private Long id;

    private String account;

    private String nickname;

    private String avatar;
}