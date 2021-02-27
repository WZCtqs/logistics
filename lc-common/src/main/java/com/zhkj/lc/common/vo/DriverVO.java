package com.zhkj.lc.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: wzc
 * @Date: 2018/12/8 09:48
 * @Description:
 */
@Data
public class DriverVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 运费油卡余额
     */
    private BigDecimal freightOilcardAmount;
    /**
     * 司机id
     */
    private Integer driverId;

    /**
     * 所属车队id
     */
    private Integer truckTeamId;
    /**
     * 所属车队名称
     */
    private String teamName;

    /**
     * 司机姓名
     */
    private String driverName;
    /**
     * 司机状态
     * 请假,在途,空闲
     */
    private String status;
    /**
     * 是否是车主
     */
    private String isOwner;
    /**
     * 司机年龄
     */
    private String driverAge;
    /**
     * 司机驾龄
     */
    private String driveYears;
    /**
     * 身份证号
     */
    private String idcardNumber;
    /**
     * 司机手机号
     */
    private String phone;
    /**
     * 驾证级别
     */
    private String licenseLevel;
    /**
     * 银行卡号
     */
    private String bankNumber;
    /**
     * 所属银行
     */
    private String depositBank;
    /**
     * 车辆id
     */
    private Integer plateId;
    /**
     * 车牌号
     */
    private String plateNumber;

    private Integer tenantId;

    private Integer[] driverIds;

    private String payWay;

    private TruckVO truTruck;

    private String teamType;

    private String isTrust;

    private String delFlag;
}
