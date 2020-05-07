package com.miaojiaosan.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author miaojiaosan
 * @date 2020/5/6
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigurationApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConfigurationApplication.class, args);
  }
}