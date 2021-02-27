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

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 油卡异常表
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@ApiModel(description = "油卡异常信息")
@TableName("oil_exception")
@Data
public class OilException extends Model<OilException> {

    private static final long serialVersionUID = 1L;

    /**
     * 异常id
     */
    @TableId(value = "exception_id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
    private Integer exceptionId;
    /**
     * 油卡id
     */
    @TableField("oil_card_id")
    @ApiModelProperty(value = "油卡基本信息id")
    private Integer oilCardId;
    /**
     * 车辆id
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "车辆id")
    private Integer truckId;

    /**车牌号*/
    @TableField(exist = false)
    @ApiModelProperty(value = "车牌号(只显示不保存)")
    @Excel(name = "车牌号")
    private String plateNumber;
    /**车辆类型*/
    @TableField(exist = false)
    @Excel(name = "车辆类型")
    @ApiModelProperty(value = "车辆类型(只显示不保存)")
    private String attribute;
    /**车主*/
    @TableField(exist = false)
    @Excel(name = "车主")
    @ApiModelProperty(value = "车主(只显示不保存)")
    private String truckOwner;
    /**车主联系方式*/
    @TableField(exist = false)
    @Excel(name = "车主联系方式")
    @ApiModelProperty(value = "车主联系方式(只显示不保存)")
    private String truckOwnerPhone;

    /**所属司机/车主id*/
    @ApiModelProperty(value = "油卡申请人id(司机id)(后台不保存不显示，小程序端搜索时使用)")
    @TableField(exist = false)
    private Integer ownerDriverId;

    /**司机名*/
    @ApiModelProperty(value = "申请人")
    @Excel(name = "申请人")
    @TableField(exist = false)
    private String driverName;

    /**申请人手机号*/
    @ApiModelProperty(value = "申请人手机号")
    @Excel(name = "申请人手机号")
    @TableField(exist = false)
    private String driverPhone;

    /**油卡号*/
    @TableField(exist = false)
    @Excel(name = "油卡号")
    @ApiModelProperty(value = "油卡号(只显示不保存)")
    private  String oilCardNumber;

    /**主卡id*/
    @ApiModelProperty(value = "主卡id")
    @Excel(name = "主卡id")
    @TableField(exist = false)
    private Integer majorId;

    /**主卡号*/
    @ApiModelProperty(value = "主卡号")
    @Excel(name = "主卡号")
    @TableField(exist = false)
    private String majorNumber;
    /**油卡类型*/
    @TableField(exist = false)
    @Excel(name = "油卡类型")
    @ApiModelProperty(value = "油卡类型(只显示不保存)")
    private String cardType;
    /**油卡余额*/
    @TableField(exist = false)
    @Excel(name = "油卡余额")
    @ApiModelProperty(value = "油卡余额(只显示不保存)")
    private  String amount;

    /**
     * 异常发起日期
     */
    @TableField("exception_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "异常发起日期")
    @ApiModelProperty(value = "异常发起日期")
    private Date exceptionDate;
    /**
     * 异常类型
     */
    @TableField("exception_type")
    @Excel(name = "异常类型")
    @ApiModelProperty(value = "异常类型（1挂失，2解锁，3消磁）")
    private String exceptionType;
    /**
     * 领卡时间
     */
    @TableField("collar_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(hidden = true)
    private Date collarDate;
    /**
     * 领卡人
     */
    @TableField("collar_people")
    @ApiModelProperty(hidden = true)
    private String collarPeople;
    /**
     * 补办油卡号
     */
    @TableField("makeup_card_number")
    @Excel(name = "补办油卡号")
    @ApiModelProperty(value = "补办油卡号")
    private String makeupCardNumber;
    /**
     * 补办地点
     */
    @TableField("makeup_place")
    @Excel(name = "补办地点")
    @ApiModelProperty(value = "补办地点")
    private String makeupPlace;
    /**
     * 处理状态（0代表未处理 1代表已处理）
     */
    @Excel(name = "处理状态")
    @ApiModelProperty(value = "处理状态")
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableField("del_flag")
    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    private String delFlag;
    /**
     * 创建者
     */
    @TableField("create_by")
    @ApiModelProperty(value = "创建者")
    private String createBy;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新者
     */
    @TableField("update_by")
    @ApiModelProperty(value = "更新者")
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField("update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    //查询时间时使用
    @TableField(exist = false)
    @ApiModelProperty(value = "时间段查询时起始时间")
    private String beginTime;
    @TableField(exist = false)
    @ApiModelProperty(value = "时间段查询时截止时间")
    private String endTime;

    @TableField(exist = false)
    private String[] truckIds;

    /**
     * 申请备注
     */
    @Excel(name = "备注")
    @ApiModelProperty(value = "备注")
    private String remark;

    @Override
    protected Serializable pkVal() {
        return this.exceptionId;
    }

    @Override
    public String toString() {
        return "OilException{" +
                "exceptionId=" + exceptionId +
                ", oilCardId=" + oilCardId +
                ", truckId=" + oilCardId +
                ", exceptionDate=" + exceptionDate +
                ", exceptionType='" + exceptionType + '\'' +
                ", collarDate=" + collarDate +
                ", collarPeople='" + collarPeople + '\'' +
                ", makeupCardNumber='" + makeupCardNumber + '\'' +
                ", makeupPlace='" + makeupPlace + '\'' +
                ", status='" + status + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                ", tenantId=" + tenantId +
                ", plateNumber='" + plateNumber + '\'' +
                ", attribute='" + attribute + '\'' +
                ", truckOwner='" + truckOwner + '\'' +
                ", truckOwnerPhone='" + truckOwnerPhone + '\'' +
                ", cardType='" + cardType + '\'' +
                ", oilCardNumber='" + oilCardNumber + '\'' +
                ", majorNumber='" + majorNumber + '\'' +
                ", amount='" + amount + '\'' +
                ", ownerDriverId=" + ownerDriverId +
                ", driverName='" + driverName + '\'' +
                ", driverPhone='" + driverName + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", remark='" + remark + '\'' +
                '}';
    }
}
