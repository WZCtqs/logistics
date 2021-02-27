package com.zhkj.lc.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: HP
 * @Date: 2018/12/12 18:01
 * @Description:
 */
@Data
public class TruckVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车辆id
     */
    private Integer truckId;
    /**
     * 车辆状态
     */
    private Integer status;
    /**
     * 车牌号
     */
    private String plateNumber;
    /**
     * 车辆种类
     */
    private String type;
    /**
     * 车主
     */
    private String truckOwner;
    /**
     * 车主手机号
     */
    private String truckOwnerPhone;
    /**
     * 开户行
     */
    private String depositBank;
    /**
     * 银行账号
     */
    private String truckCardNumber;
    /**
     * 车架号
     */
    private String carframeNumber;
    /**
     * 营运号
     */
    private String operationNumber;
    /**
     * 发票号
     */
    private String invoiceNumber;
    /**
     * 保险单号
     */
    private String policyNo;
    /**
     * 车辆品牌
     */
    private String brandNo;
    /**
     * 车辆类型
     */
    private String attribute;
    /**
     * 发动机号
     */
    private String engineNumber;
    /**
     * 行驶证
     */
    private String drivingLicense;
    /**
     * 是否监管（0：监管，2：否）
     */
    private String isSupervise;

    /**
     * 车型
     */
    private String carAttribute;

    /**
     * 车长
     */
    private String carLength;

    private Integer tenantId;

    private Integer truckTeamId;

    private String isTrust;
}
