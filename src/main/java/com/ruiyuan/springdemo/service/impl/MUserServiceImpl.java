package com.ruiyuan.springdemo.service.impl;

import com.ruiyuan.springdemo.entity.MUser;
import com.ruiyuan.springdemo.dao.MUserDao;
import com.ruiyuan.springdemo.service.MUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (MUser)表服务实现类
 *
 * @author makejava
 * @since 2021-06-22 15:41:11
 */
@Service("mUserService")
public class MUserServiceImpl implements MUserService {
    @Resource
    private MUserDao mUserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public MUser queryById(Long id) {
        return this.mUserDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<MUser> queryAllByLimit(int offset, int limit) {
        return this.mUserDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param mUser 实例对象
     * @return 实例对象
     */
    @Override
    public MUser insert(MUser mUser) {
        this.mUserDao.insert(mUser);
        return mUser;
    }

    /**
     * 修改数据
     *
     * @param mUser 实例对象
     * @return 实例对象
     */
    @Override
    public MUser update(MUser mUser) {
        this.mUserDao.update(mUser);
        return this.queryById(mUser.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.mUserDao.deleteById(id) > 0;
    }
}
