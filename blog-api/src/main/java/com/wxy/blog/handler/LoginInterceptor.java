package com.wxy.blog.handler;

import com.alibaba.fastjson.JSON;
import com.wxy.blog.dao.pojo.SysUser;
import com.wxy.blog.service.LoginService;
import com.wxy.blog.vo.ErrorCode;
import com.wxy.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if(! (handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader("Authorization");

        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        //springboot提供了静态资源服务器，排除掉；只需要拦截handler
        if (token == null){
            handleResponse(Result.fail(ErrorCode.NO_LOGIN),response);
            return false;
        }
        Optional<SysUser> sysUser = loginService.checkUserToken(token);
        if(!sysUser.isPresent()){
            handleResponse(Result.fail(ErrorCode.NO_LOGIN),response);
            return false;
        }
        return true;
    }

    void handleResponse(Result result,HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(JSON.toJSONString(result));
    }
}
