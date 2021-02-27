package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.zhkj.lc.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: wchx
 * @Date: 2019/2/19 17:50
 */
@Data
public class ReceiveBillCommonGoods {
    //订单编号
    @Excel(name = "订单编号")
    private String orderId;
    //订单编号数组
    @TableField(exist = false)
    private String[] orderIds;
    //订单类型
    @Excel(name = "订单类型")
    private String orderType;
     // 结算日期(订单日期)
     @Excel(name = "订单日期")
    private Date classDate;
     // 客户id
    private int customerId;
    @Excel(name = "是否加入对账单")
    private String isAddToBill;
     //客户
     @Excel(name = "客户")
    private String customerName;
     // 发货人
     //@Excel(name = "发货人")
    private String shipper	;
     //发货人联系方式
    // @Excel(name = "发货人联系方式")
    private String shipperPhone;
     //提货地址
    @Excel(name = "提货地址")
    private String shipperPlace;
     //收货人
     //@Excel(name = "收货人")
    private String picker;
     // 收货人联系方式
     //@Excel(name = "收货人联系方式")
    private String pickerPhone;
     //送货地址
    @Excel(name = "送货地址")
    private String pickerPlace;

   /* *//*发货人+“-”+地址（城市+详细）*//*
    @Excel(name = "发货人-提货地址1")
    private String pickupAdd1;
    @Excel(name = "发货人电话1")
    private String shipperPhone1;

    *//*发货人+“-”+地址（城市+详细）*//*
    @Excel(name = "发货人-提货地址2")
    private String pickupAdd2;
    @Excel(name = "发货人电话2")
    private String shipperPhone2;

    *//*发货人+“-”+地址（城市+详细）*//*
    @Excel(name = "发货人-提货地址3")
    private String pickupAdd3;
    @Excel(name = "发货人电话3")
    private String shipperPhone3;

    *//*收货人+“-”+地址（城市+详细）*//*
    @Excel(name = "收货人-送货地址1")
    private String arrivalAdd1;
    @Excel(name = "收货人电话1")
    private String pickerPhone1;

    *//*收货人+“-”+地址（城市+详细）*//*
    @Excel(name = "收货人-送货地址2")
    private String arrivalAdd2;
    @Excel(name = "收货人电话2")
    private String pickerPhone2;

    *//*收货人+“-”+地址（城市+详细）*//*
    @Excel(name = "收货人-送货地址3")
    private String arrivalAdd3;
    @Excel(name = "收货人电话3")
    private String pickerPhone3;*/


     //是否开发票
    private String isInvoice;
     //计费里程
     @Excel(name = "计费里程")
    private int chargedMileage;
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
     //提货费用
//     @Excel(name = "提货费用")
    private BigDecimal pickFee;
     //装货费用
//     @Excel(name = "装货费用")
    private BigDecimal packFee;
     //卸货费用
//     @Excel(name = "卸货费用")
    private BigDecimal releaseFee;
     //保险费用
     @Excel(name = "保险费用")
    private BigDecimal insuranceFee;
     //异常费用
     @Excel(name = "异常费用")
    private BigDecimal exceptionFee;
    //其他费用
    @Excel(name = "其他费用")
    private BigDecimal otherFee;
     //订单费用
     @Excel(name = "订单费用")
    private BigDecimal orderFee;
     //利率
     @Excel(name = "利率")
    private BigDecimal rate;
     //总计费用（含发票）
     @Excel(name = "总计费用（含发票）")
    private BigDecimal totalFee;
     //备注
     @Excel(name = "备注")
    private String remark;
     //结算方式
     @Excel(name = "结算方式")
    private String settlement;
     //结算状态
     @Excel(name = "结算状态")
    private String balanceStatus;

    private Date updateTime;
}
