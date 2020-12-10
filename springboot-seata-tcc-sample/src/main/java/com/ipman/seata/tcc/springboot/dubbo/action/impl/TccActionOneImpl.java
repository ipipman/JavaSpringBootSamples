package com.ipman.seata.tcc.springboot.dubbo.action.impl;

import com.ipman.seata.tcc.springboot.dubbo.action.ResultHolder;
import com.ipman.seata.tcc.springboot.dubbo.action.TccActionOne;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.stereotype.Service;

/**
 * Created by ipipman on 2020/12/10.
 *
 * @version V1.0
 * @Package com.ipman.seata.tcc.springboot.dubbo.action.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/10 12:03 下午
 */
@Service
public class TccActionOneImpl implements TccActionOne {

    /**
     * 准备
     *
     * @param actionContext
     * @param a
     * @return
     */
    @Override
    public boolean prepare(BusinessActionContext actionContext, int a) {
        String xid = actionContext.getXid();
        System.out.println("TccActionOne prepare, xid:" + xid + ", a:" + a);
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
        System.out.println("TccActionOne commit, xid:" + xid + ", a:" + actionContext.getActionContext("a"));
        ResultHolder.setActionOneResult(xid, "T");
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
        System.out.println("TccActionOne rollback, xid:" + xid + ", a:" + actionContext.getActionContext("a"));
        ResultHolder.setActionOneResult(xid, "R");
        return true;
    }
}
