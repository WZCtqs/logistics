package com.zhkj.lc.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.annotation.Excel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TruckProfit {
    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    private String plateNumber;

    /**
     * 订单数
     */
    @Excel(name = "订单数")
    private int orderSum;

    /**
     * 里程数
     */
    @Excel(name = "里程数")
    private int kilometre;

    /**
     * 应收
     */
    @Excel(name = "应收总费用")
    private BigDecimal receivable;

    /**
     * 应付
     */
    @Excel(name = "应付总费用")
    private BigDecimal expensesPay;

    /**
     * 开票税额
     */
    @Excel(name = "开票税额")
    private BigDecimal rate;

    /**
     * 租户id
     */
    private int tenantId;

    /**
     * 时间检索开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;
    /**
     * 时间检索结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    private BigDecimal needFee;

    private BigDecimal exceptionFee;

    private Integer driverId;

}
