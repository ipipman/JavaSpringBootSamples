package com.ipman.springboot.transactional.sample.core.service;

import com.ipman.springboot.transactional.sample.common.domain.model.User2;

/**
 * Created by ipipman on 2021/4/2.
 *
 * @version V1.0
 * @Package com.ipman.springboot.transactional.sample.core.service
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/4/2 11:50 上午
 */
public interface User2Service {


    /**
     *  Propagation.REQUIRED
     *  如果当前没有事务，就创建一个事务
     *  如果已经存在一个事务中，加入这个事务中
     */
    void addRequired(User2 user2);

    /**
     *  Propagation.REQUIRED
     *  如果当前没有事务，就创建一个事务
     *  如果已经存在一个事务中，加入这个事务中
     */
    void addRequiredException(User2 user2);
}
