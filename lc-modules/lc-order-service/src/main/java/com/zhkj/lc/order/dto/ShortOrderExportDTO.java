package com.zhkj.lc.order.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 盘短管理信息
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
public class ShortOrderExportDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    @Excel(name = "序号")
    private Integer sort;

    @Excel(name = "安排舱位")
    private String classOrder;

    @Excel(name = "箱型")
    private String containerType;

    @Excel(name = "箱号")
    private String containerNo;

    @Excel(name = "进站日期")
    private Date orderDate;

    @Excel(name = "进站时间")
    private String orderTime;

    @Excel(name = "进站车辆")
    private String plateNumber;

    @Excel(name = "业务种类")
    private String shortType;

    @Excel(name = "空重箱")
    private String isHeavy;

    @Excel(name = "托运客户名称")
    private String customer;

    @Excel(name = "班列日期")
    private Date classDate;

    @Excel(name = "运输路线")
    private String transLine;

    /**
     * 货物名称
     */
    private String goodsName;
    /**
     * 重量
     */
    private Double weight;
    /**
     * 件数
     */
    private Integer goodsSum;
    /**
     * 铅封号
     */
    private String sealNumber;
    /**
     * 异常情况
     */
    private String exception;
    /**
     * 备注
     */
    private String remark;
    /**
     * 调度员
     */
    private String scheduleman;
    /**
     * 短驳次数
     */
    private Integer shortTransSum;
    /**
     * 应收单价
     */
//    @Excel(name = "应收单价")
    private BigDecimal recPrice;
    /**
     * 应收金额
     */
//    @Excel(name = "应收合计")
    private BigDecimal receivables;
    /**
     * 应收备注
     */
//    @Excel(name = "应收备注")
    private String receivablesRemark;
    /**
     * 应付单价
     */
//    @Excel(name = "应付单价")
    private BigDecimal price;
    /**
     * 应付金额
     */
//    @Excel(name = "应付合计")
    private BigDecimal needPay;
    /**
     * 应付备注
     */
//    @Excel(name = "应付备注")
    private String needPayRemark;

}
