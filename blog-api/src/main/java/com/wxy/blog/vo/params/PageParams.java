package com.wxy.blog.vo.params;

import lombok.Data;

@Data
public class PageParams {
    private int page = 1;
    private int pageSize = 10;
}
