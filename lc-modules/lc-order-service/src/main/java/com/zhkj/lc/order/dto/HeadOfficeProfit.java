package com.zhkj.lc.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.annotation.Excel;
import com.zhkj.lc.common.vo.DriverVO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Data
public class HeadOfficeProfit {
    /**
     * 收入-运输费
     */
    @Excel(name = "收入-运输费")
    private BigDecimal recTransportFee;
    /**
     * 收入-提货费
     */
    @Excel(name = "收入-提货费")
    private BigDecimal recPickFee;
    /**
     * 收入-装货费
     */
    @Excel(name = "收入-装货费")
    private BigDecimal recPackFee;
    /**
     * 收入-卸货费
     */
    @Excel(name = "收入-卸货费")
    private BigDecimal recReleaseFee;
    /**
     * 收入-提箱费
     */
    @Excel(name = "收入-提箱费")
    private BigDecimal recPickcnFee;
    /**
     * 收入-异常费用
     */
    @Excel(name = "收入-异常费用")
    private BigDecimal recExceptionFee;

    /**
     * 支出-承运商-运输费
     */
    @Excel(name = "支出-承运商-运输费")
    private BigDecimal payTeamTransportFee;
    /**
     * 支出-承运商-装货费
     */
    @Excel(name = "支出-承运商-装货费")
    private BigDecimal payTeamPackFee;
    /**
     * 支出-承运商-卸货费
     */
    @Excel(name = "支出-承运商-卸货费")
    private BigDecimal payTeamReleaseFee;
    /**
     * 支出-承运商-油费
     */
    @Excel(name = "支出-承运商-油费")
    private BigDecimal payTeamOilFee;
    /**
     * 支出-承运商-异常费
     */
    @Excel(name = "支出-承运商-异常费")
    private BigDecimal payTeamExceptionFee;
    /**
     * 支出-承运商-其他费
     */
    @Excel(name = "支出-承运商-其他费")
    private BigDecimal payTeamOtherFee;

    /**
     * 支出-自有车-油费
     */
    @Excel(name = "支出-自有车-油费")
    private BigDecimal payPersonOilFee;
    /**
     * 支出-自有车-装货费
     */
    @Excel(name = "支出-自有车-装货费")
    private BigDecimal payPersonPackFee;
    /**
     * 支出-自有车-卸货费
     */
    @Excel(name = "支出-自有车-卸货费")
    private BigDecimal payPersonReleaseFee;
    /**
     * 支出-自有车-异常费
     */
    @Excel(name = "支出-自有车-异常费")
    private BigDecimal payPersonExceptionFee;
    /**
     * 支出-自有车-其他费
     */
    @Excel(name = "支出-自有车-其他费")
    private BigDecimal payPersonOtherFee ;
    /**
     * 支出-开票税额
     */
    @Excel(name = "支出-开票税额")
    private BigDecimal rate ;


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
    /**
     * 租户id
     */
    private Integer tenantId;

    private Integer driverId;

    private List<DriverVO> list;

}
