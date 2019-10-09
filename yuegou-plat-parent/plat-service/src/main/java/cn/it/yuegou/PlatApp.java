package cn.it.yuegou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 平台服务启动类
 * @author yinjunchi
 */
@SpringBootApplication
@EnableEurekaClient
public class PlatApp {
    public static void main(String[] args) {
        SpringApplication.run(PlatApp.class, args);
    }
}
