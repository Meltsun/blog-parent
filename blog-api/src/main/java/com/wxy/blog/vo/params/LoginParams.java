package com.wxy.blog.vo.params;

import lombok.Data;

@Data
public class LoginParams {
    private String account;
    private String password;
    private String nickname;
}
