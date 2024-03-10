package com.wxy.blog.controller;

import com.wxy.blog.dao.dos.Archives;
import com.wxy.blog.service.ArchivesService;
import com.wxy.blog.service.ArticleService;
import com.wxy.blog.vo.ArticleVo;
import com.wxy.blog.vo.ArticleTitleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.wxy.blog.vo.Result;
import com.wxy.blog.vo.params.PageParams;

import java.util.List;

@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArchivesService archivesService;
    /**
     * 首页文章列表
     * @param pageParams 分页参数
     * @return -
     */
    @PostMapping
    public Result listArticle(@RequestBody PageParams pageParams){
        List<ArticleTitleVo> articles = articleService.listArticle(pageParams);

        return Result.success(articles);
    }

    @PostMapping("hot")
    public Result hotArticle(){
        int limit = 5;
        List<ArticleTitleVo> articles = articleService.listHotArticle(limit);
        return Result.success(articles);
    }

    @PostMapping("listArchives")
    public Result listArchives(){
        List<Archives> archives = archivesService.listArchives();
        return Result.success(archives);
    }

    @PostMapping("new")
    public Result newArticle(){
        int limit = 5;
        List<ArticleTitleVo> articles = articleService.listNewArticle(limit);
        return Result.success(articles);
    }

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long id) {
        ArticleVo articleVo = articleService.viewArticleById(id);
        return Result.success(articleVo);
    }
}
