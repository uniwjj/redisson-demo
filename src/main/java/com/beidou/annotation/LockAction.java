package com.beidou.annotation;

import com.beidou.constant.LockType;
import org.springframework.core.annotation.AliasFor;
import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 锁行为
 *
 * @author ginger
 * @create 2019-01-22 17:01
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LockAction {
    /**
     * 锁资源，支持Spring EL表达式
     */
    @AliasFor("key")
    String value() default "'default'";

    /**
     * 锁资源，支持Spring EL表达式
     */
    @AliasFor("value")
    String key() default "'default'";

    /**
     * 锁类型
     */
    LockType lockType() default LockType.REENTRANT_LOCK;

    /**
     * 获取锁的等待时间，默认3秒
     */
    long waitTime() default 3000L;

    /**
     * 锁自动释放时间，默认30秒
     */
    long leaseTime() default 30000L;

    /**
     * 时间单位
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
