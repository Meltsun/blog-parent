package com.wxy.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxy.blog.dao.mapper.CategoryMapper;
import com.wxy.blog.dao.pojo.Category;
import com.wxy.blog.service.CategoryService;
import com.wxy.blog.vo.CategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long id) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .select(
                        Category::getId,
                        Category::getAvatar,
                        Category::getCategoryName,
                        Category::getDescription
                ).eq(Category::getId, id);
        Category category = categoryMapper.selectOne(queryWrapper);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }
}
