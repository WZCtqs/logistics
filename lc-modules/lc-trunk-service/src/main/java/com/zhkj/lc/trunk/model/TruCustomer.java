package com.zhkj.lc.trunk.model;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.zhkj.lc.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <p>
 * 客户信息表
 * </p>
 *
 * @author wzc
 * @since 2018-12-07
 */
@ApiModel(value = "TruCustomer",description = "发货方字段")
@TableName("tru_customer")
@Data
public class TruCustomer extends Model<TruCustomer> {

    private static final long serialVersionUID = 1L;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "ID")
    @TableId(value = "customer_id", type = IdType.AUTO)
    private Integer customerId;
    /**
     * 客户名字
     */
    @Excel(name="客户名称")
    @ApiModelProperty(value = "客户名称")
    @TableField("customer_name")
    private String customerName;
    /**
     * 业务员
     */
    //@Excel(name="业务员")
    @ApiModelProperty(value = "业务员")
    private String saleman;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人名称")
    @Excel(name="联系人")
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
    @Excel(name="联系人邮箱")
    private String email;

    /**
     * 客户地址
     */
    @Excel(name="客户地址")
    @ApiModelProperty(value = "客户地址")
    @TableField("customer_address")
    private String customerAddress;
    /**
     * 客户类型
     */
    @Excel(name="客户类型")
    @ApiModelProperty(value = "客户类型")
    @TableField("customer_type")
    private String customerType;

    /**
     * 付款方式(单付/月付)
     */
    @Excel(name="付款方式")
    @TableField("pay_way")
    @ApiModelProperty(value = "付款方式")
    private String payWay;
    /**
     * 是否通过（0：通过，1：不通过）
     */
    //@Excel(name="是否通过")
    @ApiModelProperty(value = "是否通过（0：通过，1：不通过）")
    @TableField("is_passed")
    private String isPassed;
    /**
     * 是否合作（0：合作，1：非合作）
     */
    //@Excel(name="是否合作")
    @ApiModelProperty(value = "是否合作（0：合作，1：非合作）")
    @TableField("is_partner")
    private String isPartner;
    /**
     * 客户状态
     */
    //@Excel(name="客户状态")
    @ApiModelProperty(value = "客户状态")
    private String status;
    /**
     * 是否失信（1：失信，0：信任）
     */
    //@Excel(name="是否失信")
    @ApiModelProperty(value = "是否失信（1：失信，0：信任）")
    @TableField("is_trust")
    private String isTrust;
    /**
     * 备注
     */
    @Excel(name="备注")
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 删除标志
     */
    @TableField("del_flag")
    @ApiModelProperty(value = "删除标志")
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
    @Excel(name = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
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

    @TableField("sex")
    private String sex;

    @TableField("photo")
    private String photo;

    @TableField(exist = false)
    private int[] customerIds;

    /**
     * 开始时间区间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "开始时间区间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date beginTime;
    /**
     * 截止时间区间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "截止时间区间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endTime;
    /**
     * 客户公众号openid
     */
    //@Excel(name="客户公众号openid")
    @ApiModelProperty(value = "客户公众号openid")
    @TableField("gopen_id")
    private String gopenId;

    public String getGopenId() {
        return gopenId;
    }

    public void setGopenId(String gopenId) {
        this.gopenId = gopenId;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
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
    public int[] getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(int[] customerIds) {
        this.customerIds = customerIds;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
        return this.customerId;
    }

    @Override
    public String toString() {
        return "TruCustomer{" +
        "customerId=" + customerId +
        ", customerName=" + customerName +
        ", saleman=" + saleman +
        ", contact=" + contact +
        ", contactJob=" + contactJob +
        ", phone=" + phone +
        ", email=" + email +
        ", customerAddress=" + customerAddress +
        ", customerType=" + customerType +
        ", payWay=" + payWay +
        ", isPassed=" + isPassed +
        ", isPartner=" + isPartner +
        ", status=" + status +
        ", isTrust=" + isTrust +
        ", remark=" + remark +
        ", delFlag=" + delFlag +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        ", tenantId=" + tenantId +
        "}";
    }
}
