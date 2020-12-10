package com.ipman.sb2accountservice.service;

import com.ipman.sb2accountservice.entity.Account;
import com.ipman.sb2accountservice.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by ipipman on 2020/12/9.
 *
 * @version V1.0
 * @Package com.ipman.sb2accountservice.service
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/9 5:52 下午
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AccountService {

    private static final String ERROR_USER_ID = "1002";

    private final AccountMapper accountMapper;

    public void debit(String userId, BigDecimal num) {
        Account account = accountMapper.selectByUserId(userId);
        account.setMoney(account.getMoney().subtract(num));
        accountMapper.updateById(account);

        if (ERROR_USER_ID.equals(userId)) {
            throw new RuntimeException("account branch exception");
        }
    }

}