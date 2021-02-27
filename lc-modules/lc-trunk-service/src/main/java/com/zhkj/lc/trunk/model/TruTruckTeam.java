package com.zhkj.lc.trunk.model;

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

/**
 * <p>
 * 车队信息表
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@Data
@TableName("tru_truck_team")
@ApiModel(value = "TruTruckTeam",description = "承运商字段")
public class TruTruckTeam extends Model<TruTruckTeam> {

    private static final long serialVersionUID = 1L;

    /**
     * 车队id
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "truck_team_id", type = IdType.AUTO)
    private Integer truckTeamId;
    /**
     * 车队名字
     */
    @Excel(name="承运商名字")
    @TableField("team_name")
    @ApiModelProperty(value = "承运商名字")
    private String teamName;
    /**
     * 业务员
     */
    //@Excel(name="业务员")
    @ApiModelProperty(value = "业务员")
    private String saleman;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "负责人")
    @Excel(name="负责人")
    private String contact;
    /**
     * 职位
     */
    @ApiModelProperty(value = "职位")
    //@Excel(name="职位")
    @TableField("contact_job")
    private String contactJob;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    @Excel(name="联系电话")
    private String phone;
    /**
     * 联系人邮箱
     */
    @ApiModelProperty(value = "联系人邮箱")
    //@Excel(name="联系人邮箱")
    private String email;
    /**
     * 结算方式(单付/月付)
     */
    @ApiModelProperty(value = "结算方式")
    @Excel(name="结算方式")
    @TableField("pay_way")
    private String payWay;
    /**
     * 是否通过（0：通过，1：不通过）
     */
    @ApiModelProperty(value = "是否通过（0：通过，1：不通过）")
    //@Excel(name="是否通过")
    @TableField("is_passed")
    private String isPassed;
    /**
     * 是否合作（0：合作，1：非合作）
     */
    @ApiModelProperty(value = "是否签合同（0：是，1：否）")
    @Excel(name="是否签合同")
    @TableField("is_partner")
    private String isPartner;
    /**
     * 车队状态
     */
    @ApiModelProperty(value = "车队状态")
    //@Excel(name="车队状态")
    private String status;
    /**
     * 是否失信（1：失信，0：信任）
     */
    @ApiModelProperty(value = "是否失信")
    @Excel(name="是否失信")
    @TableField("is_trust")
    private String isTrust;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * 车队类型(0:个体车队，1:运输车队)
     */
    @ApiModelProperty(value = "车队类型")
    @Excel(name="车队类型")
    @TableField("team_type")
    private String teamType;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Excel(name="备注")
    private String remark;
    /**
     * 删除标志
     */
    @ApiModelProperty(value = "删除标志")
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
    @Excel(name = "创建时间")
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

    @TableField(exist = false)
    private int[] truckTeamIds;

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
     * 某车队车辆总数
     */
    @ApiModelProperty(value = "车辆数")
    @Excel(name="车辆数")
    @TableField(exist = false)
    private Integer truckSum;
    /**
     * 某车队司机总数
     */
    @ApiModelProperty(value = "司机数")
    @Excel(name="司机数")
    @TableField(exist = false)
    private Integer driverSum;

    public String getTeamType() {
        return teamType;
    }

    public void setTeamType(String teamType) {
        this.teamType = teamType;
    }

    public Integer getTruckSum() {
        return truckSum;
    }

    public void setTruckSum(Integer truckSum) {
        this.truckSum = truckSum;
    }

    public Integer getDriverSum() {
        return driverSum;
    }

    public void setDriverSum(Integer driverSum) {
        this.driverSum = driverSum;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
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

    public int[] getTruckTeamIds() {
        return truckTeamIds;
    }

    public void setTruckTeamIds(int[] truckTeamIds) {
        this.truckTeamIds = truckTeamIds;
    }

    public Integer getTruckTeamId() {
        return truckTeamId;
    }

    public void setTruckTeamId(Integer truckTeamId) {
        this.truckTeamId = truckTeamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getSaleman() {
        return saleman;
    }

    public void setSaleman(String saleman) {
        this.saleman = saleman;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactJob() {
        return contactJob;
    }

    public void setContactJob(String contactJob) {
        this.contactJob = contactJob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(String isPassed) {
        this.isPassed = isPassed;
    }

    public String getIsPartner() {
        return isPartner;
    }

    public void setIsPartner(String isPartner) {
        this.isPartner = isPartner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsTrust() {
        return isTrust;
    }

    public void setIsTrust(String isTrust) {
        this.isTrust = isTrust;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    @Override
    protected Serializable pkVal() {
        return this.truckTeamId;
    }

    @Override
    public String toString() {
        return "TruTruckTeam{" +
                "truckTeamId=" + truckTeamId +
                ", teamName='" + teamName + '\'' +
                ", saleman='" + saleman + '\'' +
                ", contact='" + contact + '\'' +
                ", contactJob='" + contactJob + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", payWay='" + payWay + '\'' +
                ", isPassed='" + isPassed + '\'' +
                ", isPartner='" + isPartner + '\'' +
                ", status='" + status + '\'' +
                ", isTrust='" + isTrust + '\'' +
                ", teamType='" + teamType + '\'' +
                ", remark='" + remark + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                ", tenantId=" + tenantId +
                ", truckTeamIds=" + Arrays.toString(truckTeamIds) +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", truckSum=" + truckSum +
                ", driverSum=" + driverSum +
                '}';
    }
}
