package com.ipman.seata.tcc.springboot.dubbo.action.impl;

import com.ipman.seata.tcc.springboot.dubbo.action.ResultHolder;
import com.ipman.seata.tcc.springboot.dubbo.action.TccActionTwo;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ipipman on 2020/12/10.
 *
 * @version V1.0
 * @Package com.ipman.seata.tcc.springboot.dubbo.action.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/10 12:07 下午
 */
@Service
public class TccActionTwoImpl implements TccActionTwo {

    /**
     * 准备阶段
     *
     * @param actionContext
     * @param b
     * @param list
     * @return
     */
    @Override
    public boolean prepare(BusinessActionContext actionContext, String b, List list) {
        String xid = actionContext.getXid();
        System.out.println("TccActionTwo prepare, xid:" + xid + ", b:" + b + ", c:" + list.get(1));
        return true;
    }

    /**
     * 提交
     *
     * @param actionContext
     * @return
     */
    @Override
    public boolean commit(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println("TccActionTwo commit, xid:" + xid + ", b:" + actionContext.getActionContext("b") + ", c:" + actionContext.getActionContext("c"));
        ResultHolder.setActionTwoResult(xid, "T");
        return true;
    }

    /**
     * 回滚
     *
     * @param actionContext
     * @return
     */
    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println("TccActionTwo rollback, xid:" + xid  + ", b:" + actionContext.getActionContext("b") + ", c:" + actionContext.getActionContext("c"));
        ResultHolder.setActionTwoResult(xid, "R");
        return true;
    }
}
