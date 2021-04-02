package com.ipman.springboot.transactional.sample.core.service.impl;

import com.ipman.springboot.transactional.sample.common.domain.model.User2;
import com.ipman.springboot.transactional.sample.core.dao.User2Mapper;
import com.ipman.springboot.transactional.sample.core.service.User2Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by ipipman on 2021/4/2.
 *
 * @version V1.0
 * @Package com.ipman.springboot.transactional.sample.core.service.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/4/2 11:50 上午
 */
@Service
public class User2ServiceImpl implements User2Service {

    @Resource
    private User2Mapper user2Mapper;

    /**
     *  Propagation.REQUIRED
     *  如果当前没有事务，就创建一个事务
     *  如果已经存在一个事务中，加入这个事务中
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public void addRequired(User2 user2){
        user2Mapper.insert(user2);
    }

    /**
     *  Propagation.REQUIRED
     *  如果当前没有事务，就创建一个事务
     *  如果已经存在一个事务中，加入这个事务中
     */
    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public void addRequiredException(User2 user2){
        user2Mapper.insert(user2);
        throw new RuntimeException();
    }

}
