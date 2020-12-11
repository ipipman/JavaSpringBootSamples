package com.ipman.seata.tcc.springboot.dubbo.service;

import com.ipman.seata.tcc.springboot.dubbo.action.TccActionOne;
import com.ipman.seata.tcc.springboot.dubbo.action.TccActionTwo;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ipipman on 2020/12/10.
 *
 * @version V1.0
 * @Package com.ipman.seata.tcc.springboot.dubbo.service
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/10 12:13 下午
 */
@Service
public class TccTransactionService {

    @Autowired
    private TccActionOne tccActionOne;

    @Autowired
    private TccActionTwo tccActionTwo;

    /**
     * 分布式事务提交demo
     *
     * @return
     */
    @GlobalTransactional
    public String doTransactionCommit() {
        //第一个TCC 事务参与者
        boolean result = tccActionOne.prepare(null, 1);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        List<String> list = new ArrayList<>();
        list.add("c1");
        list.add("c2");

        //第二个TCC 事务参与者
        result = tccActionTwo.prepare(null, "two", list);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }

        //此时两个TCC参与者的prepare方法都执行成功了，TC就会触发这两个TCC参与者的commit方法进行提交
        return RootContext.getXID();
    }

    /**
     * 分布式事务回滚demo
     *
     * @param map
     * @return
     */
    @GlobalTransactional
    public String doTransactionRollback(Map<String, String> map) {
        //第一个TCC 事务参与者
        boolean result = tccActionOne.prepare(null, 1);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        List<String> list = new ArrayList<>();
        list.add("c1");
        list.add("c2");

        //第二个TCC 事务参与者
        result = tccActionTwo.prepare(null, "two", list);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }
        map.put("xid", RootContext.getXID());

        //这里故意抛出异常，TC会触发这两个TCC参与者的rollback方法进行回滚
        throw new RuntimeException("transacton rollback");
    }


}
