package com.wxy.blog.service.impl;

import com.wxy.blog.dao.mapper.ArticleMapper;
import com.wxy.blog.service.ArchivesService;
import com.wxy.blog.dao.dos.Archives;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchivesServiceImpl implements ArchivesService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<Archives> listArchives() {
        List<Archives> archives = articleMapper.listArchives();

        return archives;
    }
}
