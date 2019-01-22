package com.beidou.entity;

import lombok.Data;
import java.util.Date;

/**
 * 用户模型数据
 *
 * @author ginger
 * @create 2019-01-22 19:11
 */
@Data
public class UserModel {
    /**
     * 自增id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
