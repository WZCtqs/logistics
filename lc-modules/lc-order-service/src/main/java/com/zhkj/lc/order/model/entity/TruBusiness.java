package com.zhkj.lc.order.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel(value = "车辆业务统计")
@Data
public class TruBusiness {

    @ApiModelProperty(value = "车辆id")
    private Integer truckId;

    @ApiModelProperty(value = "车牌号")
    private String plateNumber;

    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    @ApiModelProperty(value = "司机手机号")
    private String driverPhone;

    @ApiModelProperty(value = "本月订单数")
    private Integer orderNum;

    @ApiModelProperty(value = "本月费用合计")
    private BigDecimal totalFee;


}
