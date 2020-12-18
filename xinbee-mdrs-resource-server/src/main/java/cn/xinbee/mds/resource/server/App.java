package cn.xinbee.mdrs.resource.server;

import cn.xinbee.mdrs.service.EntityService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching(order = 0)
@EnableConfigurationProperties
@EnableTransactionManagement
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Configuration
    @EnableTransactionManagement
    @EnableJpaRepositories(basePackages = "cn.xinbee.mdrs.data.jpa.repository")
    @EnableJpaAuditing
    @EntityScan(basePackages = "cn.xinbee.mdrs.data.jpa.domain")
    static class JpaConfig {

    }

    @Configuration
    @ComponentScan(basePackageClasses = {EntityService.class})
    static class ServiceConfig {

    }
}