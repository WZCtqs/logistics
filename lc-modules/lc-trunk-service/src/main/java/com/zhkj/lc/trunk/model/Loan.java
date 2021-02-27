package com.zhkj.lc.trunk.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <p>
 * 借款管理
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@ApiModel(description = "借款信息实体类")
@TableName("loan")
@Data
public class Loan extends Model<Loan> {

    private static final long serialVersionUID = 1L;

    /**
     * 借款申请id
     */
    @ApiModelProperty(value = "借款申请id")
    @TableId(value = "loan_id", type = IdType.AUTO)
    private Integer loanId;
    /**
     * 是否司机
     */
    @ApiModelProperty(value = "是否司机(0司机1车主)")
    @TableField("is_driver")
    private Integer isDriver;
    /**
     * 车辆id
     */
    @ApiModelProperty(value = "车辆id(不保存，该字段暂时不用)")
    @TableField("truck_id")
    private Integer truckId;
    /**
     * 车主
     */
    @ApiModelProperty(value = "借款申请人(存司机或者车主id)")
    @TableField("apply_man")
    private Integer applyMan;
    /**
     * 申请日期
     */
    @ApiModelProperty(value = "申请日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField("apply_date")
    private Date applyDate;
    /**
     * 借款金额
     */
    @ApiModelProperty(value = "借款金额")
    @TableField("apply_sum")
    private BigDecimal applySum;
    /**
     * 借款理由
     */
    @ApiModelProperty(value = "借款理由")
    @TableField("apply_reason")
    private String applyReason;
    /**
     * 借款账号
     */
    @ApiModelProperty(value = "借款账号")
    @TableField("loan_card_number")
    private String loanCardNumber;
    /**
     * 借款状态
     */
    @ApiModelProperty(value = "借款状态")
    private String status;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 删除标志
     */
    @ApiModelProperty(value = "司机id")
    @TableField("del_flag")
    private String delFlag;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
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
    @ApiModelProperty(value = "更新者")
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
    @ApiModelProperty(value = "租户id")
    @TableField("tenant_id")
    private Integer tenantId;
    /**
     * 还款方式
     */
    @ApiModelProperty(value = "还款方式")
    @TableField("repayment_way")
    private Integer repaymentWay;
    /**
     * 还款金额
     */
    @ApiModelProperty(value = "还款金额")
    @TableField(exist = false)
    private double repaymentMoney;

    /**
     * 还款利率
     */
    @ApiModelProperty(value = "还款利率")
    @TableField("repayment_rate")
    private String repaymentRate;
    /**
     * 还款日期
     */
    @ApiModelProperty(value = "还款日期")
    @TableField("repayment_date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date repaymentDate;
    /**
     * 借款id数组
     */
    @TableField(exist = false)
    private int[] loanIds;

    /**
     * 开始时间区间
     */
    @ApiModelProperty(value = "开始时间区间")
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date beginTime;
    /**
     * 截止时间区间
     */
    @ApiModelProperty(value = "截止时间区间")
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endTime;
    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    @TableField(exist = false)
    private String plateNumber;
    /**
     * 已还期数
     */
    @ApiModelProperty(value = "已还期数")
    @TableField(exist = false)
    private Integer returnPeriod;
    /**
     * 还款总期数
     */
    @ApiModelProperty(value = "还款总期数")
    @TableField(exist = false)
    private Integer returnPeriodSum;
    /**
     * 司机姓名
     */
    @TableField(exist = false)
    private String driverName;
    /**
     * 车主姓名
     */
    @TableField(exist = false)
    private String truckOwnName;

    /**
     * 车辆对象
     */
    @TableField(exist = false)
    private TruTruck truTruck;

    /**
     * 车辆对象
     */
    @TableField(exist = false)
    private TruDriver truDriver;



    @Override
    protected Serializable pkVal() {
        return this.loanId;
    }

    @Override
    public String toString() {
        return "Loan{" +
        "loanId=" + loanId +
        ", truckId=" + truckId +
        ", applyMan=" + applyMan +
        ", applyDate=" + applyDate +
        ", applySum=" + applySum +
        ", applyReason=" + applyReason +
        ", loanCardNumber=" + loanCardNumber +
        ", status=" + status +
        ", remark=" + remark +
        ", delFlag=" + delFlag +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        ", tenantId=" + tenantId +
        ", repaymentWay=" + repaymentWay +
        ", repaymentDate=" + repaymentDate +
        ", repaymentRate=" + repaymentRate +
        "}";
    }
}
