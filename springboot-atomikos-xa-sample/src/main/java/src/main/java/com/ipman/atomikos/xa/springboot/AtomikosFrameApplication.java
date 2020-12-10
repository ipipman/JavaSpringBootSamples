package com.ipman.atomikos.xa.springboot;

import com.ipman.atomikos.xa.springboot.service.XATransactionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AtomikosFrameApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AtomikosFrameApplication.class, args);
        XATransactionService transactionService = (XATransactionService) applicationContext.getBean("XATransactionService");

        //正确的SQL
        String errorSQL = null;
        transactionService.run(errorSQL);

        //错误的SQL，会触发当前XA事务回滚（因为demo1表并不存在）
        errorSQL = "insert into `demo1`(id, `name`) values" +
                "(?, ?)";
        transactionService.run(errorSQL);
    }

}
