package com.zhkj.lc.oilcard.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 油卡充值表
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
@ApiModel(description = "油卡充值实体类")
@TableName("oil_recharge")
public class OilRecharge extends Model<OilRecharge> {

    private static final long serialVersionUID = 1L;

    /**
     * 充值id
     */
    @ApiModelProperty(value = "充值id(添加的时候不用写,后台自动生成)")
    @TableId(value = "recharge_id", type = IdType.AUTO)
    private Integer rechargeId;
    /**
     * 油卡id
     */
    @ApiModelProperty(value = "油卡id(添加时必需)")
    @TableField("oil_card_id")
    private Integer oilCardId;

    /**
     * 主卡id
     */
    @ApiModelProperty(value = "主卡id")
    @TableField("major_id")
    private Integer majorId;
    /** 主卡号 */
    @ApiModelProperty(value = "主卡号")
    @TableField(exist = false)
    private String majorNumber;

    /** 油卡卡号 */
    @ApiModelProperty(value = "油卡卡号")
    @TableField(exist = false)
    private String oilCardNumber;

    /** 油卡类型 */
    @ApiModelProperty(value = "油卡类型")
    @TableField(exist = false)
    private String cardType;

    /** 办卡地点 */
    @ApiModelProperty(value = "办卡地点")
    @TableField(exist = false)
    private String openCardPlace;

    /** 运费油卡余额 */
    @ApiModelProperty(value = "运费油卡余额")
    @TableField(exist = false)
    private BigDecimal amount;

    /**
     * 车辆id
     */
    @ApiModelProperty(value = "车辆id(添加时必需)")
    @TableField("truck_id")
    private Integer truckId;

    /** 车牌号 */
    @ApiModelProperty(value = "车牌号")
    @TableField(exist = false)
    private String plateNumber;

    /** 车主 */
    @ApiModelProperty(value = "车主")
    @TableField(exist = false)
    private String truckOwner;

    /** 车辆类型 */
    @ApiModelProperty(value = "车辆类型")
    private String attribute;
    /**
     * 运费
     */
    @ApiModelProperty(value = "运费_正常充值")
    @TableField("transport_cost")
    private BigDecimal transportCost;
    /**
     * 编号
     */
    @ApiModelProperty(value = "编号_正常充值")
    private String no;
    /**
     * 申请日期
     */
    @ApiModelProperty(value = "申请日期",example = "2018-12-12")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField("apply_date")
    private Date applyDate;
    /**
     * 申请充值金额
     */
    @ApiModelProperty(value = "申请充值金额(金额都是两位小数)")
    @TableField("recharge_sum")
    private BigDecimal rechargeSum;
    /**
     * 申请备注
     */
    @ApiModelProperty(value = "申请备注")
    @TableField("apply_remark")
    private String applyRemark;
    /**
     * 充值类型（0：正常，1：运费，2：外调车）
     */
    @ApiModelProperty(value = "充值类型(0：正常，1：运费，2：外调车；添加时必需)")
    @TableField("recharge_type")
    private String rechargeType;
    /**
     * 所属司机/车主id
     */
    @ApiModelProperty(value = "充值申请人id")
    @TableField("owner_driver_id")
    private Integer ownerDriverId;
    /**
     * 订单id(单结司机充值绑定的订单id)
     */
    @ApiModelProperty(value = "订单id(单结司机充值绑定的订单id)")
    @TableField("order_id")
    private String orderId;
    /**
     * 司机姓名
     */
    @ApiModelProperty(value = "充值申请人")
    @TableField(exist = false)
    private String driverName;
    /**
     * 充值申请人手机号
     */
    @ApiModelProperty(value = "充值申请人手机号")
    @TableField(exist = false)
    private String driverPhone;
    /**
     * 起始地_外调车
     */
    @ApiModelProperty(value = "起始地_外调车")
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
    @ApiModelProperty(value = "终止地_外调车")
    @TableField("end_place")
    private String endPlace;
    /**
     * 箱号_外调车
     */
    @ApiModelProperty(value = "箱号_外调车")
    @TableField("container_no")
    private String containerNo;
    /**
     * 押金_外调车
     */
    @ApiModelProperty(value = "押金_外调车")
    private BigDecimal deposit;
    /**
     * 审核状态（0：通过，1：不通过）
     */
    @ApiModelProperty(value = "审核状态（0：通过，1：不通过）")
    @TableField("is_passed")
    private String isPassed;
    /**
     * 审核备注
     */
    @ApiModelProperty(value = "审核备注")
    @TableField("fail_remark")
    private String failRemark;
    /**
     * 充值时间
     */
    @ApiModelProperty(value = "充值时间(点击充值按钮，后台设置当前时间为充值时间)",example = "2018-12-12",readOnly = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField("recharge_date")
    private Date rechargeDate;
    /**
     * 部门名称_外调车
     */
    @ApiModelProperty(value = "部门名称_外调车")
    @TableField("dept_name")
    private String deptName;
    /**
     * 报销单号_外调车
     */
    @ApiModelProperty(value = "报销单号_外调车")
    @TableField("reimburse_num")
    private String reimburseNum;
    /**
     * 总运费_外调车
     */
    @ApiModelProperty(value = "总运费_外调车")
    @TableField("total_transport_cost")
    private BigDecimal totalTransportCost;
    /**
     * 月订单次数
     */
    @ApiModelProperty(value = "月订单次数")
    @TableField("month_order_num")
    private Integer monthOrderNum;
    /**
     * 月充值金额
     */
    @ApiModelProperty(value = "月充值金额")
    @TableField("month_recharge_sum")
    private BigDecimal monthRechargeSum;

    @ApiModelProperty(value = "年月日")
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date timeMonth;

    /**
     * 删除标志（0存在 1删除）
     */
    @ApiModelProperty(hidden = true)
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

    @Override
    protected Serializable pkVal() {
        return this.rechargeId;
    }

}
