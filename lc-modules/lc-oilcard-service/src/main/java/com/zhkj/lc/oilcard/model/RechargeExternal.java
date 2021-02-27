package com.zhkj.lc.oilcard.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 外调车油卡充值
 * </p>
 *
 * @author ckj
 * @since 2018-12-14
 */
@Data
@ApiModel(description = "外调车油卡充值实体类")
@TableName("oil_recharge")
public class RechargeExternal extends Model<RechargeExternal> {

    private static final long serialVersionUID = 1L;

    /**
     * 充值id
     */
    @ApiModelProperty(hidden = true)
    @TableId(value = "recharge_id", type = IdType.AUTO)
    private Integer rechargeId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    @Excel(name="部门名称")
    @TableField("dept_name")
    private String deptName;

    /**
     * 油卡id
     */
    @ApiModelProperty(value = "油卡id")
    @TableField("oil_card_id")
    private Integer oilCardId;
    /**
     * 主卡id
     */
    @ApiModelProperty(value = "主卡id")
    @TableField("major_id")
    private Integer majorId;
    /**
     * 车辆id
     */
    @ApiModelProperty(value = "车辆id")
    @TableField("truck_id")
    private Integer truckId;

    /** 车牌类型 */
    @ApiModelProperty(value = "车牌类型")
    @Excel(name="车牌类型")
    private String attribute;

    /** 车牌号 */
    @ApiModelProperty(value = "车牌号")
    @Excel(name="车牌号")
    @TableField(exist = false)
    private String plateNumber;

    /** 车主 */
    @ApiModelProperty(value = "车主")
    @Excel(name="车主")
    @TableField(exist = false)
    private String truckOwner;

    /**
     * 起始地_外调车
     */
    @ApiModelProperty(value = "起始地")
    @Excel(name="起始地")
    @TableField("start_place")
    private String startPlace;
    /**
     * 途径地_外调车
     */
    @ApiModelProperty(value = "途径地_外调车")
    @TableField("pass_place")
    private String passPlace;
    /**
     * 终止地_外调车
     */
    @ApiModelProperty(value = "终止地")
    @Excel(name="终止地")
    @TableField("end_place")
    private String endPlace;
    /**
     * 箱号_外调车
     */
    @ApiModelProperty(value = "箱号")
    @Excel(name="箱号")
    @TableField("container_no")
    private String containerNo;
    /**
     * 报销单号_外调车
     */
    @ApiModelProperty(value = "报销单号")
    @Excel(name="报销单号")
    @TableField("reimburse_num")
    private String reimburseNum;
    /**
     * 总运费_外调车
     */
    @ApiModelProperty(value = "总运费")
    @Excel(name="总运费")
    @TableField("total_transport_cost")
    private BigDecimal totalTransportCost;
    /** 主卡号 */
    @ApiModelProperty(value = "主卡号")
    @Excel(name="主卡号")
    @TableField(exist = false)
    private String majorNumber;
    /**
     * 油卡卡号
     */
    @ApiModelProperty(value = "油卡卡号")
    @Excel(name="油卡卡号")
    @TableField(exist = false)
    private String oilCardNumber;
    /**
     * 油卡类型
     */
    @ApiModelProperty(value = "油卡类型")
    @Excel(name="油卡类型")
    @TableField(exist = false)
    private String cardType;
    /**
     * 押金_外调车
     */
    @ApiModelProperty(value = "押金")
    @Excel(name="押金")
    private BigDecimal deposit;
    /**
     * 申请备注
     */
    @ApiModelProperty(value = "申请备注")
    @TableField("apply_remark")
    private String applyRemark;
    /**
     * 充值类型（0：正常，1：运费，2：外调车）
     */
    @ApiModelProperty(value = "充值类型(0：正常，1：运费，2：外调车)")
    @Excel(name="充值类型")
    @TableField("recharge_type")
    private String rechargeType;
    /**
     * 申请日期
     */
    @ApiModelProperty(value = "申请日期",example = "2018-12-12")
    @Excel(name="申请日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("apply_date")
    private Date applyDate;
    /**
     * 申请充值金额
     */
    @ApiModelProperty(value = "申请充值金额")
    @Excel(name="申请充值金额")
    @TableField("recharge_sum")
    private BigDecimal rechargeSum;
    /**
     * 所属司机/车主id
     */
    @ApiModelProperty(value = "充值申请人id")
    @TableField("owner_driver_id")
    private Integer ownerDriverId;
    /**
     * 订单id(单结司机充值绑定的订单id)
     */
    @ApiModelProperty(value = "充值申请人id")
    @TableField("order_id")
    private String orderId;
    /**
     * 司机姓名
     */
    @ApiModelProperty(value = "充值申请人")
    @Excel(name = "充值申请人")
    @TableField(exist = false)
    private String driverName;
    /**
     * 充值申请人手机号
     */
    @ApiModelProperty(value = "充值申请人手机号")
    @Excel(name = "充值申请人手机号")
    @TableField(exist = false)
    private String driverPhone;

    /**
     * 审核状态（0：通过，1：不通过）
     */
    @ApiModelProperty(value = "审核状态")
    @Excel(name="审核状态(是否通过)")
    @TableField("is_passed")
    private String isPassed;
    /**
     * 审核备注
     */
    @ApiModelProperty(value = "审核备注")
    @TableField("fail_remark")
    private String failRemark;
    /**
     * 是否充值成功
     */
    @ApiModelProperty(value = "是否充值成功",readOnly = true)
    @Excel(name="充值状态")
    private String isRechargeed;
    /**
     * 充值时间
     */
    @ApiModelProperty(value = "充值时间",readOnly = true,example = "2018-12-12")
    @Excel(name="充值时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("recharge_date")
    private Date rechargeDate;

    /**
     * 时间范围查询参数
     */
    @ApiModelProperty(value = "申请时间范围开始时间",example = "2018-12-12")
    @TableField(exist = false)
    private String beginTime;
    @ApiModelProperty(value = "申请时间范围结束时间",example = "2018-12-12")
    @TableField(exist = false)
    private String endTime;

    @TableField(exist = false)
    private String[] driverIds;

    /**
     * 删除标志（0存在 1删除）
     */
    @ApiModelProperty(hidden = true)
    @TableField("del_flag")
    private String delFlag;
    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @TableField("create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新者
     */
    @ApiModelProperty(hidden = true)
    @TableField("update_by")
    private String updateBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    @TableField("update_time")
    private Date updateTime;
    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    @TableField("tenant_id")
    private Integer tenantId;

    @TableField(exist = false)
    private String rechargeTime;


    @Override
    protected Serializable pkVal() {
        return this.rechargeId;
    }

    @Override
    public String toString() {
        return "RechargeExternal{" +
                "rechargeId=" + rechargeId +
                ", deptName='" + deptName + '\'' +
                ", oilCardId=" + oilCardId +
                ", majorId=" + majorId +
                ", truckId=" + truckId +
                ", attribute='" + attribute + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", truckOwner='" + truckOwner + '\'' +
                ", startPlace='" + startPlace + '\'' +
                ", endPlace='" + endPlace + '\'' +
                ", containerNo='" + containerNo + '\'' +
                ", reimburseNum='" + reimburseNum + '\'' +
                ", totalTransportCost=" + totalTransportCost +
                ", oilCardNumber='" + oilCardNumber + '\'' +
                ", cardType='" + cardType + '\'' +
                ", deposit=" + deposit +
                ", rechargeType='" + rechargeType + '\'' +
                ", applyDate=" + applyDate +
                ", rechargeSum=" + rechargeSum +
                ", ownerDriverId=" + ownerDriverId +
                ", driverName='" + driverName + '\'' +
                ", driverPhone='" + driverPhone + '\'' +
                ", isPassed='" + isPassed + '\'' +
                ", applyRemark'" + applyRemark + '\'' +
                ", failRemark='" + failRemark + '\'' +
                ", isRechargeed='" + isRechargeed + '\'' +
                ", rechargeDate=" + rechargeDate +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", delFlag='" + delFlag + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                ", tenantId=" + tenantId +
                '}';
    }
}
