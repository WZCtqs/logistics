package com.zhkj.lc.common.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
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
 *  主卡基础表
 * </p>
 *
 * @author wzc
 * @since 2019-02-11
 */
@Data
@ApiModel(value = "ReChargeVO")
public class ReChargeVO implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 充值id
	 */
	@ApiModelProperty(hidden = true)
	@TableId(value = "recharge_id", type = IdType.AUTO)
	private Integer rechargeId;


	/** 主卡号 */
	@ApiModelProperty(value = "主卡号")
	@Excel(name = "主卡号" )
	@TableField(exist = false)
	private String majorNumber;
	/**
	 * 油卡卡号
	 */
	@ApiModelProperty(value = "油卡卡号")
	@TableField(exist = false)
	@Excel(name = "副卡号")
	private String oilCardNumber;
	/**
	 * 油卡类型
	 */
	@ApiModelProperty(value = "油卡类型")
	@TableField(exist = false)
	@Excel(name = "油卡类型")
	private String cardType;

	/** 车牌号 */
	@ApiModelProperty(value = "车牌号")
	@TableField(exist = false)
	@Excel(name = "车牌号")
	private String plateNumber;


	/** 车牌类型 */
	@ApiModelProperty(value = "车辆类型")
	@Excel(name = "车辆类型")
	private String attribute;

	/**
	 * 所属公司
	 */
	@ApiModelProperty(value = "所属公司")
	@TableField(exist = false)
	@Excel(name = "所属公司")
	private String company;

	/**
	 * 部门名称
	 */
	@ApiModelProperty(value = "部门名称")
	@TableField("dept_name")
	@Excel(name = "部门")
	private String deptName;

	/**
	 * 申请日期
	 */
	@ApiModelProperty(value = "申请日期",example = "2018-12-12")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@TableField("apply_date")
	@Excel(name = "申请日期")
	private Date applyDate;

	/**
	 * 订单id(单结司机充值绑定的订单id)
	 */
	@TableField("order_id")
	@Excel(name = "订单号"    )
	private String orderId;

	/**
	 * 报销单号_外调车
	 */
	@ApiModelProperty(value = "报销单号")
	@TableField("reimburse_num")
	@Excel(name = "报销单号")
	private String reimburseNum;


	/**
	 * 总运费_外调车
	 */
	@ApiModelProperty(value = "总运费")
	@TableField("total_transport_cost")
	@Excel(name = "总运费")
	private BigDecimal totalTransportCost;


	/**
	 * 申请充值金额
	 */
	@ApiModelProperty(value = "申请充值金额")
	@TableField("recharge_sum")
	@Excel(name = "充值金额")
	private BigDecimal rechargeSum;


	/**
	 * 充值类型（0：正常，1：运费，2：外调车）
	 */
	@ApiModelProperty(value = "充值类型(0：正常，1：运费，2：外调车)")
	@TableField("recharge_type")
	@Excel(name = "充值类型")
	private String rechargeType;


	/**
	 * 油卡id
	 */
	@ApiModelProperty(value = "油卡id")
	@TableField("oil_card_id")
	private Integer oilCardId;

	@ApiModelProperty(value = "油卡开卡")
	@TableField(exist = false)
	@Excel(name = "开卡地")
	private String openCardPlace;
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


	/** 车主 */
	@ApiModelProperty(value = "车主")
	@TableField(exist = false)
	private String truckOwner;

	/**
	 * 起始地_外调车
	 */
	@ApiModelProperty(value = "起始地")
	@TableField("start_place")
	@Excel(name = "起始地")
	private String startPlace;
	/**
	 * 途径地_外调车
	 */
	@ApiModelProperty(value = "途径地_外调车")
	@TableField("pass_place")
	@Excel(name = "途径地")
	private String passPlace;
	/**
	 * 终止地_外调车
	 */
	@ApiModelProperty(value = "终止地")
	@TableField("end_place")
	@Excel(name = "终止地")
	private String endPlace;
	/**
	 * 箱号_外调车
	 */
	@ApiModelProperty(value = "箱号")
	@TableField("container_no")
	private String containerNo;





	/**
	 * 押金_外调车
	 */
	@ApiModelProperty(value = "押金")
	private BigDecimal deposit;
	/**
	 * 申请备注
	 */
	@ApiModelProperty(value = "申请备注")
	@TableField("apply_remark")
	private String applyRemark;


	/**
	 * 所属司机/车主id
	 */
	@ApiModelProperty(value = "充值申请人id")
	@TableField("owner_driver_id")
	private Integer ownerDriverId;

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
	 * 审核状态（0：通过，1：不通过）
	 */
	@ApiModelProperty(value = "审核状态")
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
	@Excel(name = "充值状态")
	private String isRechargeed;
	/**
	 * 充值时间
	 */

	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM")
	@TableField("recharge_date")
	@Excel(name = "充值日期")
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


	@ApiModelProperty(value = "月订单次数")
	@TableField("month_order_num")
	private Integer monthOrderNum;

	/**
	 * 月充值金额
	 */
	@ApiModelProperty(value = "月充值金额")
	@TableField("month_recharge_sum")
	private BigDecimal monthRechargeSum;

	@ApiModelProperty(value = "是否建议充值",readOnly = true)
	@TableField(exist = false)
	private String isSuggestRecharge;


	@ApiModelProperty(value = "充值日期筛选条件",readOnly = true,example = "2018-12")
	private String yearMonth;


}
