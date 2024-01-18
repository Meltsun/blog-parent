package com.wxy.blog.controller;

import com.wxy.blog.service.LoginService;
import com.wxy.blog.service.SysUserService;
import com.wxy.blog.vo.ErrorCode;
import com.wxy.blog.vo.Result;
import com.wxy.blog.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private LoginService loginService;

    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        Optional<UserVo> userVo = sysUserService.getUserInfoByToken(token);
        return userVo.map(Result::success).orElse(Result.fail(ErrorCode.NO_LOGIN));
    }

    @GetMapping("logout")
    public Result logout(@RequestHeader("Authorization") String token) {
        loginService.logout(token);
        return Result.success(null);
    }
}
