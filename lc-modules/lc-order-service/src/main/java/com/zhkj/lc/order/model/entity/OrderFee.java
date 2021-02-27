package com.zhkj.lc.order.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 订单的应付费用统计
 */
@Data
@ApiModel(value = "订单费用统计")
public class OrderFee {
    private String plateNumber;

    private Integer driverId;

    /**
     * 应付费用
     */
    private BigDecimal truckFee;
    /**
     * 异常费用
     */
    private BigDecimal exFee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderFee orderFee = (OrderFee) o;
        return Objects.equals(plateNumber, orderFee.plateNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(plateNumber);
    }
}
