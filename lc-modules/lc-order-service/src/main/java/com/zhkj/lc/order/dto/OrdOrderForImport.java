package com.zhkj.lc.order.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 订单管理
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
public class OrdOrderForImport {
    /**
     * 班列日期
     */

    @Excel(name = "班列日期")
    private Date classDate;
    /**
     * 订单类型
     */
    @Excel(name = "去回程")
    private String type;
    /**
     * 订单编号
     */
   //@Excel(name = "订单编号")
    private String orderId;
    /**
     * 订单状态
     */
    //@Excel(name = "订单状态")
    private String status;

    private String statusDec;
    /**
     * 派车日期
     */
    //@Excel(name = "派车日期")
    private Date sendTruckDate;

    /**
     * 承运单位
     */
    @Excel(name = "承运单位")
    private String carrier;
    /**
     * 舱位号
     */
    @Excel(name = "客户订单号")
    private String classOrder;
    /**
     * 客户名称
     */
    @Excel(name = "客户名称")
    private String customerName;
    /**
     * 客户id
     */
    private Integer customerId;
    /**
     * 业务员
     */
//    @Excel(name = "业务员")
    private String salesman;

    /**
     * 发货人
     */
    //@Excel(name = "发货人")
    private String consignor;
    /**
     * 发货人联系方式
     */
   // @Excel(name = "发货人联系方式")
    private String consignorPhone;
    /**
     * 装货日期
     */
   // @Excel(name = "提货日期")
    private Date pickupGoodsDate;
    /**
     * 装货地
     */
//    @Excel(name = "提货城市")
    private String pickupGoodsPlace;
    /**
     * 装货详细地址
     */
//    @Excel(name = "提货详细地址")
    private String pickupGoodsDetailplace;

    /*发货人+“-”+地址（城市+详细）*/
    @Excel(name = "发货人-提货地址1")
    private String pickupAdd1;
    @Excel(name = "发货人联系方式1")
    private String consignorPhone1;
    @Excel(name = "提货日期1")
    //@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date pickupGoodsDate1;

    /*发货人+“-”+地址（城市+详细）*/
    @Excel(name = "发货人-提货地址2")
    private String pickupAdd2;
    @Excel(name = "发货人联系方式2")
    private String consignorPhone2;
    @Excel(name = "提货日期2")
    //@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date pickupGoodsDate2;

    /*发货人+“-”+地址（城市+详细）*/
    @Excel(name = "发货人-提货地址3")
    private String pickupAdd3;
    @Excel(name = "发货人联系方式3")
    private String consignorPhone3;
    @Excel(name = "提货日期3")
   // @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date pickupGoodsDate3;


    /**
     * 收货人
     */
    //@Excel(name = "收货人")
    private String consignee;
    /**
     * 收货人联系方式
     */
   // @Excel(name = "收货人联系方式")
    private String consigneePhone;
    /**
     * 卸货日期
     */
    //@Excel(name = "送货日期")
    private Date sendGoodsDate;
    /**
     * 卸货地
     */
   // @Excel(name = "送货地址")
    private String sendGoodsPlace;
    /**
     * 卸货详细地址
     */
   // @Excel(name = "送货详细地址")
    private String sendGoodsDetailplace;

    /*收货人+“-”+地址（城市+详细）*/
    @Excel(name = "收货人-送货地址1")
    private String arrivalAdd1;
    @Excel(name = "收货人联系方式1")
    private String consigneePhone1;
    @Excel(name = "送货日期1")
    //@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date sendGoodsDate1;

    /*收货人+“-”+地址（城市+详细）*/
    @Excel(name = "收货人-送货地址2")
    private String arrivalAdd2;
    @Excel(name = "收货人联系方式2")
    private String consigneePhone2;
    @Excel(name = "送货日期2")
    //@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date sendGoodsDate2;

    /*收货人+“-”+地址（城市+详细）*/
    @Excel(name = "收货人-送货地址3")
    private String arrivalAdd3;
    @Excel(name = "收货人联系方式3")
    private String consigneePhone3;
    @Excel(name = "送货日期3")
    //@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date sendGoodsDate3;

    /**
     * 品名
     */
    @Excel(name = "货品名称")
    private String productName;
    @Excel(name = "总毛重（吨）")
    private String weight;
    /**
     * 尺寸
     */
    @Excel(name = "总体积（立方）")
    private String size;
    /**
     * 集装箱号
     */
    @Excel(name = "集装箱号")
    private String containerNo;
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
    /**
     * 提箱地
     */
    @Excel(name = "提箱地址")
    private String pickupConPlace;
    /**
     * 提箱详细地址
     */
    @Excel(name = "提箱详细地址")
    private String pickupConDetailplace;
    /**
     * 还箱地
     */
    @Excel(name = "还箱地址")
    private String returnConPlace;
    /**
     * 还箱详细地址
     */
    @Excel(name = "还箱详细地址")
    private String returnConDetailplace;
    /**
     * 调度员
     */
//    @Excel(name = "调度员")
    private String scheduleman;
    /**
     * 司机id
     */
    private Integer driverId;
    /**
     * 司机姓名
     */
    //@Excel(name = "司机姓名")
    private String driverName;

    /*司机身份证号*/
    private String idcardNumber;
    /**
     * 车牌号
     */
    //@Excel(name = "车牌号")
    private String plateNumber;

   // @Excel(name = "车辆类型")
    private String truckAttribute;
    /**
     * 是否开发票
     */
    private String isInvoice;
    /**
     * 公里
     */
    //@Excel(name = "公里")
    private Integer kilometre;
    /**
     * 应收单价
     */
    //@Excel(name = "应收单价")
    private BigDecimal recPrice;
    /**
     * 应收运费
     */
    //@Excel(name = "应收运费")
    private BigDecimal receivables;
    /**
     * 应付单价
     */
    //@Excel(name = "应付单价")
    private BigDecimal payPrice;
    /**
     * 应付运费
     */
    //@Excel(name = "应付运费")
    private BigDecimal needPay;
    /**
     * 提箱费
     */
    //@Excel(name = "提箱费")
    private BigDecimal pickcnFee;
    /**
     * 压车费
     */
    //@Excel(name = "压车费")
    private BigDecimal parkingFee;
    /**
     * 结算方式
     */
    @Excel(name = "结算方式")
    private String settlement;
    /**
     * 签收码
     */
    private String receiptCode;

    /**
     * 签收码是否发送
     */
    private String isSend;

    /**
     * 删除标志
     */
    private String delFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 租户id
     */
    private Integer tenantId;

    private String driverPhone;
    /**
     * 备注
     */
    private String receiverRemark;
    /**
     * 签收注意事项
     */
    @Excel(name = "备注")
    private String remark;
}
