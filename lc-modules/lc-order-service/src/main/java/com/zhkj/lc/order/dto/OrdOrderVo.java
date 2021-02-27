package com.zhkj.lc.order.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.vo.CustomerVO;
import com.zhkj.lc.common.vo.DriverVO;
import com.zhkj.lc.order.model.entity.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单管理
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
@TableName("ord_order")
public class OrdOrderVo extends Model<OrdOrderVo> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单编号
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 订单编号数组
     */
    @TableField(exist = false)
    private String[] orderIds;

    /**
     * 上游系统订单编号
     */
    @TableField("upstream_id")
    private String upstreamId;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 结算状态
     */
    @TableField("balance_status")
    private String balanceStatus;

    /**
     * 应付结算状态
     */
    @TableField("need_pay_status")
    private String needPayStatus;

    /**
     * 司机反馈
     */
    @TableField("feed_back")
    private String feedBack;

    @TableField(exist = false)
    private String statusDec;
    /**
     * 派车日期
     */
//    @Excel(name = "派车日期")
    @TableField("send_truck_date")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date sendTruckDate;
    /**
     * 班列日期
     */
    @Excel(name = "班列日期")
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
    @Excel(name = "承运单位")
    private String carrier;
    /**
     * 舱位号
     */
    @Excel(name = "客户订单号")
    @TableField("class_order")
    private String classOrder;
    /**
     * 客户名称
     */
    @Excel(name = "客户名称")
    @TableField(exist = false)
    private String customerName;
    /**
     * 客户id
     */
    @TableField("customer_id")
    private Integer customerId;
    /**
     * 业务员
     */
//    @Excel(name = "业务员")
    private String salesman;

    /**
     * 发货人
     */
    @Excel(name = "发货人")
    private String consignor;
    /**
     * 发货人联系方式
     */
    @Excel(name = "发货人联系方式")
    @TableField("consignor_phone")
    private String consignorPhone;
    /**
     * 装货日期
     */
    @Excel(name = "提货日期")
    @TableField("pickup_goods_date")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date pickupGoodsDate;
    /**
     * 装货地
     */
    @Excel(name = "提货城市")
    @TableField("pickup_goods_place")
    private String pickupGoodsPlace;
    /**
     * 装货地数组
     */
    @TableField(exist = false)
    private String[] pickupGoodsPlaceArray;
    /**
     * 装货详细地址
     */
    @Excel(name = "提货详细地址")
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
    @Excel(name = "送货日期")
    @TableField("send_goods_date")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date sendGoodsDate;
    /**
     * 卸货地
     */
    @Excel(name = "送货地址")
    @TableField("send_goods_place")
    private String sendGoodsPlace;
    /**
     * 卸货地数组
     */
    @TableField(exist = false)
    private String[] sendGoodsPlaceArray;
    /**
     * 卸货详细地址
     */
    @Excel(name = "送货详细地址")
    @TableField("send_goods_detailplace")
    private String sendGoodsDetailplace;
    /**
     * 品名
     */
    @Excel(name = "货品名称")
    @TableField("product_name")
    private String productName;
    /**
     * 货重
     */
    @Excel(name = "货重")
    private String weight;
    /**
     * 尺寸
     */
    @Excel(name = "体积尺寸")
    private String size;
    /**
     * 集装箱号
     */
    @Excel(name = "集装箱号")
    @TableField("container_no")
    private String containerNo;
    /**
     * 铅封号
     */
    @TableField("seal_number")
    private String sealNumber;
    /**
     * 箱型
     */
    @Excel(name = "箱型")
    @TableField("container_type")
    private String containerType;
    /**
     * 箱量
     */
    @Excel(name = "箱量")
    @TableField("container_num")
    private Integer containerNum;
    /**
     * 提箱地
     */
    @Excel(name = "提箱地址")
    @TableField("pickup_con_place")
    private String pickupConPlace;
    /**
     * 提箱地数组
     */
    @TableField(exist = false)
    private String[] pickupConPlaceArray;
    /**
     * 提箱详细地址
     */
    @Excel(name = "提箱详细地址")
    @TableField("pickup_con_detailplace")
    private String pickupConDetailplace;
    /**
     * 还箱地
     */
    @Excel(name = "还箱地址")
    @TableField("return_con_place")
    private String returnConPlace;
    /**
     * 还箱地数组
     */
    @TableField(exist = false)
    private String[] returnConPlaceArray;
    /**
     * 还箱详细地址
     */
    @Excel(name = "还箱详细地址")
    @TableField("return_con_detailplace")
    private String returnConDetailplace;
    /**
     * 调度员
     */
//    @Excel(name = "调度员")
    private String scheduleman;
    /**
     * 司机id
     */
    @TableField("driver_id")
    private Integer driverId;
    /**
     * 司机姓名
     */
//    @Excel(name = "司机姓名")
    @TableField(exist = false)
    private String driverName;

    @TableField(exist = false)
    private String driverAddress;
    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    @TableField("plate_number")
    private String plateNumber;
    /**
     * 是否开发票
     */
    @TableField("is_invoice")
    private String isInvoice;
    /**
     * 公里
     */
//    @Excel(name = "公里")
    private Integer kilometre;

    @TableField("km_yf")
    private Integer kmYf;
    /**
     * 应收单价
     */
//    @Excel(name = "应收单价")
    @TableField("rec_price")
    private BigDecimal recPrice;
    /**
     * 应收运费
     */
//    @Excel(name = "应收运费")
    private BigDecimal receivables;
    /**
     * 应付单价
     */
//    @Excel(name = "应付单价")
    @TableField("pay_price")
    private BigDecimal payPrice;
    /**
     * 应付运费
     */
//    @Excel(name = "应付运费")
    @TableField("need_pay")
    private BigDecimal needPay;
    /**
     * 应付利率
     */
    @TableField("pay_rate")
    private BigDecimal payRate;
    /**
     * 提箱费
     */
//    @Excel(name = "提箱费")
    @TableField("pickcn_fee")
    private BigDecimal pickcnFee;
    /**
     * 压车费
     */
//    @Excel(name = "压车费")
    @TableField("parking_fee")
    private BigDecimal parkingFee;
    /**
     * 应付结算方式
     */
//    @Excel(name = "应付结算方式")
    private String settlement;
    /**
     * 应收结算方式
     */
//    @Excel(name = "应收结算方式")
    @TableField("balance_way")
    private String balanceWay;
    /**
     * 上游系统订舱 备注
     */
    @TableField("receiver_remark")
    private String receiverRemark;

    /**
     * 物流公司备注
     */
    @TableField("upstream_remark")
    private String upstreamRemark;
    /**
     * 是否重去重回
     */
    @TableField("upstream_is_zqzh")
    private String upstreamIsZqzh;
    /**
     * 上游重去重回舱位号
     */
    @TableField("upstream_zqzh_class_order")
    private String upstreamZqzhClassOrder;
    /**
     * 上报异常费用
     */
    @TableField("upstream_report_fee")
    private BigDecimal upstreamReportFee;
    /**
     * 上报异常费用备注
     */
    @TableField("upstream_report_remark")
    private String upstreamReportRemark;
    /**
     * 上游返回异常费用
     */
    @TableField("upstream_return_fee")
    private BigDecimal upstreamReturnFee;
    /**
     * 压车天数
     */
    @TableField("upstream_yc_day")
    private Integer upstreamYcDay;
    /**
     * 压车单价
     */
    @TableField("upstream_yc_price")
    private BigDecimal upstreamYcPrice;

    /**
     * 签收注意事项
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 上游系统订舱id
     */
    @TableField("receipt_code")
    private String receiptCode;

    /**
     * 签收单
     */
    @TableField(exist = false)
    private String[] receiptPng;

    /**
     * 签收注意事项
     */
    @TableField("is_send")
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

    @TableField(exist = false)
    private String[] plates;

    @TableField(exist = false)
    private DriverVO driverVO;

    @TableField(exist = false)
    private String driverPhone;

    @TableField(exist = false)
    private String truckAttribute;

    @TableField(exist = false)
    private CustomerVO customerVO;

    @TableField(exist = false)
    private OrdOrderFile ordOrderFile;

    /*对接箱信通使用*/
    @TableField(exist = false)
    private String[] logistics;

    /*订单运踪信息列表*/
    @TableField(exist = false)
    private List<OrdOrderLogistics> ordOrderLogistics;

    /*订单异常费用列表*/
    @TableField(exist = false)
    private List<OrdExceptionFee> ordExceptionFees;

    /*订单异常情况列表*/
    @TableField(exist = false)
    private List<OrdExceptionCondition> ordExceptionConditions;

    /**
     * 应付现金
     */
    @TableField("pay_cash")
    private BigDecimal payCash;
    /**
     * 油卡押金费
     */
    @TableField("oil_pledge")
    private BigDecimal oilPledge;

    /**
     * ETC费用
     */
    @TableField("etc_fee")
    private BigDecimal etcFee;
    /**
     * 报销单号
     */
    private String repaynumber;
    /**
     * 运输油卡分配
     */
    @TableField("trans_oil_fee")
    private BigDecimal transOilFee;

    /**
     * 现金分配
     */
    private BigDecimal cash;

    /**
     * 应付是否开发票
     */
    @TableField("is_yf_invoice")
    private String isYFInvoice;
    /**
     * 是否加入应收对账单
     */
    @TableField("is_add_to_bill")
    private String isAddToBill;

    /**
     * 是否加入应付对账单
     */
    @TableField("if_add_to_yfbill")
    private String ifAddToYfbill;

    /*收货地址信息*/
    @TableField(exist = false)
    private List<OrdPickupArrivalAdd> arrivalAdds;
    /*发货地址信息*/
    @TableField(exist = false)
    private List<OrdPickupArrivalAdd> pickupAdds;
    /*提箱凭证信息*/
    @TableField(exist = false)
    private OrdPickupArrivalAdd pickHuAddress;
    /*提货凭证信息*/
    @TableField(exist = false)
    private OrdPickupArrivalAdd pickCnAddress;
    /*还箱凭证信息*/
    @TableField(exist = false)
    private OrdPickupArrivalAdd returnCnAddress;

    /**
     * 装货日期
     */
    @Excel(name = "提送货日期")
    @TableField(exist = false)
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date pickupOrSendGoodsDate;

    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

    @Override
    public String toString() {
        return "OrdOrder{" +
                "id=" + id +
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
                ", upstreamRemark=" + upstreamRemark +
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
