package com.zhkj.lc.order.dto;

import com.zhkj.lc.common.annotation.Excel;
import com.zhkj.lc.order.model.entity.OrdExceptionFee;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: HP
 * @Date: 2019/3/6 19:53
 * @Description:
 */
@Data
public class UpdateForNeedPayBill {
    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "公里数")
    private Integer distance;

    @ApiModelProperty(value = "运输费")
    private BigDecimal transportFee;

    @ApiModelProperty(value = "利率")
    private BigDecimal moneyRate;

    @ApiModelProperty(value = "单价")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "异常费用信息")
    List<OrdExceptionFee> exceptionFeeList;

}
