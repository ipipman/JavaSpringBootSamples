package com.ipman.sb2accountservice.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by ipipman on 2020/12/9.
 *
 * @version V1.0
 * @Package com.ipman.sb2accountservice.config
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/9 5:30 下午
 */
@Configuration
public class DataSourceConfig {

    /**
     * 数据源配置
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }
}
