package com.wxy.blog.service;

import com.wxy.blog.dao.pojo.SysUser;
import com.wxy.blog.vo.UserVo;

import java.util.Optional;

public interface SysUserService{
    Optional<SysUser> getUserById(Long id);

    /**
     * @param account 账户名
     * @param pwd     密码
     * @return 账户的id
     */
    Optional<SysUser> getUserByAccountPwd(String account, String pwd);

    Optional<UserVo> getUserInfoByToken(String token);

    Optional<SysUser> getUserByAccount(String account);

    void save(SysUser sysUser);
}
