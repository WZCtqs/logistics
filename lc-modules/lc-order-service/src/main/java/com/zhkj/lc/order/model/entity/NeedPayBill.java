package com.zhkj.lc.order.model.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhkj.lc.common.annotation.Excel;
import com.zhkj.lc.order.dto.CnNeedPayDetail;
import com.zhkj.lc.order.dto.NeedPayBaseModel;
import com.zhkj.lc.order.dto.OrderBill;
import com.zhkj.lc.order.dto.PhNeedPayDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 应付对账单
 * </p>
 *
 * @author cb
 * @since 2019-02-19
 */
@Data
@TableName("need_pay_bill")
public class NeedPayBill extends Model<NeedPayBill> {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "对账单id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 应付账单编号
     */
    @ApiModelProperty(value = "对账单编号")
    @Excel(name = "对账单编号")
    @TableField("account_pay_id")
    private String accountPayId;
    /**
     * 结算日期范围
     */
    @ApiModelProperty(value = "日期范围")
    @Excel(name = "日期范围")
    @TableField(exist = false)
    private String dateLimit;
    /**
     * 结算年月(开始)
     */
    @ApiModelProperty(value = "结算年月(开始)")
    @TableField("date_start")
    private Date dateStart;
    /**
     * 结算年月(截止)
     */
    @ApiModelProperty(value = "结算年月(截止)")
    @TableField("date_end")
    private Date dateEnd;
    /**
     * 司机id
     */
    @ApiModelProperty(value = "司机id")
    @TableField("driver_id")
    private Integer driverId;

    @ApiModelProperty(value = "司机姓名")
    /*司机姓名*/
//    @Excel(name = "司机姓名")
    @TableField(exist = false)
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    @TableField(exist = false)
    private String driverPhone;
    /**
     * 订单类型
     */
    @ApiModelProperty(value = "订单类型")
    @TableField("order_type")
    private String orderType;
    /**
     * 订单数量
     */
    @ApiModelProperty(value = "订单数量")
    @Excel(name = "订单数量")
    @TableField("order_amount")
    private Integer orderAmount;
    /**
     * 应付总金额
     */
    @ApiModelProperty(value = "应付总金额")
    @Excel(name = "合计费用")
    @TableField("total_fee")
    private BigDecimal totalFee;
    /**
     * 利率
     */
    @ApiModelProperty(value = "利率")
    @Excel(name = "利率")
    private BigDecimal rate;
    /**
     * 合计费用（含发票）
     */
    @ApiModelProperty(value = "合计费用（含发票）")
    @Excel(name = "合计费用（含发票）")
    @TableField(exist = false)
    private BigDecimal totalFeeRate;
    /**
     * 正常油卡费
     */
    @ApiModelProperty(value = "正常油卡费")
    @Excel(name = "正常油卡费")
    @TableField("normal_oilcard_fee")
    private BigDecimal normalOilcardFee;
    /**
     * 现金分配
     */
    @ApiModelProperty(value = "现金分配")
//    @Excel(name = "现金分配")
    @TableField("pay_cash")
    private BigDecimal payCash;
    /**
     * 油卡押金费
     */
    @ApiModelProperty(value = "油卡押金费")
    @TableField("oil_pledge")
    private BigDecimal oilPledge;
    /**
     * 应付现金
     */
    @ApiModelProperty(value = "应付现金")
    @Excel(name = "应付现金")
    @TableField("need_pay_cash")
    private BigDecimal needPayCash;
    /**
     * etc费用
     */
    @ApiModelProperty(value = "etc费用")
    @Excel(name = "ETC费用")
    @TableField("etc_fee")
    private BigDecimal etcFee;
    /**
     * 运输油卡费
     */
    @ApiModelProperty(value = "运输油卡费")
    @Excel(name = "运输油卡费")
    @TableField("freight_oilcard_fee")
    private BigDecimal freightOilcardFee;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Excel(name = "备注")
    private String remark;
    /**
     * 报销单号
     */
    private String repaynumber;
    /**
     * 运输油卡费余额
     */
    @TableField(exist = false)
    private BigDecimal oilTransFee;
    /**
     * 是否含税
     */
    @ApiModelProperty(value = "是否含税")
    @TableField("use_rate")
    private String useRate;
    /**
     * 对账单状态(0:未结算;1.已提交;2:普通结算;3:开票结算)
     */
    @ApiModelProperty(value = "对账单状态(0:未结算;1.已提交;2:普通结算;3:开票结算)")
    @Excel(name = "对账单状态")
    @TableField("settlement_status")
    private String settlementStatus;
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
    @ApiModelProperty(value = "创建时间")
    @Excel(name = "创建时间")
    @TableField("create_time")
    private Date createTime;
    /**
     * 反馈信息
     */
    @ApiModelProperty(value = "反馈信息")
    @Excel(name = "反馈信息")
    private String feedback;
    /**
     * 反馈金额
     */
    @ApiModelProperty(value = "反馈金额")
    @TableField("feedback_money")
    private BigDecimal feedbackMoney;
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
    @ApiModelProperty(value = "租户id")
    @TableField("tenant_id")
    private Integer tenantId;

    /**
     * 车牌号
     */
    @TableField("plate_number")
    private String plateNumber;

    @ApiModelProperty(value = "普货订单集合")
    @TableField(exist = false)
    List<PhNeedPayDetail> ppdList;

    @ApiModelProperty(value = "集装箱订单集合")
    @TableField(exist = false)
    List<CnNeedPayDetail> cnpdList;


    @TableField(exist = false)
    List<OrderBill> orderBills;

    /*结算结果集合*/
    @TableField(exist = false)
    private OrderSettlement orderSettlementStatus;
    //车主
    @TableField(exist = false)
    private String truckownName;

    @TableField(exist = false)
    private String truckownPhone;

    /*合计费用*/
    @TableField(exist = false)
    private BigDecimal allTotalFee;

    @TableField(exist = false)
    private BigDecimal allTotalFeeRate;

    /*压车费*/
    @TableField(exist = false)
    private BigDecimal ycFee;

    @TableField(exist = false)
    private BigDecimal ycFeeRate;


    @TableField(exist = false)
    private BigDecimal otherFee;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "NeedPayBill{" +
                "id=" + id +
                ", accountPayId=" + accountPayId +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", driverId=" + driverId +
                ", orderType=" + orderType +
                ", orderAmount=" + orderAmount +
                ", rate=" + rate +
                ", totalFee=" + totalFee +
                ", normalOilcardFee=" + normalOilcardFee +
                ", freightOilcardFee=" + freightOilcardFee +
                ", payCash=" + payCash +
                ", useRate=" + useRate +
                ", settlementStatus=" + settlementStatus +
                ", feedback=" + feedback +
                ", delFlag=" + delFlag +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateBy=" + updateBy +
                ", updateTime=" + updateTime +
                ", tenantId=" + tenantId +
                "}";
    }
}
