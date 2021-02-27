package com.zhkj.lc.order.dto;

import com.zhkj.lc.order.model.entity.OrdExceptionFee;
import com.zhkj.lc.order.model.entity.OrderSettlement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "pc端查看账单详情的实体")
public class BillDetailForPC {


    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderId;
    /**
     * 司机姓名
     */
    @ApiModelProperty(value = "司机姓名")
    private String driverName;
    /**
     * 车辆类型
     */
    private String carType;
    /**
     * 司机电话
     */
    @ApiModelProperty(value = "司机电话")
    private String driverPhone;
    /**
     * 司机id
     */
    @ApiModelProperty(value = "司机id")
    private Integer driverId;

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 订单类型
     */
    @ApiModelProperty(value = "订单类型")
    private String orderType;

    private String type;

    /**
     * 提货地
     */
    @ApiModelProperty(value = "提货地")
    private String pickGoodsPlace;
    /**
     * 送货地
     */
    @ApiModelProperty(value = "送货地")
    private String sendGoodsPlace;
    /**
     * 公里数
     */
    @ApiModelProperty(value = "公里数")
    private int distance;
    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal perPrice;
    /**
     * 运输费
     */
    @ApiModelProperty(value = "运输费")
    private BigDecimal transportFee;

    /**
     * 提箱费
     */
    @ApiModelProperty(value = "提箱费")
    private BigDecimal pickcnFee;

    private BigDecimal transOilFee;

   /**
     * 压车费
     */
    @ApiModelProperty(value = "压车费")
    private BigDecimal ycFee;

    /**
     * 异常费用
     */
    @ApiModelProperty(value = "异常费用")
    private BigDecimal ExFee;

    /**
     * 利率
     */
    private BigDecimal moneyRate;

    /**
     * 利率百分比字符
     */
    @ApiModelProperty(value = "利率百分比字符")
    private String rateStr;

    /**
     * 应付总额
     */
    @ApiModelProperty(value = "费用总额")
    private BigDecimal needPay;

    /**
     * 含利率的总额
     */
    @ApiModelProperty(value = "含利率的总额")
    private BigDecimal needPayByRate;


    @ApiModelProperty(value = "应该显示的总额")
    private BigDecimal totalFee;

    /**
     * 结算状态（0:未发送，1：已发送，2：司机已确认 3：司机已反馈 4：未结算 5：已结算）
     */
    @ApiModelProperty(value = "结算状态（0:未发送，1：已发送，2：司机已确认 3：司机已反馈 4：未结算 5：已结算）")
    private String needPayStatus;

    @ApiModelProperty(value = "订单时间")
    private Date orderDate;

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

    @ApiModelProperty(value = "ETC费用")
    private BigDecimal etcFee;
    /**
     * 报销单号
     */
    private String repaynumber;
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 油卡押金费
     */
    @ApiModelProperty(value = "油卡押金费")
    private BigDecimal oilPledge;

    @ApiModelProperty(value = "是否开票")
    private String isYFInvoice;

    @ApiModelProperty(value = "异常费用信息")
    List<OrdExceptionFee> exceptionFeeList;

    /*结算结果集合*/
    private OrderSettlement orderSettlementStatus;

    /*结算方式*/
    private String settlement;
}
