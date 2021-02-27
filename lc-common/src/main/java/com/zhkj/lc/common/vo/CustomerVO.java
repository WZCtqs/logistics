package com.zhkj.lc.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: wzc
 * @Date: 2018/12/10 09:59
 * @Description:
 */
@Data
public class CustomerVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 客户id
     */
    private Integer customerId;
    /**
     * 客户名字
     */
    private String customerName;
    /**
     * 业务员
     */
    private String saleman;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 职位
     */
    private String contactJob;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 联系人邮箱
     */
    private String email;
    /**
     * 是否通过（0：通过，1：不通过）
     */
    private String isPassed;
    /**
     * 是否合作（0：合作，1：非合作）
     */
    private String isPartner;
    /**
     * 客户状态
     */
    private String status;
    /**
     * 是否失信（0：失信，1：信任）
     */
    private String isTrust;
    /**
     * 备注
     */
    private String remark;

    private Integer tenantId;

    private String payWay;

    private String delFlag;
}
