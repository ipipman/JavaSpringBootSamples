package com.ipman.sharding.jdbc.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import io.shardingjdbc.core.api.config.MasterSlaveRuleConfiguration;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ipipman on 2020/12/2.
 *
 * @version V1.0
 * @Package com.ipman.mysql.shardingspherejdbc.config
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/2 7:56 下午
 */
@Configuration
public class ShardingSphereDataSource {

    //主
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "shardingsphere.master.datasource")
    public DataSource masterDataSource() {
        return new DruidDataSource();
    }

    //从1
    @Bean(name = "slave1DataSource")
    @ConfigurationProperties(prefix = "shardingsphere.slave1.datasource")
    public DataSource slave1DataSource() {
        return new DruidDataSource();
    }

    //从2
    @Bean(name = "slave2DataSource")
    @ConfigurationProperties(prefix = "shardingsphere.slave2.datasource")
    public DataSource slave2DataSource() {
        return new DruidDataSource();
    }


    //sharding proxy
    @Bean(name = "proxyDataSource")
    @ConfigurationProperties(prefix = "shardingsphere.proxy.datasource")
    public DataSource proxyDataSource() {
        return new DruidDataSource();
    }

    //配置主从数据源路由
    @Bean(name = "shardingDataSource")
    @SneakyThrows
    public DataSource shardingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                        @Qualifier("slave1DataSource") DataSource slave1DataSource,
                                        @Qualifier("slave2DataSource") DataSource slave2DataSource) {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("masterDataSource", masterDataSource);
        dataSourceMap.put("slaveDataSource0", slave1DataSource);
        dataSourceMap.put("slaveDataSource1", slave2DataSource);

        // 构建读写分离配置
        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration();
        masterSlaveRuleConfig.setName("ms_ds");
        masterSlaveRuleConfig.setMasterDataSourceName("masterDataSource");
        masterSlaveRuleConfig.getSlaveDataSourceNames().add("slaveDataSource0");
        masterSlaveRuleConfig.getSlaveDataSourceNames().add("slaveDataSource1");

        final DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, masterSlaveRuleConfig, new HashMap<>());
        return dataSource;

    }
}
