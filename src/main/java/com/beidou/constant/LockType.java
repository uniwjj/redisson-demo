package com.beidou.constant;

/**
 * 锁类型
 *
 * @author ginger
 * @create 2019-01-22 17:04
 */
public enum LockType {
    /**
     * 可重入锁
     */
    REENTRANT_LOCK,

    /**
     * 公平锁
     */
    FAIR_LOCK,

    /**
     * 读锁
     */
    READ_LOCK,

    /**
     * 写锁
     */
    WRITE_LOCK
}
