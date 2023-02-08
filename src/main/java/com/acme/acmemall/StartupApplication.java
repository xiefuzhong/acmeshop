package com.acme.acmemall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.acme.acmemall.dao"})
public class StartupApplication {

  public static void main(String[] args) {
    SpringApplication.run(StartupApplication.class, args);
  }
}
