package com.zhkj.lc.oilcard.model;

import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TruckOilRecharge {

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
     * 充值总金额
     */
    private BigDecimal sumRechargeSum;
    /**
     * 油卡类型
     */
    private String cardType;
    /**
     * 租户id
     */
    private Integer tenantId;

}
