package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.zhkj.lc.common.annotation.Excel;
import com.zhkj.lc.order.model.entity.OrdExceptionFee;
import com.zhkj.lc.order.model.entity.OrdPickupArrivalAdd;
import com.zhkj.lc.order.model.entity.OrderSettlement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 集装箱应付费用详细信息，作为应付费用列表实体类
 */
@Data
@ApiModel(value = "集装箱应付费用详细信息")
public class CnNeedPayDetail extends  NeedPayBaseModel{


    /**
     * 车队名称
     */
    @ApiModelProperty(value = "车队名称")
    private String truckTeamName;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    @Excel(name = "车牌号")
    private String plateNumber;

    /*去回程*/
    private String type;
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
     * 分公司名称
     */
    @ApiModelProperty(value = "分公司名称")
    private String branchCName;
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
     * 提货地
     */
    @Excel(name = "提箱地")
    @ApiModelProperty(value = "提箱地")
    private String pickCNPlace;
    /**
     * 送货地
     */
    @Excel(name = "还箱地")
    @ApiModelProperty(value = "还箱地")
    private String returnCNPlace;
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
     * 提箱费
     */
//    @Excel(name = "提箱费")
    @ApiModelProperty(value = "提箱费")
    private BigDecimal pickcnFee;

    /**
     * 油卡押金费
     */
    @ApiModelProperty(value = "油卡押金费")
    @Excel(name = "油卡押金费")
    private BigDecimal oilPledge;

    @ApiModelProperty(value = "ETC费用")
    @Excel(name = "ETC费用")
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
     * 利率百分比字符
     */
    @Excel(name = "利率")
    @ApiModelProperty(value = "利率百分比字符")
    private String rateStr;

    /**
     * 应付总额
     */
    @Excel(name = "应付总额")
    @ApiModelProperty(value = "费用总额")
    private BigDecimal needPay;

    /**
     * 含利率的总额
     */
    @Excel(name = "税后总额")
    @ApiModelProperty(value = "含利率的总额")
    private BigDecimal needPayByRate;

    /**
     * 应付现金 = 分配现金-正常油卡费
     */
    @Excel(name = "分配现金")
    @ApiModelProperty(value = "分配现金")
    private BigDecimal cash;

    @ApiModelProperty(value = "应付现金")
    @Excel(name = "应付现金")
    private BigDecimal payCash;

    /**
     * 备注
     */
    @Excel(name = "备注")
    @ApiModelProperty(value = "备注")
    private String remarks;

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
    @ApiModelProperty(value = "结算状态（0:未发送，1：已发送，2：司机已确认 3：司机已反馈 4：未结算 5：已结算）")
    @Excel(name = "结算状态")
    private String needPayStatus;

    @ApiModelProperty(value = "订单更新时间")
    private Date updateTime;
    /**
     * 报销单号
     */
    private String repaynumber;
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

    private Date startTimeq;//开始时间-去程

    private Date startTimeh;//开始时间-回程

    private Date endTimeq;//结束时间-去程

    private Date endTimeh;//结束时间-回程

    List<OrdExceptionFee> exceptionFeeList;

    private String zdStatus;

    private String classOrder;//舱位号

    private String containerNo;//箱号

    private Date classDate;//班列日期

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
