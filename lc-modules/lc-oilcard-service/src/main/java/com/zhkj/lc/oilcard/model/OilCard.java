package com.zhkj.lc.oilcard.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhkj.lc.common.annotation.Excel;
import com.zhkj.lc.common.vo.OilMajorVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 油卡管理
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@ApiModel(description = "油卡基础实体类")
@Data
@TableName("oil_card")
public class OilCard extends Model<OilCard> {

    private static final long serialVersionUID = 1L;

	public OilCard() {
	}

	public OilCard(String oilCardNumber) {
		this.oilCardNumber = oilCardNumber;
	}

	public OilCard(String oilCardNumber, String majorNumber) {
		this.oilCardNumber = oilCardNumber;
		this.majorNumber = majorNumber;
	}

	@Override
	public boolean equals(Object obj) {
		OilCard o = (OilCard) obj;
		return oilCardNumber.equals(o.getOilCardNumber());
	}

	/**
     * 油卡id
     */
    @ApiModelProperty(value = "油卡id")
    @TableId(value = "oil_card_id", type = IdType.AUTO)
    private Integer oilCardId;

    /**
     * 车辆id
     */
    @ApiModelProperty(value = "车辆id")
    @TableField("truck_id")
    private Integer truckId;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    @Excel(name = "车牌号")
    @TableField(exist = false)
    private String plateNumber;

    /**
     * 车主
     */
    @ApiModelProperty(value = "车主",readOnly = true)
    @Excel(name = "车主")
    @TableField(exist = false)
    private String truckOwner;

    /**
     * 车辆类型
     */
    @ApiModelProperty(value = "车辆类型")
    @Excel(name = "车辆类型")
    @TableField(exist = false)
    private String attribute;

    /**
     * 车主联系方式
     */
    @ApiModelProperty(value = "车主联系方式",readOnly = true)
    @TableField(exist = false)
    private String truckOwnerPhone;
    /**
     * 银行卡户主
     */
    @ApiModelProperty(value = "银行卡户主")
    @Excel(name = "银行卡户主")
    @TableField("bank_owner")
    private String bankOwner;
    /**
     * 开户行
     */
    @ApiModelProperty(hidden = true)
    @Excel(name = "开户行")
    @TableField("bank_name")
    private String bankName;
    /**
     * 银行卡号
     */
    @ApiModelProperty(hidden = true)
    @Excel(name = "银行卡号")
    @TableField("bank_number")
    private String bankNumber;
    /**
     * 联系方式
     */
    @ApiModelProperty(hidden = true)
    @Excel(name = "联系方式")
    @TableField("bank_owner_phone")
    private String bankOwnerPhone;
    /**
     * 油卡号
     */
    @ApiModelProperty(value = "油卡号")
    @Excel(name = "油卡号")
    @TableField("oil_card_number")
    private String oilCardNumber;
    /**
     * 押金
     */
    @ApiModelProperty(value = "押金(金额都是2位小数)")
    @Excel(name = "押金")
    private BigDecimal deposit;
    /**
     * 油卡类型
     */
    @ApiModelProperty(value = "油卡类型")
    @Excel(name = "油卡类型")
    @TableField("card_type")
    private String cardType;
    /**
     * 油卡状态
     */
    @ApiModelProperty(value = "油卡状态(默认正常)")
    @Excel(name = "油卡状态")
    @TableField("card_status")
    private String cardStatus;

    /**
     * 主卡id
     */
    @ApiModelProperty(value = "主卡id")
    @TableField("major_id")
    private Integer majorId;
    /**
     * 主卡号
     */
    @ApiModelProperty(value = "主卡号")
    @Excel(name = "充值主卡号")
    @TableField(exist = false)
    private String majorNumber;

    /**
     * 申请id
     */
    @ApiModelProperty(value = "申请id")
    @TableField("apply_id")
    private Integer applyId;

    /**
     * 所属司机/车主id
     */
    @ApiModelProperty(value = "油卡申请人id")
    @TableField("owner_driver_id")
    private Integer ownerDriverId;
    /**
     * 司机姓名
     */
    @ApiModelProperty(value = "油卡申请人")
    @Excel(name = "油卡申请人")
    @TableField(exist = false)
    private String driverName;
    /**
     * 油卡申请人手机号
     */
    @ApiModelProperty(value = "油卡申请人手机号")
    @Excel(name = "油卡申请人手机号")
    @TableField(exist = false)
    private String driverPhone;

    /**
     * 数量
     */
    @ApiModelProperty(value = "持卡数量")
    @Excel(name = "持卡数量")
    @TableField("card_quantity")
    private Integer cardQuantity;
    /**
     * 办卡地点
     */
    @ApiModelProperty(value = "办卡地点")
    @Excel(name = "办卡地点")
    @TableField("open_card_place")
    private String openCardPlace;
    /**
     * 开卡日期
     */
    @ApiModelProperty(value = "开卡时间",example = "2018-12-12")
    @Excel(name = "开卡时间")
    @TableField("open_date")
    @JsonFormat(pattern = "yyyy-MM-dd") // ,timezone = "GMT+8"
    private Date openDate;

    /**
     * 运费油卡余额
     */
    @ApiModelProperty(value = "运费油卡余额")
    @Excel(name = "运费油卡余额")
    private BigDecimal amount;
    /**
     * 退卡时间
     */
    @ApiModelProperty(value = "退卡时间",example = "2018-12-12")
    @Excel(name = "退卡时间")
    @TableField("return_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date returnDate;

    /**
     * 退卡记录
     */
    @ApiModelProperty(value = "退卡记录，只能上传图片")
    @Excel(name = "退卡记录(图片路径)")
    @TableField("return_record")
    private String returnRecord;

    /**
     * 开卡日期的时间范围
     */
    @ApiModelProperty(value = "开卡时间范围开始时间",example = "2018-12-12")
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date beginTime;
    @ApiModelProperty(value = "开卡时间范围结束时间",example = "2018-12-12")
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endTime;

    @TableField(exist = false)
    private String[] truckIds;

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

    @TableField(exist = false)
    private String majorName;

    @TableField(exist = false)
    private String majorCompany;

    @Override
    protected Serializable pkVal() {
        return this.oilCardId;
    }

    @Override
    public String toString() {
        return "OilCard{" +
                "oilCardId=" + oilCardId +
                ", oilCardNumber='" + oilCardNumber + '\'' +
                ", deposit=" + deposit +
                ", cardType='" + cardType + '\'' +
                ", cardStatus='" + cardStatus + '\'' +
                ", majorId=" + majorId +
                ", majorNumber='" + majorNumber + '\'' +
                ", applyId=" + applyId +
                ", cardQuantity=" + cardQuantity +
                ", openCardPlace='" + openCardPlace + '\'' +
                ", openDate=" + openDate +
                ", amount=" + amount +
                ", returnDate=" + returnDate +
                ", returnRecord='" + returnRecord + '\'' +
                ", truckId=" + truckId +
                ", plateNumber='" + plateNumber + '\'' +
                ", truckOwner='" + truckOwner + '\'' +
                ", attribute='" + attribute + '\'' +
                ", truckOwnerPhone='" + truckOwnerPhone + '\'' +
                ", bankOwner='" + bankOwner + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankNumber='" + bankNumber + '\'' +
                ", bankOwnerPhone='" + bankOwnerPhone + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                ", tenantId=" + tenantId +
                '}';
    }
}
