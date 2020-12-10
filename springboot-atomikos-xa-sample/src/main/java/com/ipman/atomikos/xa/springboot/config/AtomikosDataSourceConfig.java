package com.ipman.atomikos.xa.springboot.config;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.jta.JtaTransactionManager;

import java.util.Properties;

/**
 * Created by ipipman on 2020/12/8.
 */
@Configuration
public class AtomikosDataSourceConfig {

    @Value("${db1.datasource.url}")
    private String db1Url;

    @Value("${db2.datasource.url}")
    private String db2Url;

    @Value("${db1.datasource.username}")
    private String db1UserName;

    @Value("${db2.datasource.username}")
    private String db2UserName;

    private final static String XA_DS_CLASS_NAME = "com.mysql.cj.jdbc.MysqlXADataSource";

    /**
     * 第一个数据库
     *
     * @return
     */
    @Bean(name = "db1DataSource")
    @Primary
    @Qualifier("db1DataSource")
    public AtomikosDataSourceBean db1DataSourceBean() {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setUniqueResourceName("db1DataSource");
        atomikosDataSourceBean.setXaDataSourceClassName(XA_DS_CLASS_NAME);
        Properties properties = new Properties();
        properties.put("URL", db1Url);
        properties.put("user", db1UserName);
        properties.put("password", "");
        atomikosDataSourceBean.setXaProperties(properties);
        return atomikosDataSourceBean;
    }

    /**
     * 第二个数据库
     *
     * @return
     */
    @Bean(name = "db2DataSource")
    @Qualifier("db2DataSource")
    public AtomikosDataSourceBean db2DataSourceBean() {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setUniqueResourceName("db2DataSource");
        atomikosDataSourceBean.setXaDataSourceClassName(XA_DS_CLASS_NAME);
        Properties properties = new Properties();
        properties.put("URL", db2Url);
        properties.put("user", db2UserName);
        properties.put("password", "");
        atomikosDataSourceBean.setXaProperties(properties);
        return atomikosDataSourceBean;
    }

    @Bean(destroyMethod = "close", initMethod = "init")
    public UserTransactionManager userTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(true);
        return userTransactionManager;
    }

    /**
     * 定义XA事务管理器（TM）
     *
     * @return
     */
    @Bean(name = "XATransactionManager")
    public JtaTransactionManager jtaTransactionManager() {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager());
        return jtaTransactionManager;
    }
}
