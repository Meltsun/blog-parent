package com.wxy.blog.service;

import com.wxy.blog.dao.pojo.SysUser;
import com.wxy.blog.service.exception.ServiceException;
import com.wxy.blog.vo.params.LoginParams;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface LoginService {
    Optional<SysUser> checkUserToken(String token);

    Optional<String> login(LoginParams params);

    void logout(String token);

    Optional<String> register(LoginParams loginParam) throws ServiceException;
}
