package com.wxy.blog.controller;

import com.wxy.blog.service.LoginService;
import com.wxy.blog.vo.ErrorCode;
import com.wxy.blog.vo.Result;
import com.wxy.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParams params){
        Optional<String> token = loginService.login(params);
        return token.map(Result::success).orElse(Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST));
    }
}
