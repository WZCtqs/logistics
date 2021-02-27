package com.zhkj.lc.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: wzc
 * @Date: 2018/12/12 08:46
 * @Description:
 */
@Data
public class SysDictVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 编号
     */
    private Integer id;
    /**
     * 数据值
     */
    private String value;
    /**
     * 标签名
     */
    private String label;
    /**
     * 类型
     */
    private String type;
    /**
     * 描述
     */
    private String description;
    /**
     * 排序（升序）
     */
    private BigDecimal sort;
    /**
     * 租户id
     */
    private Integer tenantId;
    /**
     * 删除标志
     */
    private String delFlag;
    /**
     * 备注
     */
    private String remarks;
}
