package com.wxy.blog.dao.mapper;

import com.wxy.blog.dao.pojo.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章标签查询列表
     */
    List<Tag> getTagsByArticleId(@Param("articleId") Long articleId);

    List<Long> getHotTags(@Param("limit") int limit);
}
