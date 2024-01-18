package com.wxy.blog.service;

import com.wxy.blog.vo.TagVo;

import java.util.List;

public interface TagService {
    List<TagVo> getTagsByArticleId(Long articleId);
    List<TagVo> getHotTagsByDefaultLimit();

    int defaultLimit =3;
}
