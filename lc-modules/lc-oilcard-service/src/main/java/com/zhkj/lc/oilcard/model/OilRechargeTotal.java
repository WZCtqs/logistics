package com.zhkj.lc.oilcard.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: HP
 * @Date: 2019/4/24 10:49
 * @Description:
 */
@Data
public class OilRechargeTotal {
    /**
     * 油卡id
     */
    private Integer oilCardId;
    /**
     * 主卡id
     */
    private Integer majorId;
    /**
     * 油卡号
     */
    private String oilNumber;
    /**
     * 所属车辆id
     */
    private Integer truckId;
    /**
     * 车牌号
     */
    private String plateNumber;
    /**
     * 所属车辆类型
     */
    private String attribute;
    /**
     * 司机id
     */
    private Integer driverId;
    /**
     * 司机姓名
     */
    private String driverName;
    /**
     * 司机联系方式
     */
    private String driverPhone;
    /**
     * 充值金额
     */
    private BigDecimal rechargeSum;
    /**
     * 油卡类型
     */
    private String cardType;
    /**
     * 租户id
     */
    private Integer tenantId;
    /**
     * 充值时间
     */
    private String rechargeDate;
    /**
     * 申请备注
     */
    private String applyRemark;
    /**
     * 查询详细
     */
    private String isSelectSub;

}
