package com.zhkj.lc.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.annotation.Excel;
import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.order.model.entity.OrdExceptionFee;
import com.zhkj.lc.order.model.entity.OrderSettlement;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderProfit {
    /**
     * 订单号
     */
    @Excel(name = "订单号")
    private String orderId;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称")
    private String customerName;

    private Integer customerId;

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
     * 开票税率
     */
    @Excel(name = "开票税务金额")
    private BigDecimal rate;

    /*税率*/
    private BigDecimal payRate;

    /**
     * 租户id
     */
    private Integer tenantId;

    /*车牌号*/
    private String plateNumber;

    /**
     * 应收是否开票
     */
    private String isInvoice;

    /**
     * 应付是否开票
     */
    private String isYfInvoice;

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

    /*应付费用*/
    private BigDecimal needFee;

    /*应付异常*/
    private BigDecimal exceptionFee;

    /*应付压车异常*/
    private BigDecimal ycFee = new BigDecimal(0);

    /*应收异常*/
    private BigDecimal exYsFee;

    /*司机id*/
    private Integer driverId;

    /**/
    private BigDecimal recFee;

    private List<CustomerVO> list;

    /** 搜索条件日期类型 */
    private String timeType;

    /**车辆类型*/
    private String truckAttribute;

    private OrderSettlement orderSettlementStatus;

    private List<OrdExceptionFee> exceptionFeeList;

}
