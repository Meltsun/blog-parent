package com.wxy.blog.service.exception;

import com.wxy.blog.vo.ErrorCode;

public class ServiceException extends Exception {
    public ErrorCode errorCode;
    public ServiceException(ErrorCode code) {
        super(code.name());
        this.errorCode = code;
    }
}