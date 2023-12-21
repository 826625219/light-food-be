package com.qsj.qsjMain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableScheduling
@MapperScan("com.qsj.qsjMain.model.mapper")
public class QsjMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(QsjMainApplication.class, args);
    }

}
