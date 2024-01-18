package com.wxy.blog.vo;

//import com.wxy.blog.dao.pojo.ArticleBody;
//import com.wxy.blog.dao.pojo.Category;
import lombok.Data;

import java.util.List;

@Data
public class ArticleVo extends ArticleTitleVo {

    private String summary;

    private int commentCounts;

    private int viewCounts;

    private int weight;
    /**
     * 创建时间
     */
    private String createDate;

    private String author;

//    private ArticleBodyVo body;

    private List<TagVo> tags;

//    private List<CategoryVo> categorys;

}
