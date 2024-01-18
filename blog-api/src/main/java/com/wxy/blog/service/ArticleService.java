package com.wxy.blog.service;

import com.wxy.blog.vo.ArticleVo;
import com.wxy.blog.vo.ArticleTitleVo;
import com.wxy.blog.vo.params.PageParams;

import java.util.List;

public interface ArticleService {
    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    List<ArticleVo> listArticle(PageParams pageParams);

    List<ArticleTitleVo> listHotArticle(int limit);

    List<ArticleVo> listNewArticle(int limit);
}

