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
     * 发起分布式事务
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
        result = tccActionTwo.prepare(null, "two", list);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }
        return RootContext.getXID();
    }

    /**
     * 回滚分布式事务
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
        result = tccActionTwo.prepare(null, "two", list);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }
        map.put("xid", RootContext.getXID());

        //这里故意抛出异常
        throw new RuntimeException("transacton rollback");
    }


}
