package com.beidou.service;

import com.beidou.annotation.LockAction;
import com.beidou.constant.LockType;
import com.beidou.entity.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 分布式锁业务层
 *
 * @author ginger
 * @create 2019-01-22 17:21
 */
@Slf4j
@Service
public class DistributedLockService {
    @LockAction("'expression'")
    public void simpleName(String param) {
        log.info("enter simpleName method, lock name is [expression]");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}
        log.info("leave simpleName method, lock name is [expression]");
    }

    @LockAction("#param")
    public void simpleParam(String param) {
        log.info("enter simpleParam method, lock name is [{}]", param);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}
        log.info("leave simpleParam method, lock name is [{}]", param);
    }


    @LockAction(value = "#param", lockType = LockType.WRITE_LOCK)
    public void writeLock(String param) {
        log.info("enter writeLock method, lock name is [{}]", param);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}
        log.info("leave writeLock method, lock name is [{}]", param);
    }


    @LockAction(value = "#param", lockType = LockType.READ_LOCK)
    public void readLock(String param) {
        log.info("enter readLock method, lock name is [{}]", param);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}
        log.info("leave readLock method, lock name is [{}]", param);
    }


    @LockAction("#userModel.name")
    public void springEL(UserModel userModel) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}
    }
}
