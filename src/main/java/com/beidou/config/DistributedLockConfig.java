package com.beidou.config;

import com.beidou.annotation.LockAction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 分布式锁配置
 *
 * @author ginger
 * @create 2019-01-22 17:10
 */
@Slf4j
@Aspect
@Configuration
// @ConditionalOnBean(RedissonClient.class)
// @AutoConfigureAfter(RedissonAutoConfiguration.class)
public class DistributedLockConfig {
    @Autowired
    private RedissonClient redissonClient;

    final private ExpressionParser parser = new SpelExpressionParser();

    final private LocalVariableTableParameterNameDiscoverer discoverer =
            new LocalVariableTableParameterNameDiscoverer();

    @Pointcut("@annotation(com.beidou.annotation.LockAction)")
    private void lockPoint() {
    }

    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        LockAction lockAction = method.getAnnotation(LockAction.class);
        String key = lockAction.value();
        Object[] args = pjp.getArgs();
        key = parse(key, method, args);

        RLock lock = getLock(key, lockAction);
        if (!lock.tryLock(lockAction.waitTime(), lockAction.leaseTime(), lockAction.unit())) {
            log.error("get lock error [{}]", key);
            return null;
        }

        // 得到锁,执行方法，释放锁
        log.info("get lock success [{}]", key);
        try {
            return pjp.proceed();
        } catch (Exception e) {
            log.error("execute locked method occurs an exception", e);
        } finally {
            lock.unlock();
            log.info("release lock [{}]", key);
        }
        return null;
    }

    private String parse(String key, Method method, Object[] args) {
        String[] params = discoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }

    private RLock getLock(String key, LockAction lockAction) {
        switch (lockAction.lockType()) {
            case REENTRANT_LOCK:
                return redissonClient.getLock(key);
            case FAIR_LOCK:
                return redissonClient.getFairLock(key);
            case READ_LOCK:
                return redissonClient.getReadWriteLock(key).readLock();
            case WRITE_LOCK:
                return redissonClient.getReadWriteLock(key).writeLock();
            default:
                throw new RuntimeException("unsupported lock type [" + lockAction.lockType().name() + "]");
        }
    }

}
