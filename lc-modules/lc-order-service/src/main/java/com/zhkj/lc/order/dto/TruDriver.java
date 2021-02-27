package com.zhkj.lc.order.dto;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhkj.lc.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 司机信息表
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@ApiModel(description = "司机信息实体类")
@TableName("tru_driver")
@Data
public class TruDriver extends Model<TruDriver> {

    private static final long serialVersionUID = 1L;

    /**
     * 司机id
     */
    @ApiModelProperty(value = "司机id")
    @TableId(value = "driver_id", type = IdType.AUTO)
    private Integer driverId;
    /**
     * 车队id
     */
    @ApiModelProperty(value = "车队id")
    @TableField(value = "truck_team_id")
    private Integer truckTeamId;

    @TableField(exist = false)
    private String isTrust;

    @TableField(exist = false)
    private String payWay;

    /**
     * 车队ids
     */
    @TableField(exist = false)
    private int[] truckTeamIds;
    /**
     * 司机姓名
     */
    @ApiModelProperty(value = "司机姓名")
    @Excel(name="司机姓名")
    @TableField("driver_name")
    private String driverName;
    /**
     * 微信头像
     */
    @ApiModelProperty(value = "微信头像")
    @TableField("wx_photo")
    private String wxPhoto;
    /**
     * 微信昵称
     */
    @ApiModelProperty(value = "微信昵称")
    @TableField("wx_name")
    private String wxName;
    /**
     * 司机状态
     */
    @ApiModelProperty(value = "司机状态")
    private String status;
    /**
     * 是否是车主
     */
    @ApiModelProperty(value = "是否是车主")
    @TableField("is_owner")
    private String isOwner;
    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄")
    @TableField("driver_age")
    private Integer driverAge;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    @Excel(name="身份证号")
    @TableField("idcard_number")
    private String idcardNumber;

    /**
     * 身份证照片（正）
     */
    @ApiModelProperty(value = "身份证照片（正）")
    @TableField("idcard_photo_up")
    private String idcardPhotoUp;

    @TableField(exist = false)
    private String[] idcardPhotoUpArr;
    /**
     * 身份证照片（反）
     */
    @ApiModelProperty(value = "身份证照片（反）")
    @TableField("idcard_photo_down")
    private String idcardPhotoDown;
    @TableField(exist = false)
    private String[] idcardPhotoDownArr;
    /**
     * 司机手机号
     */
    @ApiModelProperty(value = "司机手机号")
    @Excel(name="司机手机号")
    private String phone;
    /**
     * 紧急联系人
     */
    @ApiModelProperty(value = "紧急联系人")
    @Excel(name="紧急联系人")
    @TableField("urgent_phone")
    private String urgentPhone;
    /**
     * 驾证级别
     */
    @ApiModelProperty(value = "驾证级别")
    @Excel(name="驾证级别")
    @TableField("license_level")
    private String licenseLevel;
    /**
     * 驾龄
     */
    @ApiModelProperty(value = "驾龄")
    @TableField("drive_years")
    private Integer driveYears;
    /**
     * 驾证照片
     */
    @ApiModelProperty(value = "驾证照片")
    @TableField("license_photo")
    private String licensePhoto;
    @TableField(exist = false)
    private String[] licensePhotoArr;
    /**
     * 从业资格证
     */
    @ApiModelProperty(value = "从业资格证")
    private String qualification;
    @TableField(exist = false)
    private String[] qualificationArr;
    /**
     * 驾驶证审验日期
     */
    @ApiModelProperty(value = "驾驶证审验日期")
    @Excel(name="驾驶证审验日期")
    @TableField("licenseLevel_time")
    private Date licenseLevelTime;
    /**
     * 驾驶证验审状态（0：已验审，1：待验审）
     */
    @ApiModelProperty(value = "驾驶证审验日期")
    @TableField("licenseLevel_status")
    private String licenseLevelStatus;
    /**
     * 从业证审验日期
     */
    @ApiModelProperty(value = "从业证审验日期")
    @Excel(name="从业证审验日期")
    @TableField("qualification_time")
    private Date qualificationTime;
    /**
     * 从业证验审状态（0：已验审，1：待验审）
     */
    @ApiModelProperty(value = "从业证审验日期")
    @TableField("qualification_status")
    private String qualificationStatus;
    /**
     * 所属银行
     */
    @ApiModelProperty(value = "所属银行")
    @TableField("deposit_bank")
    private String depositBank;
    /**
     * 银行卡号
     */
    @ApiModelProperty(value = "银行卡号")
    @Excel(name="银行卡号")
    @TableField("bank_number")
    private String bankNumber;
    /**
     * 车辆id
     */
    @ApiModelProperty(value = "车辆id")
    @TableField("plate_id")
    private Integer plateId;
    /**
     * 删除标志F
     */
    @ApiModelProperty(hidden = true)
    @TableField("del_flag")
    private String delFlag;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    @Excel(name="创建者")
    @TableField("create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Excel(name="创建时间")
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
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 租户id
     */
    @ApiModelProperty(hidden = true)
    @TableField("tenant_id")
    private Integer tenantId;
    /**
     * 小程序openid
     */
    @ApiModelProperty(value = "小程序openid")
    @TableField("xopen_id")
    private String xopenId;

    /**
     * 公众号openid
     */
    @ApiModelProperty(value = "公众号openid")
    @TableField("gopen_id")
    private String gopenId;

    @ApiModelProperty(value = "运费油卡余额")
    @Excel(name="运费油卡余额")
    @TableField("freight_oilcard_amount")
    private BigDecimal freightOilcardAmount;


    @Override
    protected Serializable pkVal() {
        return null;
    }
}
