package com.ipman.hmily.tcc.springboot.dubbo.account.action;

import java.math.BigDecimal;

/**
 * Created by ipipman on 2020/12/10.
 *
 * @version V1.0
 * @Package com.ipman.hmily.tcc.springboot.dubbo.account.action
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/10 3:49 下午
 */
public interface AccountService {

    //支付
    public void payment(String userId, BigDecimal num);
}
