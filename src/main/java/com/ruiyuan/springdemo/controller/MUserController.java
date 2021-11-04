package com.ruiyuan.springdemo.controller;

import com.ruiyuan.springdemo.entity.MUser;
import com.ruiyuan.springdemo.service.MUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (MUser)表控制层
 *
 * @author makejava
 * @since 2021-06-22 15:41:11
 */
@RestController
@RequestMapping("mUser")
public class MUserController {
    /**
     * 服务对象
     */
    @Resource
    private MUserService mUserService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public MUser selectOne(Long id) {
        return this.mUserService.queryById(id);
    }

}
