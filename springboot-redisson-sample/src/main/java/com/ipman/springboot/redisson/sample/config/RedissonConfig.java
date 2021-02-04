package com.ipman.springboot.redisson.sample.config;


import lombok.Getter;
import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ipipman on 2021/1/28.
 *
 * @version V1.0
 * @Package com.ipman.springboot.redisson.sample.config
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/1/28 6:09 下午
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redisson")
@Setter
@Getter
public class RedissonConfig {

    //redis链接地址
    private String address;
    //最小空闲连接数,默认值:10,最小保持连接数（长连接）
    private int connectionMinimumIdleSize;
    //连接空闲超时，单位：毫秒 默认10000;当前连接池里的连接数量超过了最小空闲连接数，
    //而连接空闲时间超过了该数值，这些连接将会自动被关闭，并从连接池里去掉
    private int idleConnectionTimeout;
    //ping节点超时,单位：毫秒,默认1000
    private int pingTimeout;
    //连接等待超时,单位：毫秒,默认10000
    private int connectTimeout;
    //命令等待超时,单位：毫秒,默认3000；等待节点回复命令的时间。该时间从命令发送成功时开始计时
    private int timeout;
    //命令失败重试次数，默认值:3
    private int retryAttempts;
    //命令重试发送时间间隔，单位：毫秒,默认值:1500
    private int retryInterval;
    //重新连接时间间隔，单位：毫秒,默认值：3000;连接断开时，等待与其重新建立连接的时间间隔
    private int reconnectionTimeout;
    //执行失败最大次数, 默认值：3；失败后直到 reconnectionTimeout超时以后再次尝试。
    private int failedAttempts;
    //身份验证密码
    private String password;
    //单个连接最大订阅数量，默认值：5
    private int subscriptionsPerConnection;
    //客户端名称
    private String clientName;
    //长期保持一定数量的发布订阅连接是必须的
    private int subscriptionConnectionMinimumIdleSize;
    //发布和订阅连接池大小，默认值：50
    private int subscriptionConnectionPoolSize;
    //发布和订阅连接池大小，默认值：50
    private int connectionPoolSize;
    //数据库编号，默认值：0
    private int database;
    //是否启用DNS监测，默认值：false
    private boolean dnsMonitoring;
    //DNS监测时间间隔，单位：毫秒，默认值：5000
    private int dnsMonitoringInterval;
    //当前处理核数量 * 2
    private int thread;

    @Bean
    public RedissonClient redisson() throws Exception {
        System.out.println(address);
        Config config = new Config();
        config.useSingleServer()
                .setAddress(address)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setConnectionPoolSize(connectionPoolSize)
                .setDatabase(database)
                .setDnsMonitoring(dnsMonitoring)
                .setDnsMonitoringInterval(dnsMonitoringInterval)
                .setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
                .setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
                .setSubscriptionsPerConnection(subscriptionsPerConnection)
                .setClientName(clientName)
                .setFailedAttempts(failedAttempts)
                .setRetryAttempts(retryAttempts)
                .setRetryInterval(retryInterval)
                .setReconnectionTimeout(reconnectionTimeout)
                .setTimeout(timeout)
                .setConnectTimeout(connectTimeout)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setPingTimeout(pingTimeout)
                .setPassword(password);

        return Redisson.create(config);
    }
}
