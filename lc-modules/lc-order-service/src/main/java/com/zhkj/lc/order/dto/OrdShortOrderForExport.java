package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhkj.lc.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 盘短管理信息
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
public class OrdShortOrderForExport extends Model<OrdShortOrderForExport> {

    private static final long serialVersionUID = 1L;

    /**
     * 盘短订单编号
     */
    @TableField("order_id")
    @Excel(name = "订单编号")
    private String orderId;
    /**
     * 托运客户
     */
    @Excel(name = "托运客户")
    private String customer;
    /**
     * 业务日期
     */
    @Excel(name = "业务日期")
    @TableField("order_date")
    private Date orderDate;
    /**
     * 盘短类型
     */
    @Excel(name = "业务类型")
    @TableField("short_type")
    private String shortType;
    /**
     * 运输路线
     */
    @Excel(name = "运输路线")
    @TableField("trans_line")
    private String transLine;
    /**
     * 班列日期
     */
    @Excel(name = "班列日期")
    @TableField("class_date")
    private Date classDate;
    /**
     * 提箱日期
     */
    @Excel(name = "提箱日期")
    @TableField("pickup_cn_date")
    private Date pickupCnDate;
    /**
     * 承运单位
     */
    @Excel(name = "承运单位")
    private String carrier;
    /**
     * 舱位号
     */
    @Excel(name = "舱位号")
    @TableField("class_order")
    private String classOrder;
    /**
     * 业务员
     */
    @Excel(name = "业务员")
    private String salesman;
    /**
     * 确认人（陆港签收人）
     */
    @Excel(name = "确认人")
    @TableField("receiver_people")
    private String receiverPeople;
    /**
     * 货名
     */
    @Excel(name = "货物名称")
    @TableField("goods_name")
    private String goodsName;
    /**
     * 重量
     */
    @Excel(name = "重量")
    private Double weight;
    /**
     * 件数
     */
    @Excel(name = "数量")
    @TableField("goods_sum")
    private Integer goodsSum;
    /**
     * 空重箱
     */
    @Excel(name = "空重箱")
    @TableField("is_heavy")
    private String isHeavy;
    /**
     * 箱号
     */
    @Excel(name = "箱号")
    @TableField("container_no")
    private String containerNo;
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
     * 铅封号
     */
    @Excel(name = "铅封号")
    @TableField("seal_number")
    private String sealNumber;
    /**
     * 异常情况
     */
    @Excel(name = "异常情况")
    private String exception;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;
    /**
     * 调度员
     */
    @Excel(name = "调度员")
    private String scheduleman;
    /**
     * 司机id
     */
    @TableField("driver_id")
    private Integer driverId;
    /**
     * 司机姓名
     */
    @Excel(name = "司机姓名")
    private String driverName;
    /**
     * 车牌号
     */
    @Excel(name = "车牌号")
    @TableField("plate_number")
    private String plateNumber;
    /**
     * 短驳次数
     */
    @Excel(name = "短驳次数")
    @TableField("short_trans_sum")
    private Integer shortTransSum;
    /**
     * 应收单价
     */
    @Excel(name = "应收单价")
    @TableField("rec_price")
    private BigDecimal recPrice;
    /**
     * 应收金额
     */
    @Excel(name = "应收金额")
    private BigDecimal receivables;
    /**
     * 应收备注
     */
    @Excel(name = "应收备注")
    @TableField("receivables_remark")
    private String receivablesRemark;
    /**
     * 应付单价
     */
    @Excel(name = "应付单价")
    private BigDecimal price;
    /**
     * 应付金额
     */
    @Excel(name = "应付金额")
    @TableField("need_pay")
    private BigDecimal needPay;
    /**
     * 应付备注
     */
    @Excel(name = "应付备注")
    @TableField("need_pay_remark")
    private String needPayRemark;

    /**
     * 是否加入对账单
     */
    private Integer isaddbill;
    /**
     * 删除标志（0代表存在 2代表删除）
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

    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

}
