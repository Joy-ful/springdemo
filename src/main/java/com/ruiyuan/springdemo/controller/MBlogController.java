package com.ruiyuan.springdemo.controller;

import com.ruiyuan.springdemo.entity.MBlog;
import com.ruiyuan.springdemo.service.MBlogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (MBlog)表控制层
 *
 * @author makejava
 * @since 2021-06-22 15:50:02
 */
@RestController
@RequestMapping("mBlog")
public class MBlogController {
    /**
     * 服务对象
     */
    @Resource
    private MBlogService mBlogService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public MBlog selectOne(Long id) {
        return this.mBlogService.queryById(id);
    }

}
