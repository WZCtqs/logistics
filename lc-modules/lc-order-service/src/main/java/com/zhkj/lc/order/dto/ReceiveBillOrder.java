package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: wchx
 * @Date: 2019/2/19 17:50
 */
@Data
public class ReceiveBillOrder {
     //订单编号
     @Excel(name = "订单编号")
    private String orderId;
    //订单编号数组
    @TableField(exist = false)
    private String[] orderIds;
     //订单类型
     @Excel(name = "订单类型")
    private String orderType;
    //结算日期(班列日期)
    @Excel(name = "班列日期")
    private Date classDate;
    //结算日期(订单日期)
    @Excel(name = "订单日期")
    private Date orderDate;
     //客户id
    private int customerId;
    @Excel(name = "是否加入对账单")
    private String isAddToBill;
     //客户
     @Excel(name = "客户")
    private String customerName;
    //品名
    @Excel(name = "品名")
    private String productName;
    /**
     * 舱位号
     */
    @Excel(name = "舱位号")
    private String classOrder;
    /**
     * 集装箱号
     */
    @Excel(name = "集装箱号")
    private String containerNo;
    @Excel(name = "铅封号")
    private String sealNumber;
    /**
     * 箱型
     */
    @Excel(name = "箱型")
    private String containerType;
    /**
     * 箱量
     */
    @Excel(name = "箱量")
    private Integer containerNum;
     //提箱地
     @Excel(name = "提箱地")
    private String pickupConPlace;
     //还箱地
     @Excel(name = "还箱地")
    private String returnConPlace;

     //发货人
     //@Excel(name = "发货人")
    private String consignor;
     //发货人联系方式
     //@Excel(name = "发货人联系方式")
    private String consignorPhone;
     //装货地
    @Excel(name = "提货地址")
    private String pickupGoodsDetailplace;
     //装货日期
     //@Excel(name = "装货日期")
    private Date pickupGoodsDate;
     //收货人
     //@Excel(name = "收货人")
    private String consignee;
     //收货人联系方式
     //@Excel(name = "收货人联系方式")
    private String consigneePhone;
     //卸货地
    @Excel(name = "送货地址")
    private String sendGoodsDetailplace;
     //卸货日期
     //@Excel(name = "卸货日期")
    private Date sendGoodsDate;

    /*发货人+“-”+地址（城市+详细）*//*
    @Excel(name = "发货人-提货地址1")
    private String pickupAdd1;
    @Excel(name = "发货人联系方式1")
    private String consignorPhone1;
    @Excel(name = "提货日期1")
    private String pickupGoodsDate1;

    *//*发货人+“-”+地址（城市+详细）*//*
    @Excel(name = "发货人-提货地址2")
    private String pickupAdd2;
    @Excel(name = "发货人联系方式2")
    private String consignorPhone2;
    @Excel(name = "提货日期2")
    private String pickupGoodsDate2;

    *//*发货人+“-”+地址（城市+详细）*//*
    @Excel(name = "发货人-提货地址3")
    private String pickupAdd3;
    @Excel(name = "发货人联系方式3")
    private String consignorPhone3;
    @Excel(name = "提货日期3")
    private String pickupGoodsDate3;

    *//*收货人+“-”+地址（城市+详细）*//*
    @Excel(name = "收货人-送货地址1")
    private String arrivalAdd1;
    @Excel(name = "收货人联系方式1")
    private String consigneePhone1;
    @Excel(name = "送货日期1")
    private String sendGoodsDate1;

    *//*收货人+“-”+地址（城市+详细）*//*
    @Excel(name = "收货人-送货地址2")
    private String arrivalAdd2;
    @Excel(name = "收货人联系方式2")
    private String consigneePhone2;
    @Excel(name = "送货日期2")
    private String sendGoodsDate2;

    *//*收货人+“-”+地址（城市+详细）*//*
    @Excel(name = "收货人-送货地址3")
    private String arrivalAdd3;
    @Excel(name = "收货人联系方式3")
    private String consigneePhone3;
    @Excel(name = "送货日期3")
    private String sendGoodsDate3;*/

     //是否开发票
    private String isInvoice;
     //公里
     @Excel(name = "公里")
    private int kilometre;
     //车牌号
    @Excel(name = "车牌号")
    private String plateNumber;
    //车牌号
    @Excel(name = "车辆类型")
    private String truckAttribute;
     //应收单价
     @Excel(name = "应收单价")
    private BigDecimal recPrice;
     //应收运费
     @Excel(name = "应收运费")
    private BigDecimal receivables;
     //提箱费
//     @Excel(name = "提箱费")
    private BigDecimal pickcnFee;
     //压车费
//     @Excel(name = "压车费")
    private BigDecimal parkingFee;
     //异常费用
     @Excel(name = "异常费用")
    private BigDecimal exceptionFee;
     //订单费用
     @Excel(name = "订单费用")
    private BigDecimal orderFee;
     //结算方式
     @Excel(name = "结算方式")
    private String settlement;
     //结算状态
     @Excel(name = "结算状态")
    private String balanceStatus;
     //利率
     @Excel(name = "利率")
    private BigDecimal rate;
     //总计费用（含发票）
     @Excel(name = "总计费用（含发票")
    private BigDecimal totalFee;
     //备注
     @Excel(name = "备注")
    private String remark;

    private Date updateTime;
    //去回程（0：去，1：回）
    private String type;
}