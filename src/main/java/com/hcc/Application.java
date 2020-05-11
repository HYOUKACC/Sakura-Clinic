package com.hcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication // 配置注解，表明这是springBoot的启动类
@MapperScan("com.hcc.mapper") // 配置注解扫描，把mapper里的类都交给Spring进行管理
public class Application {
    public static void main(String args[]){
        SpringApplication.run(Application.class);
    }
}
