package com.company.business;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@Slf4j
@EnableEurekaClient
@SpringBootApplication
@ComponentScan("com.company")
@MapperScan("com.company.server.mapper")
public class BusinessApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BusinessApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info("启动成功");
        log.info("Business地址:\thttp://127.0.0.1:{}",env.getProperty("server.port"));
    }
}
