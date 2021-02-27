package com.zhkj.lc.trunk.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 车辆信息表
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@ApiModel(description = "车辆信息实体类")
@TableName("tru_truck")
@Data
public class TruTruck extends Model<TruTruck> {

    private static final long serialVersionUID = 1L;

    /**
     * 车辆id
     */
    @ApiModelProperty(value = "车辆id")
    @TableId(value = "truck_id", type = IdType.AUTO)
    private Integer truckId;
    /**
     * 车辆状态
     */
    private Integer status;

    /**
     * 司机id
     */
    @ApiModelProperty(value = "司机id")
    @TableField(value = "driver_id")
    private Integer driverId;

    /**
     * 司机名
     */
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String driverName;

    /**
      * 司机手机号
     */
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String phone;

    /**
     * 车辆的车主id(司机表中标识为车主的id)
     */
    @ApiModelProperty(value = "车辆的车主id(司机表中标识为车主的id)")
    @TableField(value = "truck_owner_id")
    private Integer truckOwnerId;

    /**
     * 所属车队id(车队表id)
     */
    @ApiModelProperty(value = "所属车队id(车队表id)")
    @TableField(value = "truck_team_id")
    private Integer truckTeamId;
    /**
     * 车队是否失信（0信任，1失信）
     */
    @ApiModelProperty(value = "车队是否失信（0信任，1失信）")
    @TableField(exist = false)
    private String isTrust;
    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    @TableField("plate_number")
    private String plateNumber;
    /**
     * 挂车牌号
     */
    @ApiModelProperty(value = "挂车牌号")
    @TableField("trailer_brand")
    private String trailerBrand;
    /**
     * 车长(chang)
     */
    @ApiModelProperty(value = "车长(chang)")
    @TableField("car_length")
    private String carLength;
    /**
     * 车重
     */
    @ApiModelProperty(value = "车重")
    @TableField("car_weight")
    private String carWeight;
    /**
     * 载重
     */
    @ApiModelProperty(value = "载重")
    @TableField("carry_capacity")
    private String carryCapacity;
    /**
     * 车辆种类
     * 二桥,三桥等
     */
    @ApiModelProperty(value = "车辆种类")
    private String type;
    /**
     * 车主
     */
    @ApiModelProperty(value = "车主")
    @TableField("truck_owner")
    private String truckOwner;
    /**
     * 车主手机号
     */
    @ApiModelProperty(value = "车主手机号")
    @TableField("truck_owner_phone")
    private String truckOwnerPhone;
    /**
     * 开户行
     */
    @ApiModelProperty(value = "开户行")
    @TableField("deposit_bank")
    private String depositBank;
    /**
     * 银行账号
     */
    @ApiModelProperty(value = "银行账号")
    @TableField("truck_card_number")
    private String truckCardNumber;

    /**
     * 车辆验审时间
     */
    @ApiModelProperty(value = "车辆验审时间")
    @TableField("truck_time")
    private Date truckTime;
    /**
     * 车辆验审状态（0：正常，1：临期提醒,2.过期）
     */
    @ApiModelProperty(value = "车辆验审状态")
    @TableField("truck_status")
    private String truckStatus;
    /**
     * 营运证验审时间
     */
    @ApiModelProperty(value = "营运证验审时间")
    @TableField("operation_time")
    private Date operationTime;
    /**
     * 营运证验审状态（0：正常，1：临期提醒,2.过期）
     */
    @ApiModelProperty(value = "营运证验审状态")
    @TableField("operation_status")
    private String operationStatus;
    /**
     * 保险验审时间
     */
    @ApiModelProperty(value = "保险验审时间")
    @TableField("policy_no_time")
    private Date policyNoTime;
    /**
     * 保险验审状态（0：正常，1：临期提醒,2.过期）
     */
    @ApiModelProperty(value = "保险验审状态")
    @TableField("policy_no_status")
    private String policyNoStatus;


    /**
     * 车架号
     */
    @ApiModelProperty(value = "车架号")
    @TableField("carframe_number")
    private String carframeNumber;
    /**
     * 营运号
     */
    @ApiModelProperty(value = "营运号")
    @TableField("operation_number")
    private String operationNumber;
    /**
     * 发票号
     */
    @ApiModelProperty(value = "发票号")
    @TableField("invoice_number")
    private String invoiceNumber;
    /**
     * 保险单号
     */
    @ApiModelProperty(value = "保险单号")
    @TableField("policy_no")
    private String policyNo;
    /**
     * 车辆品牌
     * 长安,东风等
     */
    @ApiModelProperty(value = "车辆品牌")
    @TableField("brand_no")
    private String brandNo;
    /**
     * 车辆类型
     * 自有,挂靠,外调
     */
    @ApiModelProperty(value = "车辆类型")
    private String attribute;
    /**
     * 车型
     * 集卡,半挂,高栏
     */
    @ApiModelProperty(value = "车型")
    @TableField("car_attribute")
    private String carAttribute;
    /**
     * 发动机号
     */
    @ApiModelProperty(value = "发动机号")
    @TableField("engine_number")
    private String engineNumber;
    /**
     * 行驶证
     */
    @ApiModelProperty(value = "行驶证")
    @TableField("driving_license")
    private String drivingLicense;
    /**
     * 是否监管（0：监管，2：否）
     */
    @ApiModelProperty(value = "是否监管（0：监管，2：否）")
    @TableField("is_supervise")
    private String isSupervise;
    /**
     * 删除标志（0代表存在 2代表删除）
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
    @ApiModelProperty(value = "创建时间")
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
     * 车辆证件
     */
    @TableField(exist = false)
    private TruTruckfile truTruckfile;

    @TableField(exist = false)
    private int[] truckIds;

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
     * 车队ids
     */
    @TableField(exist = false)
    private int[] truckTeamIds;

    /**
     * 司机对象
     */
    @TableField(exist = false)
    private TruDriver truDriver;

    /**
     * 司机对象列表
     */
    @TableField(exist = false)
    private List<TruDriver> truDriverList;

    /**
     * 车队对象
     */
    @TableField(exist = false)
    private TruTruckTeam truTruckTeam;

    public TruTruckfile getTruTruckfile() {
        return truTruckfile;
    }

    public void setTruTruckfile(TruTruckfile truTruckfile) {
        this.truTruckfile = truTruckfile;
    }

    public List<TruDriver> getTruDriverList() {
        return truDriverList;
    }

    public void setTruDriverList(List<TruDriver> truDriverList) {
        this.truDriverList = truDriverList;
    }

    public String getCarAttribute() {
        return carAttribute;
    }

    public void setCarAttribute(String carAttribute) {
        this.carAttribute = carAttribute;
    }

    public TruDriver getTruDriver() {
        return truDriver;
    }

    public void setTruDriver(TruDriver truDriver) {
        this.truDriver = truDriver;
    }

    public TruTruckTeam getTruTruckTeam() {
        return truTruckTeam;
    }

    public void setTruTruckTeam(TruTruckTeam truTruckTeam) {
        this.truTruckTeam = truTruckTeam;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getTrailerBrand() {
        return trailerBrand;
    }

    public void setTrailerBrand(String trailerBrand) {
        this.trailerBrand = trailerBrand;
    }

    public Integer getTruckTeamId() {
        return truckTeamId;
    }

    public void setTruckTeamId(Integer truckTeamId) {
        this.truckTeamId = truckTeamId;
    }

    public Integer getTruckOwnerId() {
        return truckOwnerId;
    }

    public void setTruckOwnerId(Integer truckOwnerId) {
        this.truckOwnerId = truckOwnerId;
    }

    public String getCarLength() {
        return carLength;
    }

    public void setCarLength(String carLength) {
        this.carLength = carLength;
    }

    public String getCarWeight() {
        return carWeight;
    }

    public void setCarWeight(String carWeight) {
        this.carWeight = carWeight;
    }

    public String getCarryCapacity() {
        return carryCapacity;
    }

    public void setCarryCapacity(String carryCapacity) {
        this.carryCapacity = carryCapacity;
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

    public int[] getTruckIds() {
        return truckIds;
    }

    public void setTruckIds(int[] truckIds) {
        this.truckIds = truckIds;
    }

    public Integer getTruckId() {
        return truckId;
    }

    public void setTruckId(Integer truckId) {
        this.truckId = truckId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTruckOwner() {
        return truckOwner;
    }

    public void setTruckOwner(String truckOwner) {
        this.truckOwner = truckOwner;
    }

    public String getTruckOwnerPhone() {
        return truckOwnerPhone;
    }

    public void setTruckOwnerPhone(String truckOwnerPhone) {
        this.truckOwnerPhone = truckOwnerPhone;
    }

    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }

    public String getTruckCardNumber() {
        return truckCardNumber;
    }

    public void setTruckCardNumber(String truckCardNumber) {
        this.truckCardNumber = truckCardNumber;
    }

    public String getCarframeNumber() {
        return carframeNumber;
    }

    public void setCarframeNumber(String carframeNumber) {
        this.carframeNumber = carframeNumber;
    }

    public String getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(String operationNumber) {
        this.operationNumber = operationNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getBrandNo() {
        return brandNo;
    }

    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getIsSupervise() {
        return isSupervise;
    }

    public void setIsSupervise(String isSupervise) {
        this.isSupervise = isSupervise;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    protected Serializable pkVal() {
        return this.truckId;
    }

    @Override
    public String toString() {
        return "TruTruck{" +
        "truckId=" + truckId +
        ", plateNumber=" + plateNumber +
        ", carLength=" + carLength +
        ", carWeight=" + carWeight +
        ", carryCapacity=" + carryCapacity +
        ", type=" + type +
        ", truckOwner=" + truckOwner +
        ", truckOwnerPhone=" + truckOwnerPhone +
        ", depositBank=" + depositBank +
        ", truckCardNumber=" + truckCardNumber +
        ", carframeNumber=" + carframeNumber +
        ", operationNumber=" + operationNumber +
        ", invoiceNumber=" + invoiceNumber +
        ", policyNo=" + policyNo +
        ", brandNo=" + brandNo +
        ", attribute=" + attribute +
        ", engineNumber=" + engineNumber +
        ", drivingLicense=" + drivingLicense +
        ", isSupervise=" + isSupervise +
        ", delFlag=" + delFlag +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        ", remark=" + remark +
        ", tenantId=" + tenantId +
        "}";
    }
}
