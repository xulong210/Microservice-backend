package cn.xu.eduorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients  //远程调用
@EnableDiscoveryClient //注册进nacos
@ComponentScan(basePackages = {"cn.xu"})
@MapperScan("cn.xu.eduorder.mapper")
public class OrdersApplication {

    public static void main(String[] args){
        SpringApplication.run(OrdersApplication.class,args);
    }
}
