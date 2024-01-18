package com.wxy.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxy.blog.dao.mapper.SysUserMapper;
import com.wxy.blog.dao.pojo.SysUser;
import com.wxy.blog.service.LoginService;
import com.wxy.blog.service.SysUserService;
import com.wxy.blog.util.JWTUtils;
import com.wxy.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private LoginService loginService;
    @Override
    public Optional<SysUser> getUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        return Optional.ofNullable(sysUser);
    }

    @Override
    public Optional<SysUser> getUserByAccountPwd(String account, String pwd) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .select(SysUser::getId,SysUser::getAccount,SysUser::getAvatar,SysUser::getNickname)
                .eq(SysUser::getAccount, account)
                .eq(SysUser::getPassword, pwd)
                .last("limit 1");
        return Optional.ofNullable(sysUserMapper.selectOne(queryWrapper));
    }

    @Override
    public Optional<SysUser> getUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .select(SysUser::getId,SysUser::getAccount,SysUser::getAvatar,SysUser::getNickname)
                .eq(SysUser::getAccount, account)
                .last("limit 1");
        return Optional.ofNullable(sysUserMapper.selectOne(queryWrapper));
    }

    @Override
    public void save(SysUser sysUser) {
        sysUserMapper.insert(sysUser);
    }

    @Override
    public Optional<UserVo> getUserInfoByToken(String token) {
        return loginService
                .checkUserToken(token)
                .filter(x -> JWTUtils.checkToken(token).isPresent())
                .map(x-> {
                    UserVo userVo = new UserVo();
                    BeanUtils.copyProperties(x, userVo);
                    return userVo;
                });
    }
}
