package com.beidou;

import com.beidou.service.RedissonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
@Slf4j
@ComponentScan
@PropertySource("classpath:config.properties")
public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Main.class);
        RedissonService redissonService = ctx.getBean(RedissonService.class);
        redissonService.setEx("king", "ginger", 100, TimeUnit.SECONDS);
        String king = redissonService.get("king");
        log.info(king);
        ((AnnotationConfigApplicationContext) ctx).close();
    }
}
