package com.wxy.blog.service;

import com.wxy.blog.vo.ArticleVo;
import com.wxy.blog.vo.ArticleTitleVo;
import com.wxy.blog.vo.params.PageParams;

import java.util.List;

public interface ArticleService {
    /**
     * 分页查询
     * @param pageParams 分页参数
     * @return 文章列表
     */
    List<ArticleVo> listArticle(PageParams pageParams);

    List<ArticleTitleVo> listHotArticle(int limit);

    List<ArticleTitleVo> listNewArticle(int limit);

    ArticleVo findArticleById(Long id);
}

