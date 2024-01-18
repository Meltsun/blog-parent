package com.wxy.blog.controller;

import com.wxy.blog.service.LoginService;
import com.wxy.blog.service.exception.ServiceException;
import com.wxy.blog.vo.ErrorCode;
import com.wxy.blog.vo.Result;
import com.wxy.blog.vo.params.LoginParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@RestController
@RequestMapping("/register")
public class RigisterController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result register(@RequestBody LoginParams loginParam){
        try {
            Optional<String> token = loginService.register(loginParam);
            return token.map(Result::success).orElse(Result.fail(ErrorCode.PARAMS_ERROR));
        } catch (ServiceException e) {
            return Result.fail(e.errorCode);
        }
    }
}
