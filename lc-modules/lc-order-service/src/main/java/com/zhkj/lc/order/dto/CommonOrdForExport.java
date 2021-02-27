package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.zhkj.lc.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 导出普货订单所需bean
 */
@Data
public class CommonOrdForExport {

    //订单号
    @Excel(name = "订单号")
    private String morderId;

    private String status;
    //订单状态
    @Excel(name = "订单状态")
    private String statusValue;

    //客户名称
    @Excel(name = "客户名称")
    private String customerName;

    private Integer customerId;
    //发货人
   // @Excel(name = "发货人")
    private String shipper;

    /**
     * 发货人电话
     */
   // @Excel(name = "发货人电话")
    private String shipperPhone;

    /**
     * 收货人电话
     */
    //@Excel(name = "收货人电话")
    private String pickerPhone;

    //收货人
    //@Excel(name = "收货人")
    private String picker;

    //发货城市
    //@Excel(name = "发货城市")
    private String sendGoodsPlace;

    //收货城市
    //@Excel(name = "收货城市")
    private String pickGoodsPlace;

    /*发货人+“-”+地址（城市+详细）*/
    @Excel(name = "发货人-提货地址1")
    private String pickupAdd1;
    @Excel(name = "发货人电话1")
    private String shipperPhone1;

    /*发货人+“-”+地址（城市+详细）*/
    @Excel(name = "发货人-提货地址2")
    private String pickupAdd2;
    @Excel(name = "发货人电话2")
    private String shipperPhone2;

    /*发货人+“-”+地址（城市+详细）*/
    @Excel(name = "发货人-提货地址3")
    private String pickupAdd3;
    @Excel(name = "发货人电话3")
    private String shipperPhone3;

    /*收货人+“-”+地址（城市+详细）*/
    @Excel(name = "收货人-送货地址1")
    private String arrivalAdd1;
    @Excel(name = "收货人电话1")
    private String pickerPhone1;

    /*收货人+“-”+地址（城市+详细）*/
    @Excel(name = "收货人-送货地址2")
    private String arrivalAdd2;
    @Excel(name = "收货人电话2")
    private String pickerPhone2;

    /*收货人+“-”+地址（城市+详细）*/
    @Excel(name = "收货人-送货地址3")
    private String arrivalAdd3;
    @Excel(name = "收货人电话3")
    private String pickerPhone3;


    //结算方式

    private String balanceWay;

    //结算方式
    @Excel(name = "应收结算方式")
    private String balanceWayValue;
    //计费里程
    @Excel(name = "计费里程")
    private BigDecimal mchargedMileage;

    //体积
    @Excel(name = "体积")
    private BigDecimal sumVolume;

    //重量
    @Excel(name = "重量")
    private BigDecimal sumWeight;

    //发货时间
    @Excel(name = "发货时间")
    private Date sendGoodsDate;

    //到货时间
    @Excel(name = "到货时间")
    private Date pickGoodsDate;

    /**
     * 运输费用
     */
    @Excel(name = "应收运输费用")
    private BigDecimal mtransportFee;
    /**
     * 提货费用
     */
    @Excel(name = "应收提货费用")
    private BigDecimal mpickFee;
    /**
     * 装货费用
     */
    @Excel(name = "应收装货费用")
    private BigDecimal mpackFee;
    /**
     * 卸货费用
     */
    @Excel(name = "应收卸货费用")
    private BigDecimal mreleaseFee;
    /**
     * 保险费用
     */
    @Excel(name = "应收保险费用")
    private BigDecimal minsuranceFee;
    /**
     * 其他费用
     */
    @Excel(name = "应收其他费用")
    private BigDecimal motherFee;
    /**
     * 是否开发票（0：否，1：是）
     */

    private String isInvoice;

    @Excel(name = "是否开发票")
    private String isInvoiceValue;
    /**
     * 费用合计
     */
    @Excel(name = "应收费用合计")
    private BigDecimal mtotalFee;


    /****车辆调度信息***/
    private String truckType;

    //车辆类型
    @Excel(name = "车辆类型")
    private String truckTypeValue;

    //车牌号
    @Excel(name = "车牌号")
    private String plateNumber;

    //主司机
    @Excel(name = "主司机")
    private String mdriver;

    //主司机手机号
    @Excel(name = "主司机手机号")
    private String mdriverPhone;

    //副司机
    @Excel(name = "副司机")
    private String sdriver;

    //副司机手机号
    @Excel(name = "副司机手机号")
    private String sdriverPhone;

    /**
     * 车型（0：厢车，1：自卸，2：冷藏，3：平板）
     */

    private String vehicleType;

    /**
     * 车长
     */

    private String vehicleLength;

    @Excel(name = "车型")
    private String vehicleAttribute;
    /**
     * 运输费
     */
    @Excel(name = "应付运输费")
    private BigDecimal transportFee;
    /**
     * 装货费
     */
    @Excel(name = "应付装货费")
    private BigDecimal packFee;
    /**
     * 卸货费
     */
    @Excel(name = "应付卸货费")
    private BigDecimal releaseFee;
    /**
     * 保险费
     */
    @Excel(name = "应付保险费")
    private BigDecimal insuranceFee;
    /**
     * 其他费用
     */
    @Excel(name = "应付其他费用")
    private BigDecimal otherFee;

    private String payType;
    /**
     * 结算方式（0：按单 1：按月）
     */
    @Excel(name = "应付结算方式")
    private String payTypeValue;
    /**
     * 付款方式（0：现金，1：油卡）
     */
    @Excel(name = "应付油卡费")
    private BigDecimal oilPayment;
    /**
     * 费用总计
     */
    @Excel(name = "应付费用总计")
    private BigDecimal totalFee;
    /**
     * 付款金额
     */
    @Excel(name = "应付现金")
    private BigDecimal cashPayment;

    /**
     * 路桥费
     */
    @Excel(name = "应付路桥费")
    private BigDecimal luqiaoFee;
    /**
     * 油费
     */
    @Excel(name = "应付油费")
    private BigDecimal oilFee;

    //备注
    @Excel(name = "备注")
    private String remarks;






}
