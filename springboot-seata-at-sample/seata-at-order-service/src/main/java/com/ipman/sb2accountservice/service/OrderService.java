package com.ipman.sb2accountservice.service;

import com.ipman.sb2accountservice.client.AccountClient;
import com.ipman.sb2accountservice.entity.Order;
import com.ipman.sb2accountservice.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OrderService {

    private final AccountClient accountClient;

    private final OrderMapper orderMapper;

    public void create(String userId, String commodityCode, Integer count) {
        BigDecimal orderMoney = new BigDecimal(count).multiply(new BigDecimal(5));
        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(count);
        order.setMoney(orderMoney);

        orderMapper.insert(order);
        accountClient.debit(userId, orderMoney);

    }
}