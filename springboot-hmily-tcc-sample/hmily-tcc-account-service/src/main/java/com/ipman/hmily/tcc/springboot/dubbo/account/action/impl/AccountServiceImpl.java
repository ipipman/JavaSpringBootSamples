package com.ipman.hmily.tcc.springboot.dubbo.account.action.impl;

import com.ipman.hmily.tcc.springboot.dubbo.account.action.AccountService;
import com.ipman.hmily.tcc.springboot.dubbo.account.entity.Account;
import com.ipman.hmily.tcc.springboot.dubbo.account.mapper.AccountMapper;
//import org.dromara.hmily.annotation.HmilyTCC;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Created by ipipman on 2020/12/10.
 *
 * @version V1.0
 * @Package com.ipman.hmily.tcc.springboot.dubbo.account.action.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/10 3:49 下午
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountMapper accountMapper;

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public void payment(String userId, BigDecimal num) {
        System.out.println("payment = " + userId + "， " + num);
        Account account = accountMapper.selectByUserId(userId);
        account.setMoney(account.getMoney().subtract(num));
        accountMapper.updateById(account);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean confirm(String userId, BigDecimal num) {
        System.out.println("confirm = " + userId + "， " + num);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(String userId, BigDecimal num) {
        System.out.println("confirm = " + userId + "， " + num);
        return true;
    }

}
