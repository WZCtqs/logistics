package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单管理
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
public class UpStreamOrderExcepFee extends Model<UpStreamOrderExcepFee> {

    private static final long serialVersionUID = 1L;
    /**
     * 上游系统订单id
     */
    @ApiModelProperty(value = "上游系统订单id")
    private String upstreamId;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderId;
    /**
     * 承运单位
     */
    @ApiModelProperty(value = "承运单位")
    private String carrier;
    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String plateNumber;
    /**
     * 压车单价
     */
    @ApiModelProperty(value = "压车单价")
    private BigDecimal ccmCarryPrice;
    /**
     * 压车天数
     */
    @ApiModelProperty(value = "压车天数")
    private Integer ccmCarryDay;
    /**
     * 异常费用
     */
    @ApiModelProperty(value = "异常费用")
    private BigDecimal ccmExceptionFees;
    /**
     * 异常费用备注
     */
    @ApiModelProperty(value = "异常费用备注")
    private String ccmExceptionDetails;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;

    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

}
