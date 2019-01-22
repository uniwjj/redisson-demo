package com.beidou;

import com.beidou.entity.UserModel;
import com.beidou.service.DistributedLockService;
import com.beidou.service.RedissonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
@Slf4j
@ComponentScan
@EnableAspectJAutoProxy
@PropertySource("classpath:config.properties")
public class Main {
    public static void main(String[] args) throws Exception {
        springELTest();
    }


    private static void springELTest() throws InterruptedException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Main.class);
        final DistributedLockService lockService = ctx.getBean(DistributedLockService.class);
        UserModel userModel = new UserModel();
        userModel.setName("This is Name.");
        Thread thread1 = new Thread(() -> lockService.springEL(userModel), "thread-1");
        Thread thread2 = new Thread(() -> lockService.springEL(userModel), "thread-2");

        thread1.setDaemon(true);
        thread2.setDaemon(true);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        ((AnnotationConfigApplicationContext) ctx).close();
    }


    private static void readWriteLockTest() throws InterruptedException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Main.class);
        final DistributedLockService lockService = ctx.getBean(DistributedLockService.class);
        Thread threadWrite = new Thread(() -> lockService.readLock("testParam"), "thread-write");
        Thread threadRead = new Thread(() -> lockService.writeLock("testParam"), "thread-read");

        threadWrite.setDaemon(true);
        threadRead.setDaemon(true);
        threadWrite.start();
        threadRead.start();
        threadWrite.join();
        threadRead.join();
        ((AnnotationConfigApplicationContext) ctx).close();
    }


    private static void simpleParamTest() throws InterruptedException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Main.class);
        final DistributedLockService lockService = ctx.getBean(DistributedLockService.class);
        Thread thread1 = new Thread(() -> lockService.simpleParam("testParam"), "thread-1");
        Thread thread2 = new Thread(() -> lockService.simpleParam("testParam"), "thread-2");
        Thread thread3 = new Thread(() -> lockService.simpleParam("testParam3"), "thread-3");

        thread1.setDaemon(true);
        thread2.setDaemon(true);
        thread3.setDaemon(true);
        thread1.start();
        thread2.start();
        thread3.start();
        thread1.join();
        thread2.join();
        thread3.join();
        ((AnnotationConfigApplicationContext) ctx).close();
    }


    private static void simpleNameTest() throws InterruptedException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Main.class);
        final DistributedLockService lockService = ctx.getBean(DistributedLockService.class);
        Thread thread1 = new Thread(() -> {
            lockService.simpleName("testName");
            lockService.simpleName("testName");
        }, "thread-1");
        Thread thread2 = new Thread(() -> lockService.simpleName("testName"), "thread-2");

        thread1.setDaemon(true);
        thread2.setDaemon(true);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        ((AnnotationConfigApplicationContext) ctx).close();
    }


    private static void redissonTest() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Main.class);
        RedissonService redissonService = ctx.getBean(RedissonService.class);
        redissonService.setEx("king", "ginger", 100, TimeUnit.SECONDS);
        String king = redissonService.get("king");
        log.info(king);
        ((AnnotationConfigApplicationContext) ctx).close();
    }
}
