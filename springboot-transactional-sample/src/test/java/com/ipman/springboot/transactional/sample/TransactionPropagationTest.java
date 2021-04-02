package com.ipman.springboot.transactional.sample;

import com.ipman.springboot.transactional.sample.common.domain.model.User1;
import com.ipman.springboot.transactional.sample.common.domain.model.User2;
import com.ipman.springboot.transactional.sample.core.service.User1Service;
import com.ipman.springboot.transactional.sample.core.service.User2Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionPropagationTest {

    @Autowired
    private User1Service user1Service;

    @Autowired
    private User2Service user2Service;

    /**
     * Propagation.REQUIRED
     * 如果当前没有事务，就创建一个事务
     * 如果已经存在一个事务中，加入这个事务中
     * 总结：
     * 外围方法没有开启事务，插入"张三"、"李四"方法在自己事务中独立运行
     * 外围方法异常不影响内部插入"张三"、"李四"方法独立的事务
     */
    @Test
    public void transactionPropagationRequired() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addRequired(user2);

        throw new RuntimeException();
    }

    /**
     * Propagation.REQUIRED
     * 如果当前没有事务，就创建一个事务
     * 如果已经存在一个事务中，加入这个事务中
     * 总结：
     * 外围方法没有开启事务，插入"张三"、"李四"方法在自己事务中独立运行
     * 所以插入"李四"方法抛出异常只会回滚插入"李四"的方法，插入"张三"的方法不受影响
     */
    @Test
    public void transactionPropagationRequiredException() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addRequiredException(user2);
    }

}
