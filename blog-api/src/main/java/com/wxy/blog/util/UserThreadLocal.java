package com.wxy.blog.util;

import com.wxy.blog.dao.pojo.SysUser;

import java.util.Optional;


public class UserThreadLocal implements AutoCloseable{
    //私有构造器，禁止代码创建实例，只允许使用静态方法
    private UserThreadLocal(){}
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void set(SysUser sysUser){
        LOCAL.set(sysUser);
    }
    public static Optional<SysUser> get(){
        return Optional.ofNullable(LOCAL.get());
    }
    public static void remove(){
        LOCAL.remove();
    }

    @Override
    public void close() {
        LOCAL.remove();
    }
}

