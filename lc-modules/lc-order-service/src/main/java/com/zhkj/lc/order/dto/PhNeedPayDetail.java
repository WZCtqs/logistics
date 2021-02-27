package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.annotations.TableField;
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
 * 普货订单应付账单
 */
@Data
@ApiModel(value = "普货订单应付账单")
public class PhNeedPayDetail  extends  NeedPayBaseModel{

    /**
     * 车队名称
     */
    @ApiModelProperty(value = "车队名称")
    private String truckTeamName;

    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    @ApiModelProperty(value = "车牌号")
    private String plateNumber;
    /**
     * 订单号
     */
    @Excel(name = "订单号")
    @ApiModelProperty(value = "订单号")
    private String orderId;

    private Date orderDate;
    /**
     * 司机姓名
     */
    @Excel(name = "司机姓名")
    @ApiModelProperty(value = "司机姓名")
    private String driverName;
    /**
     * 司机电话
     */
    @Excel(name = "司机电话")
    @ApiModelProperty(value = "司机电话")
    private String driverPhone;
    /**
     * 司机id
     */
    @ApiModelProperty(value = "司机id")
    private Integer driverId;
    /**
     * 订单类型
     */
    @Excel(name = "订单类型")
    @ApiModelProperty(value = "订单类型")
    private String orderType;

    /**
     * 提货地
     */
    @Excel(name = "提货地")
    @ApiModelProperty(value = "提货地")
    private String pickGoodsPlace;
    /**
     * 送货地
     */
    @Excel(name = "送货地")
    @ApiModelProperty(value = "送货地")
    private String sendGoodsPlace;
    /**
     * 公里数
     */
    @Excel(name = "公里数")
    @ApiModelProperty(value = "公里数")
    private Integer distance;
    /**
     * 单价
     */
    @Excel(name = "单价")
    @ApiModelProperty(value = "单价")
    private BigDecimal perPrice;
    /**
     * 运输费
     */
    @Excel(name = "运输费")
    @ApiModelProperty(value = "运输费")
    private BigDecimal transportFee;

    private BigDecimal transportFeeRate;

    /**
     * 装货费
     */
    @ApiModelProperty(value = "装货费")
    private BigDecimal packFee;

    /**
     * 卸货费
     */
    @ApiModelProperty(value = "卸货费")
    private BigDecimal releaseFee;

    /**
     * 油费
     */
    @ApiModelProperty(value = "油费")
    private BigDecimal oilFee;
    /**
     * 油卡押金费
     */
    @Excel(name = "油卡押金费")
    @ApiModelProperty(value = "油卡押金费")
    private BigDecimal oilPledge;

    @ApiModelProperty(value = "ETC费用")
    private BigDecimal etcFee;
    /**
     * 异常费用
     */
    @Excel(name = "异常费用")
    @ApiModelProperty(value = "异常费用")
    private BigDecimal ExFee;

    /*压车费*/
    private BigDecimal ycFee;
    private BigDecimal ycFeeRate;
    /**
     * 利率
     */
    private BigDecimal moneyRate;
    /**
     * 利率字符串
     */
    @Excel(name = "利率")
    @ApiModelProperty(value = "利率字符串")
    private String rateStr;

    /**
     * 应付总额
     */
    @Excel(name = "应付总额")
    @ApiModelProperty(value = "费用总额")
    private BigDecimal needPay;
    /**
     * 含利率总额
     */
    @Excel(name = "税后总额")
    @ApiModelProperty(value = "含利率总额")
    private BigDecimal needPayByRate;

    /**
     * 备注
     */
    @Excel(name = "备注")
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 报销单号
     */
    private String repaynumber;
    /**
     * 运费油卡
     */
    @Excel(name = "运费油卡")
    @ApiModelProperty(value = "运费油卡")
    private BigDecimal transOilFee;

    /**
     * 正常油卡费用
     */
    @Excel(name = "正常油卡费用")
    @ApiModelProperty(value = "正常油卡费用")
    private BigDecimal commonOilFee;
    /**
     * 司机反馈
     */
    @Excel(name = "司机反馈")
    @ApiModelProperty(value = "司机反馈")
    private String feedBack;
    /**
     * 结算状态（0:未发送，1：已发送，2：司机已确认 3：司机已反馈 4：未结算 5：已结算）
     */
    @Excel(name = "结算状态")
    @ApiModelProperty(value = "结算状态（0:未发送，1：已发送，2：司机已确认 3：司机已反馈 4：未结算 5：已结算）")
    private String needPayStatus;

    /**
     * 应付现金 = 分配现金-正常油卡费
     */
    @Excel(name = "分配现金")
    @ApiModelProperty(value = "分配现金")
    private BigDecimal cash;

    @ApiModelProperty(value = "应付现金")
    private BigDecimal payCash;


    private Date updateTime;

    /**
     * 结算方式
     */
    @Excel(name = "结算方式")
    @ApiModelProperty(value = "结算方式 0:单结 1：月结")
    private String settlement;

    /**
     * 车辆类型
     */
    @Excel(name = "车辆类型")
    @ApiModelProperty(value = "车辆类型 0：集卡车 1：半挂车 2：高栏车")
    private String carType;

    /**
     * 是否应付开发票
     */
    private String isYFInvoice;

    /**
     * 是否应付开发票
     */
    private String ifAddToYfbill;

    private Date startTime;

    private Date endTime;

    List<OrdExceptionFee> exceptionFeeList;

    private String zdStatus;

    /*结算结果集合*/
    private OrderSettlement orderSettlementStatus;
    /*提货地*/
    private List<OrdPickupArrivalAdd> pickupAdds;
    /*途径地*/
    private String waypoints;
    /*卸货地*/
    private List<OrdPickupArrivalAdd> arrivalAdds;

    //车主
    @TableField(exist = false)
    private String truckownName;
}
