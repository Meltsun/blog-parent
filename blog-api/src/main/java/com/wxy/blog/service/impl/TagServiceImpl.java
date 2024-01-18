package com.wxy.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxy.blog.dao.mapper.TagMapper;
import com.wxy.blog.dao.pojo.Article;
import com.wxy.blog.dao.pojo.Tag;
import com.wxy.blog.service.TagService;
import com.wxy.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagMapper tagMapper;

    @Override
    public List<TagVo> getTagsByArticleId(Long articleId) {
        List<Tag> tags = tagMapper.getTagsByArticleId(articleId);
        return makeVo(tags);
    }

    @Override
    public List<TagVo> getHotTagsByDefaultLimit() {
        return getHotTags(TagService.defaultLimit);
    }

    public List<TagVo> getHotTags(int limit){
        List<Long> ids =  tagMapper.getHotTags(TagService.defaultLimit);
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Tag::getId, ids);
        List<Tag> tags = tagMapper.selectList(queryWrapper);
        return makeVo(tags);
    }

    private List<TagVo> makeVo(List<Tag> tags){
        return tags.stream().map(this::makeVo).collect(Collectors.toList());
    }

    private TagVo makeVo(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
}
