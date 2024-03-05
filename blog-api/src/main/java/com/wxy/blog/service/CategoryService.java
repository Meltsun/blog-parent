package com.wxy.blog.service;


import com.wxy.blog.vo.CategoryVo;

public interface CategoryService {

    CategoryVo findCategoryById(Long id);
}