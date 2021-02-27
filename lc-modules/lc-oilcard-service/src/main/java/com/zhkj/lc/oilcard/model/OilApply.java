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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 油卡申请表
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@ApiModel(description = "油卡申请发送实体类")
@TableName("oil_apply")
public class OilApply extends Model<OilApply> {

    private static final long serialVersionUID = 1L;

    /**
     * 申请id
     */
    @ApiModelProperty(value = "申请id")
    @TableId(value = "apply_id", type = IdType.AUTO)
    private Integer applyId;
    /**
     * 申请车辆信息
     */
    @ApiModelProperty(value = "申请车辆id")
    @TableField("truck_id")
    private Integer truckId;

    /** 车牌号 */
    @ApiModelProperty(value = "车牌号")
    @Excel(name = "车牌号")
    @TableField(exist = false)
    private String plateNumber;

    /** 车主 */
    @ApiModelProperty(value = "车主",readOnly = true)
    @Excel(name = "车主")
    @TableField(exist = false)
    private String truckOwner;

    /** 车辆类型 */
    @ApiModelProperty(value = "车辆类型")
    @Excel(name = "车辆类型")
    private String attribute;

    /** 车辆拥有者联系方式 */
    @ApiModelProperty(value = "车辆拥有者联系方式")
    @Excel(name = "联系方式")
    @TableField(exist = false)
    private String truckOwnerPhone;

    /**
     * 已持卡数量
     */
    @ApiModelProperty(value = "已持卡数量")
    @Excel(name = "已持卡数量")
    @TableField("truck_applied")
    private Integer truckApplied;

    /**
     * 申请日期
     */
    @ApiModelProperty(value = "申请日期", example = "2018-12-12")
    @Excel(name = "申请日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("apply_date")
    private Date applyDate;

    /**
     * 油卡类型
     */
    @ApiModelProperty(value = "申请的油卡类型",example = "1或者2或者3")
    @Excel(name = "申请油卡类型")
    @TableField("apply_card_type")
    private String applyCardType;
    /**
     * 申请办卡地点
     */
    @ApiModelProperty(value = "申请办卡地点")
    @Excel(name = "申请办卡地点")
    @TableField("open_card_place")
    private String openCardPlace;
    /**
     * 申请备注
     */
    @ApiModelProperty(value = "申请备注")
    @Excel(name = "申请备注")
    @TableField("apply_remark")
    private String applyRemark;
    /**
     * 所属司机/车主id
     */
    @ApiModelProperty(value = "油卡申请人id")
    @TableField("owner_driver_id")
    private Integer ownerDriverId;
    /**
     * 司机姓名
     */
    @ApiModelProperty(value = "油卡申请人名字")
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
     * 审核
     */
    @ApiModelProperty(value = "审核状态(是否通过)",example = "0或1")
    @Excel(name = "审核状态(是否通过)")
    @TableField("is_passed")
    private String isPassed;
    /**
     * 审核备注
     */
    @ApiModelProperty(value = "审核备注")
    @Excel(name = "审核备注")
    @TableField("is_passed_remark")
    private String isPassedRemark;
    /**
     * 发送油卡号
     */
    @ApiModelProperty(value = "发送的油卡号")
    @Excel(name = "发送的油卡号")
    @TableField("oil_card_number")
    private String oilCardNumber;

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
     * 押金
     */
    @ApiModelProperty(value = "押金")
    @Excel(name = "押金")
    private BigDecimal deposit;
    /**
     * 发放日期
     */
    @ApiModelProperty(value = "发放日期",example = "2018-12-12")
    @Excel(name = "发放日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("get_date")
    private Date getDate;

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
     * 发放状态
     */
    @ApiModelProperty(value = "发放状态(0发送1未发送)")
    @Excel(name = "发放状态(是否发送)")
    @TableField("get_status")
    private String getStatus;
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

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public Integer getTruckId() {
        return truckId;
    }

    public void setTruckId(Integer truckId) {
        this.truckId = truckId;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getApplyCardType() {
        return applyCardType;
    }

    public void setApplyCardType(String applyCardType) {
        this.applyCardType = applyCardType;
    }

    public String getOpenCardPlace() {
        return openCardPlace;
    }

    public void setOpenCardPlace(String openCardPlace) {
        this.openCardPlace = openCardPlace;
    }

    public Integer getTruckApplied() {
        return truckApplied;
    }

    public void setTruckApplied(Integer truckApplied) {
        this.truckApplied = truckApplied;
    }

    public String getApplyRemark() {
        return applyRemark;
    }

    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
    }

    public String getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(String isPassed) {
        this.isPassed = isPassed;
    }

    public String getIsPassedRemark() {
        return isPassedRemark;
    }

    public void setIsPassedRemark(String isPassedRemark) {
        this.isPassedRemark = isPassedRemark;
    }

    public String getOilCardNumber() {
        return oilCardNumber;
    }

    public void setOilCardNumber(String oilCardNumber) {
        this.oilCardNumber = oilCardNumber;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public Date getGetDate() {
        return getDate;
    }

    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }

    public String getGetStatus() {
        return getStatus;
    }

    public void setGetStatus(String getStatus) {
        this.getStatus = getStatus;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getTruckOwner() {
        return truckOwner;
    }

    public void setTruckOwner(String truckOwner) {
        this.truckOwner = truckOwner;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getTruckOwnerPhone() {
        return truckOwnerPhone;
    }

    public void setTruckOwnerPhone(String truckOwnerPhone) {
        this.truckOwnerPhone = truckOwnerPhone;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getOwnerDriverId() {
        return ownerDriverId;
    }

    public void setOwnerDriverId(Integer ownerDriverId) {
        this.ownerDriverId = ownerDriverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public String getMajorNumber() {
        return majorNumber;
    }

    public void setMajorNumber(String majorNumber) {
        this.majorNumber = majorNumber;
    }

    public String[] getTruckIds() {
        return truckIds;
    }

    public void setTruckIds(String[] truckIds) {
        this.truckIds = truckIds;
    }

    @Override
    protected Serializable pkVal() {
        return this.applyId;
    }

    @Override
    public String toString() {
        return "OilApply{" +
                "applyId=" + applyId +
                ", truckId=" + truckId +
                ", plateNumber='" + plateNumber + '\'' +
                ", truckOwner='" + truckOwner + '\'' +
                ", attribute='" + attribute + '\'' +
                ", truckOwnerPhone='" + truckOwnerPhone + '\'' +
                ", truckApplied=" + truckApplied +
                ", applyDate=" + applyDate +
                ", applyCardType='" + applyCardType + '\'' +
                ", openCardPlace='" + openCardPlace + '\'' +
                ", applyRemark='" + applyRemark + '\'' +
                ", ownerDriverId=" + ownerDriverId +
                ", driverName='" + driverName + '\'' +
                ", driverPhone='" + driverPhone + '\'' +
                ", isPassed='" + isPassed + '\'' +
                ", isPassedRemark='" + isPassedRemark + '\'' +
                ", oilCardNumber='" + oilCardNumber + '\'' +
                ", majorId=" + majorId +
                ", majorNumber='" + majorNumber + '\'' +
                ", deposit=" + deposit +
                ", getDate=" + getDate +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", getStatus='" + getStatus + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                ", tenantId=" + tenantId +
                '}';
    }
}
