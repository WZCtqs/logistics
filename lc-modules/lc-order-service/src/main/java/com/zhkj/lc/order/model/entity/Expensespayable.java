package com.zhkj.lc.order.model.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.xiaoleilu.hutool.date.DateTime;
import com.zhkj.lc.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 应收对账单表
 * </p>
 *
 * @author wzc
 * @since 2019-02-15
 */
@Data
public class  Expensespayable extends Model< Expensespayable> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 对账单编号
     */
    @ApiModelProperty(value = "对账单编号")
    @TableField("account_pay_id")
    @Excel(name = "对账单编号")
    private String accountPayId;
    /**
     * 结算年月(开始)
     */
    @ApiModelProperty(value = "结算年月(开始)")
    @TableField("pay_date_start")
    @Excel(name = "结算年月(开始)")
    private Date payDateStart;
    /**
     * 结算年月(截止)
     */
    @ApiModelProperty(value = "结算年月(截止)")
    @TableField("pay_date_end")
    @Excel(name = "结算年月(截止)")
    private Date payDateEnd;

    /**
     * 日期范围
     */
    @ApiModelProperty(value = "日期范围")
    @TableField(exist = false)
    @Excel(name = "日期范围")
    private Date startAndEnd;
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    @TableField("customer_name")
    @Excel(name = "客户名称")
    private String customerName;
    /**
     * 订单类型
     */
    @ApiModelProperty(value = "订单类型")
    @TableField("order_type")
    @Excel(name = "订单类型")
    private String orderType;
    /**
     * 订单数量
     */
    @ApiModelProperty(value = "订单数量")
    @TableField("order_amount")
    @Excel(name = "订单数量")
    private Integer orderAmount;
    /**
     * 开票利率
     */
    @ApiModelProperty(value = "开票利率")
    @Excel(name = "开票利率")
    private BigDecimal rate;
    /**
     * 合计应收费用
     */
    @ApiModelProperty(value = "合计应收费用")
    @TableField("total_fee")
    @Excel(name = "合计应收费用")
    private BigDecimal totalFee;
    /**
     * 对账单状态(0:未结算;1.已提交;2:普通结算;3:开票结算)
     */
    @ApiModelProperty(value = "对账单状态(0:未结算;1.已提交;2:普通结算;3:开票结算)")
    @TableField("settlement_status")
    @Excel(name = "对账单状态")
    private String settlementStatus;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Excel(name = "备注")
    private String remark;
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
    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Integer tenantId;

    @TableField(exist = false)
    private int[] ids;

    /**
     * 订单编号数组
     */
    @TableField(exist = false)
    private String[] orderIds;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @TableField(exist = false)
    private String paramDate;
}
