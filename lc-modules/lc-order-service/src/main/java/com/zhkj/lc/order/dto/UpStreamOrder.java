package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单管理
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
public class UpStreamOrder extends Model<UpStreamOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    @TableField("order_id")
    private String orderId;
    /**
     * 订单状态
     */
    private String status;

    @Excel(name = "订单状态")
    private String statusDec;
    /**
     * 派车日期
     */
    @TableField("send_truck_date")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date sendTruckDate;
    /**
     * 班列日期
     */
    @TableField("class_date")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date classDate;
    /**
     * 订单类型
     */
    @Excel(name = "去回程")
    private String type;
    /**
     * 承运单位
     */
    private String carrier;
    /**
     * 舱位号
     */
    @TableField("class_order")
    private String classOrder;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户id
     */
    @Excel(name = "客户名称")
    @TableField("customer_id")
    private Integer customerId;
    /**
     * 业务员
     */
    private String salesman;

    /**
     * 发货人
     */
    @Excel(name = "联系人")
    private String people;

    private String consignor;
    /**
     * 发货人联系方式
     */
    @TableField("consignor_phone")
    private String consignorPhone;

    @Excel(name = "联系方式")
    private String peoplePhone;
    /**
     * 装货地
     */
    @TableField("pickup_goods_place")
    private String pickupGoodsPlace;
    /**
     * 装货详细地址
     */
    @Excel(name = "始发地详细地址")
    @TableField("pickup_goods_detailplace")
    private String pickupGoodsDetailplace;
    /**
     * 收货人
     */
    @Excel(name = "收货人")
    private String consignee;
    /**
     * 收货人联系方式
     */
    @Excel(name = "收货人联系方式")
    @TableField("consignee_phone")
    private String consigneePhone;
    /**
     * 卸货日期
     */
    @TableField("send_goods_date")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date sendGoodsDate;
    /**
     * 卸货地
     */
    @TableField("send_goods_place")
    private String sendGoodsPlace;
    /**
     * 卸货详细地址
     */
    @Excel(name = "目的地详细地址")
    @TableField("send_goods_detailplace")
    private String sendGoodsDetailplace;
    /**
     * 品名
     */
    @Excel(name = "货品名称")
    @TableField("product_name")
    private String productName;
    /**
     * 尺寸
     */
    private String size;
    /**
     * 货重
     */
    private String weight;
    /**
     * 集装箱号
     */
    @TableField("container_no")
    private String containerNo;
    /**
     * 箱型
     */
    @Excel(name = "箱型*箱量")
    @TableField("container_type")
    private String containerType;
    /**
     * 箱量
     */
    @TableField("container_num")
    private Integer containerNum;
    /**
     * 提箱地
     */
    @TableField("pickup_con_place")
    private String pickupConPlace;
    /**
     * 提箱详细地址
     */
    @TableField("pickup_con_detailplace")
    private String pickupConDetailplace;
    /**
     * 还箱地
     */
    @TableField("return_con_place")
    private String returnConPlace;
    /**
     * 还箱详细地址
     */
    @TableField("return_con_detailplace")
    private String returnConDetailplace;


    /**
     * 装货日期
     */
    @Excel(name = "提货日期")
    @TableField("pickup_goods_date")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date pickupGoodsDate;
    /**
     * 调度员
     */
    private String scheduleman;
    /**
     * 司机id
     */
    @TableField("driver_id")
    private Integer driverId;
    /**
     * 司机姓名
     */
    private String driverName;

    /*司机身份证号*/
    private String idcardNumber;
    /**
     * 车牌号
     */
    @TableField("plate_number")
    private String plateNumber;

    private String truckAttribute;
    /**
     * 是否开发票
     */
    @TableField("is_invoice")
    private String isInvoice;
    /**
     * 公里
     */
    private Integer kilometre;
    /**
     * 应收单价
     */
    private BigDecimal recPrice;
    /**
     * 应收运费
     */
    private BigDecimal receivables;
    /**
     * 应付单价
     */
    private BigDecimal payPrice;
    /**
     * 应付运费
     */
    private BigDecimal needPay;
    /**
     * 提箱费
     */
    private BigDecimal pickcnFee;
    /**
     * 压车费
     */
    private BigDecimal parkingFee;
    /**
     * 结算方式
     */
    private String settlement;

    /**
     * 备注
     */
    private String receiverRemark;
    /**
     * 签收注意事项
     */
    private String remark;
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
    @TableField("del_flag")
    private String delFlag;
    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;

    private String driverPhone;

    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

    @Override
    public String toString() {
        return "OrdOrder{" +
                ", orderId=" + orderId +
                ", type=" + type +
                ", status=" + status +
                ", classDate=" + classDate +
                ", sendTruckDate=" + sendTruckDate +
                ", salesman=" + salesman +
                ", scheduleman=" + scheduleman +
                ", carrier=" + carrier +
                ", classOrder=" + classOrder +
                ", customerId=" + customerId +
                ", productName=" + productName +
                ", size=" + size +
                ", weight=" + weight +
                ", containerNo=" + containerNo +
                ", containerType=" + containerType +
                ", containerNum=" + containerNum +
                ", pickupConPlace=" + pickupConPlace +
                ", returnConPlace=" + returnConPlace +
                ", consignor=" + consignor +
                ", consignorPhone=" + consignorPhone +
                ", pickupGoodsPlace=" + pickupGoodsPlace +
                ", pickupGoodsDate=" + pickupGoodsDate +
                ", consignee=" + consignee +
                ", consigneePhone=" + consigneePhone +
                ", sendGoodsPlace=" + sendGoodsPlace +
                ", sendGoodsDate=" + sendGoodsDate +
                ", driverId=" + driverId +
                ", plateNumber=" + plateNumber +
                ", isInvoice=" + isInvoice +
                ", kilometre=" + kilometre +
                ", receiverRemark=" + receiverRemark +
                ", remark=" + remark +
                ", delFlag=" + delFlag +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateBy=" + updateBy +
                ", updateTime=" + updateTime +
                ", tenantId=" + tenantId +
                "}";
    }
}
