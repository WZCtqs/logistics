package com.zhkj.lc.order.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.zhkj.lc.common.vo.DriverVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 车辆调度信息
 * </p>
 *
 * @author cb
 * @since 2019-01-03
 */
@Data
@TableName("ord_common_truck")
public class OrdCommonTruck extends Model<OrdCommonTruck> {

    private static final long serialVersionUID = 1L;

    /**
     * 普货订单编号
     */
    @TableId("order_id")
    private String orderId;
    /**
     * 车辆类型（0：自有车，1：外调车）
     */
    @TableField("truck_type")
    private String truckType;

    /**
     * 结算状态
     */
    @TableField("balance_status")
    private String balanceStatus;

    /**
     * 承运商id
     */
    @TableField("truck_team_id")
    private Integer truckTeamId;
    /**
     * 司机id
     */
    @TableField("mdriver_id")
    private Integer mdriverId;

    /**
     * 司机id
     */
    @TableField("sdriver_id")
    private Integer sdriverId;

    /**
     * 车牌号
     */
    @TableField("plate_number")
    private String plateNumber;
    /**
     * 应付单价
     */
    @TableField("pay_price")
    private BigDecimal payPrice;
    /**
     * 应付利率
     */
    @TableField("pay_rate")
    private BigDecimal payRate;
    /**
     * 车型（0：厢车，1：自卸，2：冷藏，3：平板）
     */
    @TableField("vehicle_type")
    private String vehicleType;
    /**
     * 车高
     */
    @TableField("vehicle_length")
    private String vehicleLength;
    /**
     * 运输费
     */
    @TableField("transport_fee")
    private BigDecimal transportFee;
    /**
     * 装货费
     */
    @TableField("pack_fee")
    private BigDecimal packFee;
    /**
     * 卸货费
     */
    @TableField("release_fee")
    private BigDecimal releaseFee;
    /**
     * 保险费
     */
    @TableField("insurance_fee")
    private BigDecimal insuranceFee;
    /**
     * 其他费用
     */
    @TableField("other_fee")
    private BigDecimal otherFee;
    /**
     * 结算方式（0：按单 1：按月）
     */
    @TableField("pay_type")
    private String payType;
    /**
     * 付款方式（0：现金，1：油卡）
     */
    @TableField("oil_payment")
    private BigDecimal oilPayment;
    /**
     * 费用总计
     */
    @TableField("total_fee")
    private BigDecimal totalFee;
    /**
     * 付款金额
     */
    @TableField("cash_payment")
    private BigDecimal cashPayment;
    /**
     * 计费里程
     */
    @TableField("charged_mileage")
    private Integer chargedMileage;
    /**
     * 路桥费
     */
    @TableField("luqiao_fee")
    private BigDecimal luqiaoFee;
    /**
     * 油费
     */
    @TableField("oil_fee")
    private BigDecimal oilFee;
    /**
     * 车辆调度备注
     */
    private String remarks;


    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;

    /**
     * 应付状态
     */
    @TableField("need_pay_status")
    private String needPayStatus;

    /**
     * 司机反馈
     */
    @TableField("feed_back")
    private String feedBack;


    @TableField("pay_cash")
    private BigDecimal payCash;

    private BigDecimal cash;

    /**
     * 油卡押金费
     */
    @TableField("oil_pledge")
    private BigDecimal oilPledge;
    /**
     * 运输油卡分配
     */
    @TableField("trans_oil_fee")
    private BigDecimal transOilFee;
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
     * 应付是否开发票
     */
    @TableField("is_yf_invoice")
    private String isYFInvoice;

    /**
     * 是否加入应付对账单
     */
    @TableField("if_add_to_yfbill")
    private String ifAddToYFBill;

    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

    @Override
    public String toString() {
        return "OrdCommonTruck{" +
        ", orderId=" + orderId +
        ", truckType=" + truckType +
        ", truckTeamId=" + truckTeamId +
        ", plateNumber=" + plateNumber +
        ", vehicleType=" + vehicleType +
        ", vehicleHeight=" + vehicleLength +
        ", transportFee=" + transportFee +
        ", packFee=" + packFee +
        ", releaseFee=" + releaseFee +
        ", insuranceFee=" + insuranceFee +
        ", otherFee=" + otherFee +
        ", payType=" + payType +

        ", totalFee=" + totalFee +

        ", chargedMileage=" + chargedMileage +
        ", luqiaoFee=" + luqiaoFee +
        ", oilFee=" + oilFee +
        ", remarks=" + remarks +
        
        "}";
    }
}
