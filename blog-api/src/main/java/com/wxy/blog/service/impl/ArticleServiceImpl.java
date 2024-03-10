package com.wxy.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wxy.blog.dao.mapper.ArticleBodyMapper;
import com.wxy.blog.dao.mapper.ArticleMapper;
import com.wxy.blog.dao.pojo.Article;
import com.wxy.blog.dao.pojo.ArticleBody;
import com.wxy.blog.dao.pojo.SysUser;
import com.wxy.blog.service.ArticleService;
import com.wxy.blog.service.CategoryService;
import com.wxy.blog.service.SysUserService;
import com.wxy.blog.service.TagService;
import com.wxy.blog.vo.ArticleBodyVo;
import com.wxy.blog.vo.ArticleVo;
import com.wxy.blog.vo.ArticleTitleVo;
import com.wxy.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
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

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Override
    public List<ArticleTitleVo> listArticle(PageParams pageParams) {

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
        return articles
                .stream()
                .map(x->{
                    ArticleTitleVo articleTitleVo = new ArticleTitleVo();
                    BeanUtils.copyProperties(x,articleTitleVo);
                    return articleTitleVo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleTitleVo> listNewArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .select(Article::getId, Article::getTitle)
                .orderByDesc(Article::getCreateDate)
                .last("LIMIT " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return articles
                .stream()
                .map(x->{
                    ArticleTitleVo articleTitleVo = new ArticleTitleVo();
                    BeanUtils.copyProperties(x,articleTitleVo);
                    return articleTitleVo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ArticleVo viewArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        asyncArticleService.updateViewCount(article);
        return makeArticleVo(article);
    }

    @Autowired
    private AsyncArticleService asyncArticleService;

    /**
     * 为article添加createDate,tags,author,body
     * private List<TagVo> tags;
     *
     * @param article 完整的article
     * @return 添加了article中没有的属性的articleVo
     */
    private ArticleVo makeArticleVo(Article article) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        articleVo.setTags(tagService.getTagsByArticleId(article.getId()));
        articleVo.setAuthor(sysUserService
                .getUserById(article.getAuthorId())
                .orElseGet(() -> {
                    SysUser defaultSysUser = new SysUser();
                    defaultSysUser.setNickname("未知用户");
                    return defaultSysUser;
                })
                .getNickname());
        articleVo.setBody(getBodyById(article.getBodyId()));
        articleVo.setCategory(categoryService.findCategoryById(article.getCategoryId()));
        return articleVo;
    }

    private ArticleBodyVo getBodyById(Long id){
        LambdaQueryWrapper<ArticleBody> articleBodyQueryWrapper = new LambdaQueryWrapper<>();
        articleBodyQueryWrapper.select(
                ArticleBody::getId,
                ArticleBody::getContent,
                ArticleBody::getContentHtml,
                ArticleBody::getArticleId).eq(ArticleBody::getId, id);
        ArticleBody articleBody = articleBodyMapper.selectOne(articleBodyQueryWrapper);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}

@EnableAsync
@Component
class AsyncArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Async("taskExecutor")
    protected void updateViewCount(Article article){
        try {
            //睡眠5秒 证明不会影响主线程的使用
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(article.getViewCounts() + 1);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId,article.getId());
        queryWrapper.eq(Article::getViewCounts,article.getViewCounts());//?
        articleMapper.update(articleUpdate,queryWrapper);
    }
}