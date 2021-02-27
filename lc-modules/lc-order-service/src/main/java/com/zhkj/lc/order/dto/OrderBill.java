package com.zhkj.lc.order.dto;

import com.zhkj.lc.common.annotation.Excel;
import com.zhkj.lc.order.model.entity.OrdExceptionFee;
import com.zhkj.lc.order.model.entity.OrdPickupArrivalAdd;
import com.zhkj.lc.order.model.entity.OrderSettlement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 小程序端查看账单详细信息的实体
 */
@Data
@ApiModel(value = "账单详情")
public class OrderBill {

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    @Excel(name = "订单编号")
    private String orderId;

    @ApiModelProperty(value = "车牌号")
    @Excel(name = "车牌号")
    private String plateNumber;

    @ApiModelProperty(value = "车辆种类")
    @Excel(name = "车辆种类")
    private String carType;
    /**
     * 发货地
     */
    @ApiModelProperty(value = "发货地")
    @Excel(name = "发货地")
    private String startPlace;
    /**
     * 到货地
     */
    @ApiModelProperty(value = "到货地")
    @Excel(name = "到货地")
    private String endPlace;

    private String type;

    private String pickCnPlace;

    private String returnCnPlace;
    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    @Excel(name = "下单时间")
    private Date orderDate;
    /**
     * 运费总计
     */
    @ApiModelProperty(value = "运费总计")
    @Excel(name = "运费总计")
    private BigDecimal transFee;

    private BigDecimal transFeeRate;
    /**
     * 公里数
     */
    @ApiModelProperty(value = "公里数")
    @Excel(name = "公里数")
    private int distance;
    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    @Excel(name = "单价")
    private BigDecimal perPrice;
    /**
     * 异常费用总计
     */
    @ApiModelProperty(value = "异常费用总计")
    @Excel(name = "异常费用总计")
    private BigDecimal exFee;
    /**
     * 压车费
     */
    @ApiModelProperty(value = "压车费")
    @Excel(name = "压车费")
    private BigDecimal ycFee;

    private BigDecimal ycFeeRate;
    /**
     * 放空费
     */
    @ApiModelProperty(value = "放空费")
    @Excel(name = "放空费")
    private BigDecimal fkFee;
    /**
     * 路桥费
     */
    @ApiModelProperty(value = "路桥费")
    @Excel(name = "路桥费")
    private BigDecimal lqFee;
    /**
     * 扣除油卡
     */
    @ApiModelProperty(value = "分配油卡")
    @Excel(name = "分配油卡")
    private BigDecimal transOilFee;

    /**
     * 异常费用列表
     */
    @ApiModelProperty(value = "异常费用列表")
    private List<OrdExceptionFee> exceptionFeeList;
    /**
     * 合计应付
     */
    @ApiModelProperty(value = "合计应付")
	@Excel(name = "合计应付")
    private BigDecimal totalFee;

    /*
     * 应付现金
     */
    private BigDecimal payCash;
    /*
     * 油卡押金费
     */
    private BigDecimal oilPledge;
    /*
     * ETC费用
     */
    private BigDecimal etcFee;
    /**
     * 报销单号
     */
    private String repaynumber;
    /*
     * 现金分配
     */
    private BigDecimal cash;

    /*
     * 正常油卡费
     */
    private BigDecimal commonOilFee;
    /*
     * 结算方式
     */
    private String settlement;

    @ApiModelProperty(value = "反馈信息")
    private  String feedBack;

    /*结算结果集合*/
    private OrderSettlement orderSettlementStatus;

    private String isYfInvoice;

    private Integer driverId;

    private String driverName;

    private String driverPhone;

    private BigDecimal payRate;

    private List<OrdPickupArrivalAdd> arrivalAdds;

    private List<OrdPickupArrivalAdd> pickupAdds;
}
