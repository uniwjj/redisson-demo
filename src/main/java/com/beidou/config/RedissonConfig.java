package com.beidou.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置
 *
 * @author ginger
 * @create 2019-01-22 14:25
 */
@Slf4j
@Configuration
// @ConfigurationProperties(prefix="spring.redisson")
public class RedissonConfig {
    @Value("${spring.redisson.address}")
    private String address;
    @Value("${spring.redisson.thread}")
    private int thread;
    @Value("${spring.redisson.codec}")
    private String codec;
    @Value("${spring.redisson.connectionMinimumIdleSize}")
    private int connectionMinimumIdleSize;
    @Value("${spring.redisson.idleConnectionTimeout}")
    private int idleConnectionTimeout;
    @Value("${spring.redisson.connectTimeout}")
    private int connectTimeout;
    @Value("${spring.redisson.timeout}")
    private int timeout;
    @Value("${spring.redisson.retryAttempts}")
    private int retryAttempts;
    @Value("${spring.redisson.retryInterval}")
    private int retryInterval;
    @Value("${spring.redisson.password}")
    private String password;
    @Value("${spring.redisson.connectionPoolSize}")
    private int connectionPoolSize;
    @Value("${spring.redisson.database}")
    private int database;


    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonSingle() {
        Config config = new Config();
        config.useSingleServer().setAddress(address)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setConnectionPoolSize(connectionPoolSize)
                .setDatabase(database)
                .setRetryAttempts(retryAttempts)
                .setRetryInterval(retryInterval)
                .setTimeout(timeout)
                .setConnectTimeout(connectTimeout)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setPassword(password);
        config.setThreads(thread);
        // 指定编码方式，采用String编码
        config.setCodec(new StringCodec());
        // The caller is responsible for closing the EventLoopGroup.
        // 如果设置了EventLoopGroup，需要设置者关闭EventLoopGroup，
        // shutdown方法只会关闭系统创建的部分
        // config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }
}
