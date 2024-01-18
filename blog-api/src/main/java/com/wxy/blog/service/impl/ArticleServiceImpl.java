package com.wxy.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wxy.blog.dao.mapper.ArticleMapper;
import com.wxy.blog.dao.pojo.Article;
import com.wxy.blog.dao.pojo.SysUser;
import com.wxy.blog.service.ArticleService;
import com.wxy.blog.service.SysUserService;
import com.wxy.blog.service.TagService;
import com.wxy.blog.vo.ArticleVo;
import com.wxy.blog.vo.ArticleTitleVo;
import com.wxy.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;
    @Override
    public List<ArticleVo> listArticle(PageParams pageParams) {

        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);

        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
        return records.stream().map(this::makeArticleVo).collect(Collectors.toList());
    }

    @Override
    public List<ArticleTitleVo> listHotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .select(Article::getId, Article::getTitle)
                .orderByDesc(Article::getViewCounts)
                .last("LIMIT " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return articles.stream().map((fullArticle -> {
            ArticleTitleVo article = new ArticleTitleVo();
            article.setId(fullArticle.getId());
            article.setTitle(fullArticle.getTitle());
            return article;
        })).collect(Collectors.toList());
    }

    @Override
    public List<ArticleVo> listNewArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .select(Article::getId,Article::getTitle)
                .orderByDesc(Article::getCreateDate)
                .last("LIMIT " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return articles.stream().map(this::makeArticleVo).collect(Collectors.toList());
    }

    /**
     * 用pao对象组装返回给前端的articleVo数据，大部分来源于查询 article ，但仍然要手动处理createDate、author、ArticleBod
     * private List<TagVo> tags;
     *
     * @param article article
     * @return articleVo
     */
    private ArticleVo makeArticleVo(Article article) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        articleVo.setTags(tagService.getTagsByArticleId(article.getId()));
        articleVo.setAuthor(sysUserService
                .getUserById(article.getAuthorId())
                .orElseGet(()->{
                    SysUser defaultSysUser = new SysUser();
                    defaultSysUser.setNickname("未知用户");
                    return defaultSysUser;
                })
                .getNickname());
        return articleVo;
    }


}
