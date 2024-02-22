package com.wxy.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.wxy.blog.dao.pojo.SysUser;
import com.wxy.blog.service.LoginService;
import com.wxy.blog.service.SysUserService;
import com.wxy.blog.service.exception.ServiceException;
import com.wxy.blog.util.JWTUtils;
import com.wxy.blog.vo.ErrorCode;
import com.wxy.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String salt = "mszlu!@#";

    private String encodePassword(String password){
        return DigestUtils.md5Hex(password + salt);
    }

    private String cacheUserToken(SysUser sysUser) {
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set(
                "TOKEN_"+token,
                JSON.toJSONString(sysUser),
                1,
                TimeUnit.DAYS
        );
        return token;
    }
    @Override
    public Optional<SysUser> checkUserToken(String token){
        String userJson = redisTemplate.opsForValue().get("TOKEN_"+token);
        return Optional.ofNullable(userJson).filter(x->!x.isEmpty()).map(x -> JSON.parseObject(x, SysUser.class));
    }

    @Override
    public Optional<String> login(LoginParams params) {
        String password = params.getPassword();
        password = encodePassword(password);
        Optional<SysUser> sysUser = sysUserService.getUserByAccountPwd(params.getAccount(), password);
        return sysUser.map(this::cacheUserToken);
    }

    @Override
    public void logout(String token) {
        JWTUtils
                .checkToken(token)
                .ifPresent(x-> redisTemplate.delete("TOKEN_" + token));
    }

    @Override
    public Optional<String> register(LoginParams loginParam) throws ServiceException {
        if(sysUserService.getUserByAccount(loginParam.getAccount()).isPresent()){
            throw new ServiceException(ErrorCode.DUP_ACCOUNT);
        }
        SysUser sysUser = new SysUser();
        sysUser.setNickname(loginParam.getNickname());
        sysUser.setAccount(loginParam.getAccount());
        sysUser.setPassword(DigestUtils.md5Hex(loginParam.getPassword()+salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        sysUserService.save(sysUser);
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Optional.of(token);
    }
}