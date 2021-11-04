package com.ruiyuan.springdemo.service;

import com.ruiyuan.springdemo.entity.MBlog;

import java.util.List;

/**
 * (MBlog)表服务接口
 *
 * @author makejava
 * @since 2021-06-22 15:50:02
 */
public interface MBlogService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MBlog queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<MBlog> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param mBlog 实例对象
     * @return 实例对象
     */
    MBlog insert(MBlog mBlog);

    /**
     * 修改数据
     *
     * @param mBlog 实例对象
     * @return 实例对象
     */
    MBlog update(MBlog mBlog);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
