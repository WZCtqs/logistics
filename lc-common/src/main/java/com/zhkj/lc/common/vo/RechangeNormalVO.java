package com.zhkj.lc.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: wzc
 * @Date: 2018/12/8 09:48
 * @Description:
 */
@Data
public class RechangeNormalVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 充值id
     */
    private Integer rechargeId;
    /**
     * 油卡id
     */
    private Integer oilCardId;

    /**
     * 车辆id
     */
    private Integer truckId;

    /** 车牌号 */
    private String plateNumber;

    /** 车主 */
    private String truckOwner;

    /** 车辆类型 */
    private String attribute;
    /**
     * 运费
     */
    private BigDecimal transportCost;
    /**
     * 编号
     */
    private String no;

    /** 主卡号 */
    private String majorNumber;
    /**
     * 油卡卡号
     */
    private String oilCardNumber;

    /** 油卡类型 */
    private String cardType;

    /** 办卡地点 */
    private String openCardPlace;
    /**
     * 主卡id
     */
    private Integer majorId;
    /**
     * 申请备注
     */
    private String applyRemark;
    /**
     * 充值类型（0：正常，1：运费，2：外调车）
     */
    private String rechargeType;
    /**
     * 申请日期
     */
    private Date applyDate;
    /**
     * 申请充值金额
     */
    private BigDecimal rechargeSum;
    /**
     * 所属司机/车主id
     */
    private Integer ownerDriverId;
    /**
     * 订单id(单结司机充值绑定的订单id)
     */
    private String orderId;
    /**
     * 司机姓名
     */
    private String driverName;
    /**
     * 是否是车主
     */
    private String isOwner;
    /**
     * 月订单次数
     */
    private Integer monthOrderNum;
    /**
     * 月充值金额
     */
    private BigDecimal monthRechargeSum;


    private String isSuggestRecharge;


    private String timeMonth;

    /**
     * 审核状态（0：通过，1：不通过）
     */
    private String isPassed;
    /**
     * 审核备注
     */
    private String failRemark;
    /**
     * 充值状态（充值时间不为空：充值成功）
     */
    private String isRechargeed;
    /**
     * 充值时间
     */
    private Date rechargeDate;

    /**
     * 时间范围查询参数
     */
	/*@ApiModelProperty(hidden = true)
	private Map<String, Object> params;*/


    private String beginTime;

    private String endTime;

    /**
     * 删除标志（0存在 1删除）
     */
    private String delFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */

    private String updateBy;
    /**
     * 更新时间
     */

    private Date updateTime;
    /**
     * 租户id
     */
    private Integer tenantId;
}
