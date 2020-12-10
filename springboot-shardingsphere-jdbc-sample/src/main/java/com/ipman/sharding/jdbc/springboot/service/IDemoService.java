package com.ipman.sharding.jdbc.springboot.service;

/**
 * Created by ipipman on 2020/12/2.
 *
 * @version V1.0
 * @Package com.ipman.mysql.shardingspherejdbc.service
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/2 7:50 下午
 */
public interface IDemoService {

    //测试读写分离
    void testMasterSlave();

    //测试主从事务
    void testTransaction();
}
