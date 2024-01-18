package com.wxy.blog.controller;

import com.wxy.blog.service.TagService;
import com.wxy.blog.vo.Result;
import com.wxy.blog.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tags")
public class TagsController {
    @Autowired
    TagService tagService;

    @GetMapping("/hot")
    public Result hotTags(){
        List<TagVo> tagVos = tagService.getHotTagsByDefaultLimit();
        return Result.success(tagVos);
    }
}
