package com.wxy.blog.controller;

import com.wxy.blog.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping
    public Result hotTags(){
        return Result.success(null);
    }
}
