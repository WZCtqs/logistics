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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ckj
 * @since 2019-02-11
 */
@Data
@ApiModel(description = "油卡月充值信息实体类")
@TableName("oil_major_recharge")
public class OilMajorRecharge extends Model<OilMajorRecharge> {

    private static final long serialVersionUID = 1L;

    /**
     * 主卡每天充值id
     */
    @ApiModelProperty(value = "主卡充值记录id(添加的时候不用写,后台自动生成)")
    @TableId(value = "major_recharge_id", type = IdType.AUTO)
    private Integer majorRechargeId;
    /**
     * 主卡id
     */
    @ApiModelProperty(value = "主卡id")
    @TableField("major_id")
    private Integer majorId;
    /**
     * 主卡号
     */
    @Excel(name = "主卡号")
    @ApiModelProperty(value = "主卡号")
    @TableField(exist = false)
    private String majorNumber;
    /**
     * 主卡名
     */
    @Excel(name = "主卡名")
    @ApiModelProperty(value = "主卡名")
    @TableField(exist = false)
    private String majorName;
    /**
     * 主卡充值金额
     */
    @Excel(name = "主卡充值金额")
    @ApiModelProperty(value = "主卡充值金额")
    @TableField("major_recharge_amount")
    private BigDecimal majorRechargeAmount;
    /**
     * 主卡充值时间
     */
    @Excel(name = "主卡充值时间")
    @ApiModelProperty(value = "主卡充值时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("major_recharge_date")
    private Date majorRechargeDate;

    @ApiModelProperty(value = "主卡充值方式：0：直接充值，1：加油站充值")
    @TableField("recharge_type")
    private String rechargeType;

    @ApiModelProperty(value = "主卡所属公司名称")
    @TableField(exist = false)
    private String majorCompany;

    /**
     * 删除标志（0代表存在 1代表删除）
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
    @ApiModelProperty(hidden = true)
    @TableField("tenant_id")
    private Integer tenantId;
    /**
     * 备注
     */
    @Excel(name = "备注")
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(exist = false)
    private String yearMonth;

    public Integer getMajorRechargeId() {
        return majorRechargeId;
    }

    public void setMajorRechargeId(Integer majorRechargeId) {
        this.majorRechargeId = majorRechargeId;
    }

    public Date getMajorRechargeDate() {
        return majorRechargeDate;
    }

    public void setMajorRechargeDate(Date majorRechargeDate) {
        this.majorRechargeDate = majorRechargeDate;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public BigDecimal getMajorRechargeAmount() {
        return majorRechargeAmount;
    }

    public void setMajorRechargeAmount(BigDecimal majorRechargeAmount) {
        this.majorRechargeAmount = majorRechargeAmount;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    protected Serializable pkVal() {
        return this.majorRechargeId;
    }

    @Override
    public String toString() {
        return "OilMajorRecharge{" +
        ", majorRechargeId=" + majorRechargeId +
        ", majorRechargeDate=" + majorRechargeDate +
        ", majorId=" + majorId +
        ", majorRechargeAmount=" + majorRechargeAmount +
        ", delFlag=" + delFlag +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        ", tenantId=" + tenantId +
        ", remark=" + remark +
        "}";
    }
}
